package metalrain.com.shared.state;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Adam on 4/23/2016.
 */
public class Player implements GameEntity, Steppable, Drawable, HasPoint {

    private static final double PADDLE_SIZE  = 150;
    private static final float max_speed = 50f;
    GameDisc point = new GameDisc();
    final int number;
    final Team team;
    private double input_x, input_y;

    public int getNumber() {
        return number;
    }

    static int num = 0;
    public Player() {
        this.number = num++;
        team = Team.BLUE;
        point.setRadius(PADDLE_SIZE);
    }


    public void setInputPosition(double x, double y) {
        this.input_x = x;
        this.input_y = y;
    }


    @Override
    public void step(double time, GameState state) {
        point.setXs((float) (point.getXs() + (input_x - point.getX())/5.0)/2);
        point.setYs((float) (point.getYs() + (input_y - point.getY())/5.0)/2);

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

    Paint p = new Paint();
    @Override
    public void draw(Canvas c) {
        double position_x = (point.getX() / GameState.BOARD_WIDTH)*c.getWidth();
        double position_y = (point.getY() / GameState.BOARD_HEIGHT)*c.getHeight();
        c.drawCircle((float)position_x, (float)position_y, (float) point.getRadius(), p);
    }

    enum Team {RED, BLUE};


}
