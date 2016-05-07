package metalrain.com.shared.server;

import android.content.Context;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.List;

import metalrain.com.shared.R;

import static metalrain.com.shared.NetworkTools.getCurrentIp;

/**
 * Created by Adam on 4/21/2016.
 */
public abstract class Server implements Runnable {
    private final String currentIp;
    ZMQ.Context context = ZMQ.context(1);
    private final MessagePack messagePack = new MessagePack();
    private final String port;
    private boolean stop = false;

    public Server(Context ctx) {
        currentIp = getCurrentIp(ctx);
        port = ctx.getString(R.string.port);
    }


    @Override
    public void run() {
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        socket.bind("tcp://"+currentIp+":"+port);
        while(!Thread.currentThread().isInterrupted() && !stop) {
            final byte[] msg = socket.recv(0);
            try {
                List<String> message = messagePack.read(msg, Templates.tList(Templates.TString));
                socket.send(messagePack.write(handleMessage(message)), 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public void stop() {
        stop = true;
        new Thread(() -> context.term());
    }

    protected abstract List<String> handleMessage(List<String> message);



    /****************************************************************
     * These are the commands and their abbreviation for on the wire.
     */
    public enum Command{ JOIN("J"), UPDATE("U");
        public final String command;

        Command(String command) {
            this.command = command;
        }


    }

}
