package metalrain.com.shared.server;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import metalrain.com.shared.R;
import metalrain.com.shared.WakeLockedActivity;
import metalrain.com.shared.state.Player;
import metalrain.com.shared.state.GameState;

public class ServerActivity extends WakeLockedActivity {
    private GameState airHockey = new GameState();
    private Server server;
    private Thread thread;

    public GameState getAirHockey() {
        return airHockey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_main);
        server = new Server(this) {
            @Override
            protected List<String> handleMessage(List<String> msg) {
                List<String> result = new ArrayList<>();

                result.add(msg.get(0)); //Return the command
                if (msg.get(0).equals(Server.Command.JOIN.command)) {

                    Player p = airHockey.getNewPlayer(msg.get(1));
                    result.add(String.valueOf(p.getNumber()));
                    return result;
                } else if (msg.get(0).equals(Server.Command.UPDATE.command)) {
                    double x = Double.valueOf(msg.get(1));
                    double y = Double.valueOf(msg.get(2));
                    int player_id = Integer.valueOf(msg.get(3));


                    airHockey.updatePlayerPosition(player_id, x,  y);
                    return result;
                }
                return Collections.EMPTY_LIST;
            }
        };

        GameRenderer gameView = (GameRenderer)findViewById(R.id.game_view);
        gameView.setState(airHockey);
        thread = new Thread(server);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
        server.stop();
    }

}
