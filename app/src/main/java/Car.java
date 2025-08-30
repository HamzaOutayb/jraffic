import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Car {
    private Rectangle shape;
    private double speed = 15;
    private double directionX;
    private double directionY;
    private double width = 10;
    private double height = 10;

    public Car(Point2D start, Color color, double speed, double dirX, double dirY) {
        this.shape = new Rectangle(this.width, this.height, color);
        this.shape.setX(start.getX());
        this.shape.setY(start.getY());
        this.speed = speed;
        this.directionX = dirX;
        this.directionY = dirY;
    }

    public Rectangle getShape() { return shape; }
    
    public double getHeight() {
        return this.height;
    }

    public void move() {
        shape.setX(shape.getX() + speed * directionX);
        shape.setY(shape.getY() + speed * directionY);
    }
}
