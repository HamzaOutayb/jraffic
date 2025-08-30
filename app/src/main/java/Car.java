import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Car extends Rectangle {
    private double speed = 0.1;
    private double directionX;
    private double directionY;
    private boolean changed = false;
    private Direction direction;
    private Color color;

    public Car(Point2D start, Direction direction) {
        super(start.getX(), start.getY(), 40, 40);
        Color c = randomColor();
        setFill(c);
        this.color = c;
        setDirection(direction);
    }

    public void setDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                this.directionX = -1;
                this.directionY = 0;
                break;
            case RIGHT:
                this.directionX = 1;
                this.directionY = 0;
                break;
            case UP:
                this.directionX = 0;
                this.directionY = -1;
                break;
            case DOWN:
                this.directionX = 0;
                this.directionY = 1;
                break;
        }
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean getChanged() {
        return this.changed;
    }

    public void setChanged(boolean c) {
        this.changed = c;
    }

    public double getDirectionX() {
        return this.directionX;
    }

    public double getDirectionY() {
        return this.directionY;
    }

    public void move() {
        this.setX(this.getX() + (speed * directionX));
        this.setY(this.getY() + (speed * directionY));
    }

    private Color randomColor() {
        Random rand = new Random();
        int colorIndex = rand.nextInt(3);
        switch (colorIndex) {
            case 0:
                return Color.YELLOW;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            default:
                return Color.RED;
        }
    }
}
