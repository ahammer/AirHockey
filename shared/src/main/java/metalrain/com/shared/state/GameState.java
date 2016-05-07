package metalrain.com.shared.state;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 4/23/2016.
 */
public class GameState implements Steppable, Drawable{
    public final static double BOARD_WIDTH = 3840;
    public final static double BOARD_HEIGHT = 2048;

    Map<String, Player> playerIpMap = new HashMap<>();

    List<Player> players = new ArrayList<>();
    Puck thePuck = new Puck();
    private Vector2 collisionPoint;

    public Vector2 getCollisionPoint() {
        return collisionPoint;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getNewPlayer(String ip) {
        if (playerIpMap.containsKey(ip)) return playerIpMap.get(ip);
        Player p = new Player();
        players.add(p);
        playerIpMap.put(ip, p);
        return p;
    }

    public void updatePlayerPosition(int player_id, double x, double y) {
        for (Player p: players) {
            if (p.getNumber() == player_id) {
                p.setInputPosition(x,y);
                return;
            }
        }
    }

    @Override
    public void step(double time, GameState state) {

        collisionPoint = null;
        List<GameEntity> entities = getEntityList();
        for (GameEntity e:entities) {
            if (e instanceof Steppable) {
                ((Steppable) e).step(time, state);
            }
        }
        try {
            for (int i = 0; i < entities.size(); i++) {
                for (int j = i + 1; j < entities.size(); j++) {
                    GameEntity e1 = entities.get(i);
                    GameEntity e2 = entities.get(j);
                    GameDisc p1 = ((HasPoint) e1).getPoint();
                    GameDisc p2 = ((HasPoint) e2).getPoint();
                    if (p1.collides(p2)) {
                        collisionPoint = p1.getCollisionPoint(p2);

                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void draw(Canvas c) {
        for (Player p:players) p.draw(c);
        thePuck.draw(c);
    }

    private List<GameEntity> getEntityList() {
        ArrayList<GameEntity> list = new ArrayList<>();
        list.addAll(players);
        list.add(thePuck);
        return list;
    }
}


