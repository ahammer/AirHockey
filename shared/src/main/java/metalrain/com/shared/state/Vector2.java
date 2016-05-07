package metalrain.com.shared.state;

/**
 * Created by Adam on 4/28/2016.
 */
public class Vector2 {
    public double x;
    public double y;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {

        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x*x+y*y);
    }

    //Returns a normalized point, that has length = 1;
    public Vector2 normalize() {
        Vector2 p = new Vector2();
        double length = getLength();
        p.x = x/length;
        p.y = y/length;
        return p;
    }

    //Creates new vector
    public Vector2 delta(Vector2 other) {
        Vector2 p = new Vector2();
        p.x = getX() - other.getX();
        p.y = getY() - other.getY();
        return p;
    }

    //Modifies this vector
    public Vector2 multiply(double radius) {
        x *= radius;
        y *= radius;
        return this;
    }

    //Modifies this vector
    public Vector2 add(double xOffset, double yOffset) {
        x += xOffset;
        y += yOffset;
        return this;
    }

    public Vector2 add(Vector2 offsetVector) {
        return this.add(offsetVector.x, offsetVector.y);
    }
}
