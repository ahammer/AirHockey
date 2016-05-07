package metalrain.com.shared.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import metalrain.com.shared.R;
import metalrain.com.shared.WakeLockedActivity;
import metalrain.com.shared.server.Server;
import metalrain.com.shared.state.GameState;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static metalrain.com.shared.NetworkTools.getCurrentIp;

public class ClientActivity extends WakeLockedActivity {
    private Subscription touchSubscription;
    private View mainView;
    private static String mServerIp = null;
    public static Integer mPlayerId = null;
    ZMQ.Context context;

    public static void StartActivity(Context ctx, String ip) {
        Intent intent = new Intent(ctx, ClientActivity.class);
        intent.putExtra("ip", ip);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        mainView = findViewById(R.id.touch_view);
        mainView.setOnTouchListener((view, event) -> {
            motionEventOnSubscribe.emit(event);
            return true;
        });
        context = ZMQ.context(1);
        connectToServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(() -> context.term());
    }

    private void connectToServer() {
        new Thread(() -> {
            String ip = getIntent().getStringExtra("ip");
            ZMQ.Socket socket = context.socket(ZMQ.REQ);
            try {
                socket.connect("tcp://" + ip + ":" + getString(R.string.port));
                socket.setReceiveTimeOut(1000);
                List<String> message = new ArrayList<String>();
                message.add(Server.Command.JOIN.command);
                message.add(getCurrentIp(this));
                socket.send(messagePack.write(message));
                byte[] responseData = socket.recv(0);
                if (responseData != null) {
                    List<String> messageList = messagePack.read(responseData, Templates.tList(Templates.TString));
                    if (messageList.get(0).startsWith(Server.Command.JOIN.command)) {
                        mPlayerId = Integer.valueOf(messageList.get(1));
                        mServerIp = ip;
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
            //Failed
            runOnUiThread(() -> {
                finish();
            });
        }).start();

    }

    private MotionEventOnSubscribe motionEventOnSubscribe;
    Observable<MotionEvent> motionEventObservable = Observable.create(motionEventOnSubscribe = new MotionEventOnSubscribe());
    @Override
    protected void onResume() {
        super.onResume();

        touchSubscription = motionEventObservable
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                //We filter all events when we don't have a server IP
                .filter(motionEvent -> mServerIp != null)
                //We filter again by move and down events
                .filter(motionEvent ->
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE
                        ||
                        motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                //And sample at a 100ms rate, to prevent server overload
                .sample(100,TimeUnit.MILLISECONDS)
                //And finally, reportLocation to the server.
                .subscribe(motionEvent -> {
                    reportLocation(motionEvent.getX(), motionEvent.getY());
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        touchSubscription.unsubscribe();
    }

    MessagePack messagePack = new MessagePack();
    private void reportLocation(final float x, final float y) {

        ZMQ.Socket socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://"+mServerIp+":"+getString(R.string.port));
        List<String> message = new ArrayList();
        message.add(Server.Command.UPDATE.command);
        double nx = x/mainView.getWidth();
        double ny = y/mainView.getHeight();
        nx *= GameState.BOARD_WIDTH;
        ny *= GameState.BOARD_HEIGHT;


        message.add(String.valueOf((int)nx));
        message.add(String.valueOf((int)ny));
        message.add(String.valueOf(mPlayerId));

        try {
            socket.send(messagePack.write(message));
            byte[] result = socket.recv(0);
            List<String> resultList = messagePack.read(result, Templates.tList(Templates.TString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    private static class MotionEventOnSubscribe implements Observable.OnSubscribe<MotionEvent> {
        public Subscriber<? super MotionEvent> mSubscriber;

        @Override
        public void call(Subscriber<? super MotionEvent> subscriber) {
            mSubscriber = subscriber;
        }

        public void emit(MotionEvent event) {
            if (mSubscriber != null && !mSubscriber.isUnsubscribed()) {
                mSubscriber.onNext(event);
            }
        }
    }
}
