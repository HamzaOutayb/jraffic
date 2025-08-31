import javafx.geometry.Point2D;
import javafx.application.Application;
import javafx.scene.Scene;
// import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;
import javafx.animation.AnimationTimer;

public class App extends Application {
    public static List<Vehicle> vehicles = new ArrayList<>();

    public void intplan(Pane root) {

        // with two rectangles and two lines;
        Rectangle horizontalRoad = new Rectangle(0, 250, 800, 100);
        horizontalRoad.setFill(Color.DARKGRAY);

        Rectangle verticalRoad = new Rectangle(350, 0, 100, 800);
        verticalRoad.setFill(Color.DARKGRAY);

        Line hLine = new Line(0, 300, 800, 300);
        hLine.setStroke(Color.WHITE);
        hLine.setStrokeWidth(2);

        Line vLine = new Line(400, 0, 400, 600);
        vLine.setStroke(Color.WHITE);
        vLine.setStrokeWidth(2);
        root.getChildren().addAll(horizontalRoad, verticalRoad, hLine, vLine);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        intplan(root);

        scene.setOnKeyPressed(event -> {
            Vehicle newVehicle = null;

            switch (event.getCode()) {
                case LEFT:
                    System.out.println(vehicles.size());
                    newVehicle = new Vehicle(new Point2D(760+Constants.VEHICLE_HEIGHT, 255), Constants.Direction.LEFT);
                    break;
                case RIGHT:
                    newVehicle = new Vehicle(new Point2D(-Constants.VEHICLE_HEIGHT, 305), Constants.Direction.RIGHT);
                    break;
                case UP:
                    newVehicle = new Vehicle(new Point2D(405, 560+Constants.VEHICLE_HEIGHT), Constants.Direction.UP);
                    break;
                case DOWN:
                    newVehicle = new Vehicle(new Point2D(360, -Constants.VEHICLE_HEIGHT), Constants.Direction.DOWN);
                    break;
                case R:
                    System.out.println("R key pressed");
                    break;
                case ESCAPE:
                    System.out.println("ESC key pressed");
                    System.exit(0);
                    break;
                default:
                    break;
            }

            if (newVehicle != null) {
                boolean collisionDetected = false;
                for (Vehicle vehicle : App.vehicles) {
                    if (newVehicle.intersects(vehicle)) {
                        collisionDetected = true;
                        break;
                    }
                }

                if (!collisionDetected) {
                    App.vehicles.add(newVehicle);
                    root.getChildren().add(newVehicle); // Add the vehicle to the pan
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println(String.format("arrey list that holds vehicles size %d", vehicles.size()));
                Iterator<Vehicle> vehicleIterator = vehicles.iterator();
                while (vehicleIterator.hasNext()) {
                    Vehicle vehicle = vehicleIterator.next();

                    if (vehicle.getX() < -50 || vehicle.getX() > 800 || vehicle.getY() < -50 || vehicle.getY() > 600) {
                        root.getChildren().remove(vehicle);
                        vehicleIterator.remove();
                        continue;
                    }
                }
                int index = 0;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getColor() == Color.BLUE && vehicle.getY() > 253
                            && vehicle.getDirection() == Constants.Direction.DOWN
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.LEFT);
                        vehicle.setChanged(true);
                    } else if (vehicle.getColor() == Color.BLUE && vehicle.getY() < 300
                            && vehicle.getDirection() == Constants.Direction.UP
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.RIGHT);
                        vehicle.setChanged(true);

                    } else if (vehicle.getColor() == Color.BLUE && vehicle.getX() > 356
                            && vehicle.getDirection() == Constants.Direction.RIGHT
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.DOWN);
                        vehicle.setChanged(true);

                    } else if (vehicle.getColor() == Color.BLUE && vehicle.getX() < 405
                            && vehicle.getDirection() == Constants.Direction.LEFT
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.UP);
                        vehicle.setChanged(true);

                    }

                    if (vehicle.getColor() == Color.RED && vehicle.getY() > 306
                            && vehicle.getDirection() == Constants.Direction.DOWN
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.RIGHT);
                        vehicle.setChanged(true);
                    } else if (vehicle.getColor() == Color.RED && vehicle.getY() < 300
                            && vehicle.getDirection() == Constants.Direction.UP
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.LEFT);
                        vehicle.setChanged(true);

                    } else if (vehicle.getColor() == Color.RED && vehicle.getX() > 406
                            && vehicle.getDirection() == Constants.Direction.RIGHT
                            && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.UP);
                        vehicle.setChanged(true);

                    } else if (vehicle.getColor() == Color.RED && vehicle.getX() < 355 && vehicle.getDirection() == Constants.Direction.LEFT && !vehicle.getChanged()) {
                        vehicle.setDirection(Constants.Direction.DOWN);
                        vehicle.setChanged(true);

                    }

                    vehicle.move(index);
                    index++;
                }
            }
        };

        timer.start();
        primaryStage.setTitle("Crossroad Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
