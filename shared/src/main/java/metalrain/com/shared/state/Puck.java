package metalrain.com.shared.state;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Adam on 4/28/2016.
 */
public class Puck implements Steppable, HasPoint, Drawable, GameEntity {

    GameDisc point = new GameDisc();
    private double max_speed = 50;

    public Puck() {
        point.setX(GameState.BOARD_WIDTH/2);
        point.setY(GameState.BOARD_HEIGHT/2);
        point.setRadius(40);
    }

    @Override
    public void step(double time, GameState state) {
        point.setXs(point.getXs() * 0.8f);
        point.setYs(point.getYs() * 0.8f);
        double speed = Math.sqrt(point.getXs() * point.getXs() + point.getYs() * point.getYs());
        if (speed>max_speed) {
            point.setXs((float) ((point.getXs() /speed)*max_speed));
            point.setYs((float) ((point.getYs() /speed)*max_speed));
        }
        point.setX(point.getX() + point.getXs());
        point.setY(point.getY() + point.getYs());
    }

    @Override
    public GameDisc getPoint() {
        return point;
    }

    Paint paint = new Paint();
    @Override
    public void draw(Canvas c) {
        paint.setColor(Color.RED);
        double position_x = (point.getX() / GameState.BOARD_WIDTH)*c.getWidth();
        double position_y = (point.getY() / GameState.BOARD_HEIGHT)*c.getHeight();
        c.drawCircle((float)position_x, (float)position_y, (float) point.getRadius(), paint);

    }
}
