import javafx.geometry.Point2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;
import javafx.animation.AnimationTimer;

public class App extends Application {
    public static List<Car> cars = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        /// craeting the road;
        /// with two rectangles and two lines;
        Rectangle horizontalRoad = new Rectangle(0, 250, 800, 100); // x, y, width, height
        horizontalRoad.setFill(Color.DARKGRAY);

        Rectangle verticalRoad = new Rectangle(350, 0, 100, 600);
        verticalRoad.setFill(Color.DARKGRAY);

        Line hLine = new Line(0, 300, 800, 300);
        hLine.setStroke(Color.WHITE);
        hLine.setStrokeWidth(2);

        Line vLine = new Line(400, 0, 400, 600);
        vLine.setStroke(Color.WHITE);
        vLine.setStrokeWidth(2);

        root.getChildren().addAll(horizontalRoad, verticalRoad, hLine, vLine);

        Scene scene = new Scene(root, 800, 600, Color.DARKGREEN);

        scene.setOnKeyPressed(event -> {
            Car newCar = null;

            switch (event.getCode()) {
                case LEFT:
                    newCar = new Car(new Point2D(760, 255), Direction.LEFT);
                    break;
                case RIGHT:
                    newCar = new Car(new Point2D(0, 305), Direction.RIGHT);
                    break;
                case UP:
                    newCar = new Car(new Point2D(405, 560), Direction.UP);
                    break;
                case DOWN:
                    newCar = new Car(new Point2D(360, 0), Direction.DOWN);
                    break;
                case R:
                    System.out.println("R key pressed");
                    break;
                case ESCAPE:
                    System.out.println("ESC key pressed");
                    break;
                default:
                    break;
            }

            if (newCar != null) {
                boolean collisionDetected = false;
                for (Car car : App.cars) {
                    if (newCar.intersects(car)) {
                        collisionDetected = true;
                        break;
                    }
                }

                if (!collisionDetected) {
                    App.cars.add(newCar);
                    root.getChildren().add(newCar); // Add the car to the pan
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println(String.format("arrey list that holds cars size %d", cars.size()));
                Iterator<Car> carIterator = cars.iterator();
                while (carIterator.hasNext()) {
                    Car car = carIterator.next();

                    if (car.getX() < -50 || car.getX() > 800 || car.getY() < -50 || car.getY() > 600) {
                        root.getChildren().remove(car);
                        carIterator.remove();
                        continue;
                    }
                }
                int index = 0;
                for (Car car : cars) {
                    if (car.getColor() == Color.BLUE && car.getY() > 253 && car.getDirection() == Direction.DOWN
                            && !car.getChanged()) {
                        car.setDirection(Direction.LEFT);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.BLUE && car.getY() < 300 && car.getDirection() == Direction.UP
                            && !car.getChanged()) {
                        car.setDirection(Direction.RIGHT);
                        car.setChanged(true);

                    } else if (car.getColor() == Color.BLUE && car.getX() > 356 && car.getDirection() == Direction.RIGHT
                            && !car.getChanged()) {
                        car.setDirection(Direction.DOWN);
                        car.setChanged(true);

                    } else if (car.getColor() == Color.BLUE && car.getX() < 405 && car.getDirection() == Direction.LEFT
                            && !car.getChanged()) {
                        car.setDirection(Direction.UP);
                        car.setChanged(true);

                    }

                    if (car.getColor() == Color.RED && car.getY() > 306 && car.getDirection() == Direction.DOWN
                            && !car.getChanged()) {
                        car.setDirection(Direction.RIGHT);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.RED && car.getY() < 300 && car.getDirection() == Direction.UP
                            && !car.getChanged()) {
                        car.setDirection(Direction.LEFT);
                        car.setChanged(true);

                    } else if (car.getColor() == Color.RED && car.getX() > 406 && car.getDirection() == Direction.RIGHT
                            && !car.getChanged()) {
                        car.setDirection(Direction.UP);
                        car.setChanged(true);

                    } else if (car.getColor() == Color.RED && car.getX() < 355 && car.getDirection() == Direction.LEFT
                            && !car.getChanged()) {
                        car.setDirection(Direction.DOWN);
                        car.setChanged(true);

                    }

                    car.move(index);
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
