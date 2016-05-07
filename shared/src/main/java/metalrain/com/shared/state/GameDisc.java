package metalrain.com.shared.state;

/**
 * Created by Adam on 4/25/2016.
 */
public class GameDisc {
    private Vector2 point = new Vector2();
    private Vector2 velocity = new Vector2();
    private double radius;
    private double weight = 10; //In kg


    public boolean collides(GameDisc p) {
        double dx = getX() - p.getX();
        double dy = getY() - p.getY();
        double dist = Math.sqrt(dx*dx+dy*dy);
        if (dist < (getRadius() + p.getRadius())) return true;
        return false;
    }

    public Vector2 getCollisionPoint(GameDisc p) {
        if (!collides(p)) return null;

        Vector2 center1 = getPoint();
        Vector2 center2 = p.getPoint();
        return center2.delta(center1).normalize().multiply(getRadius()).add(center1);
    }


    public double getX() {
        return point.x;
    }

    public void setX(double x) {
        point.setX(x);
    }

    public double getY() {
        return point.y;
    }

    public void setY(double y) {
        point.setY(y);
    }

    public double getXs() {
        return velocity.x;
    }

    public void setXs(double xs) {
        velocity.setX(xs);
    }

    public double getYs() {
        return velocity.y;
    }

    public void setYs(double ys) {
        velocity.setY(ys);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPoint() {
        return point;
    }


}
