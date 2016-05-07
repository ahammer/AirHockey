package metalrain.com.demosactivity;


import android.app.Activity;
import android.os.Bundle;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import metalrain.com.shared.server.GameRenderer;
import metalrain.com.shared.state.GameState;
import metalrain.com.shared.state.Player;

public class AutoGameActivity extends Activity {

    private Player P1;
    private Player P2;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    GameState state = new GameState();
    private ScheduledFuture<?> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_main);

        P1 = state.getNewPlayer("test1");
        P2 = state.getNewPlayer("test2");

        Player shouldBeP1 = state.getNewPlayer("test1");
        assert shouldBeP1 == P1;

        GameRenderer gameView = (GameRenderer)findViewById(metalrain.com.shared.R.id.game_view);
        gameView.setState(state);

        future = executorService.scheduleAtFixedRate(() -> {
            updatePlayer(P1, System.currentTimeMillis()/10);
            updatePlayer(P2, -System.currentTimeMillis()/5);
        },0,100, TimeUnit.MILLISECONDS);


    }

    private void updatePlayer(Player player, long l) {
        double c1 = Math.cos(l/1000.0);
        double c2 = Math.cos(l/800.0);
        double c3 = Math.cos(l/600.0);
        double c4 = Math.cos(l/400.0);

        double s1 = Math.sin(l/1000.0);
        double s2 = Math.sin(l/800.0);
        double s3 = Math.sin(l/600.0);
        double s4 = Math.sin(l/400.0);

        double x = (c1+s1+c3+s3)/4.0;
        double y = (c2+s2+c4+s4)/4.0;
        double hw = GameState.BOARD_WIDTH/2;
        double hh = GameState.BOARD_HEIGHT/2;
        x = (x*hw)+hw;
        y = (y*hh)+hh;

        player.setInputPosition(x,y);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        future.cancel(false);

    }
}
