import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Vehicle extends Rectangle {
    private double directionX;
    private double directionY;
    private boolean changed = false;
    private Constants.Direction direction;
    private Color color;
    private int x;
    private int y;

    public Constants.Direction getDirection() {
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

    public Vehicle(Point2D start, Constants.Direction direction) {
        super(start.getX(), start.getY(), Constants.VEHICLE_WIDTH, Constants.VEHICLE_HEIGHT);
        Color c = randomColor();
        setFill(c);
        this.color = c;
        setDirection(direction);
    }

    public void setDirection(Constants.Direction direction) {
        switch (direction) {
            case LEFT:
                this.directionX = -Constants.VEHICLE_SPEED;
                this.directionY = 0;
                break;
            case RIGHT:
                this.directionX = Constants.VEHICLE_SPEED;
                this.directionY = 0;
                break;
            case UP:
                this.directionX = 0;
                this.directionY = -Constants.VEHICLE_SPEED;
                break;
            case DOWN:
                this.directionX = 0;
                this.directionY = Constants.VEHICLE_SPEED;
                break;
        }
        this.direction = direction;
    }

    public void move(int index) {
        this.setX(this.getX() + (directionX));
        this.setY(this.getY() + (directionY));

        for (int i = 0; i < App.vehicles.size(); i++) {
            if (i == index)
                continue;

            Vehicle otherVehicle = App.vehicles.get(i);

            if (intersects(otherVehicle)) {
                this.setX(this.getX() - (Constants.VEHICLE_SPEED  * directionX));
                this.setY(this.getY() - (Constants.VEHICLE_SPEED  * directionY));
            }
        }
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

    public boolean intersects(Vehicle otherVehicle) {
        int size = 40;

        int aLeft = (int) this.getX();
        int aRight = (int) (this.getX() + size);
        int aTop = (int) this.getY();
        int aBottom = (int) (this.getY() + size);

        int bLeft = (int) otherVehicle.getX();
        int bRight = (int) (otherVehicle.getX() + size);
        int bTop = (int) otherVehicle.getY();
        int bBottom = (int) (otherVehicle.getY() + size);

        return !(aLeft >= bRight || aRight <= bLeft || aTop >= bBottom || aBottom <= bTop);
    }

}
