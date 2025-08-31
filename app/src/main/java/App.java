import javafx.geometry.Point2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;
import javafx.animation.AnimationTimer;

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

public class App extends Application {
    public static List<Car> cars = new ArrayList<>();
    public static HashMap<Direction, Integer> map = new HashMap<>();
    public boolean upl = true;
    public boolean downl = true;
    public boolean rightl = true;
    public boolean leftl = true;
    public boolean centerIsEmpty = true;

    private Direction currentGreenDirection = null;
    private Circle upLight, downLight, leftLight, rightLight;

    // --- Delay management ---
    private long lastSwitchTime = 0;
    private long delayDuration = 2_000_000_000L; // 2 seconds in nanoseconds
    private Direction pendingGreen = null;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Creating the road
        Rectangle horizontalRoad = new Rectangle(0, 250, 800, 100);
        horizontalRoad.setFill(Color.DARKGRAY);
        Rectangle verticalRoad = new Rectangle(350, 0, 100, 600);
        verticalRoad.setFill(Color.DARKGRAY);
        Line hLine = new Line(0, 300, 800, 300);
        hLine.setStroke(Color.WHITE);
        hLine.setStrokeWidth(2);
        Line vLine = new Line(400, 0, 400, 600);
        vLine.setStroke(Color.WHITE);
        vLine.setStrokeWidth(2);

        // Initialize traffic lights
        upLight = new Circle(400, 200, 12);
        downLight = new Circle(400, 400, 12);
        leftLight = new Circle(300, 300, 12);
        rightLight = new Circle(500, 300, 12);

        upLight.setStroke(Color.BLACK);
        downLight.setStroke(Color.BLACK);
        leftLight.setStroke(Color.BLACK);
        rightLight.setStroke(Color.BLACK);

        // Start with UP green
        setLights(Direction.UP);
        currentGreenDirection = Direction.UP;
        updateLightColors();

        root.getChildren().addAll(horizontalRoad, verticalRoad, hLine, vLine,
                upLight, downLight, leftLight, rightLight);

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
                case ESCAPE:
                    System.exit(0);
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
                    map.put(newCar.getDirection(),
                            map.getOrDefault(newCar.getDirection(), 0) + 1);
                    root.getChildren().add(newCar);
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Check if intersection is empty
                centerIsEmpty = true;
                for (Car car : cars) {
                    double carCenterX = car.getX() + car.getWidth() / 2;
                    double carCenterY = car.getY() + car.getHeight() / 2;
                    if (carCenterX >= 350 && carCenterX <= 450
                            && carCenterY >= 250 && carCenterY <= 350) {
                        centerIsEmpty = false;
                        break;
                    }
                }

                // Find direction with most cars
                Direction maxDirection = null;
                int maxCars = Integer.MIN_VALUE;
                for (Map.Entry<Direction, Integer> entry : map.entrySet()) {
                    if (entry.getValue() > maxCars) {
                        maxCars = entry.getValue();
                        maxDirection = entry.getKey();
                    }
                }

                // Light switching with delay
                if (centerIsEmpty && maxDirection != null
                        && maxDirection != currentGreenDirection) {
                    if (pendingGreen == null) {
                        // Step 1: turn all red
                        upl = downl = leftl = rightl = true;
                        updateLightColors();

                        // Step 2: schedule new green after delay
                        pendingGreen = maxDirection;
                        lastSwitchTime = now;
                    }
                }

                // If waiting, check if delay passed
                if (pendingGreen != null) {
                    if (now - lastSwitchTime >= delayDuration) {
                        setLights(pendingGreen);
                        currentGreenDirection = pendingGreen;
                        updateLightColors();
                        pendingGreen = null;
                    }
                }

                // Remove cars that are out of bounds
                Iterator<Car> carIterator = cars.iterator();
                while (carIterator.hasNext()) {
                    Car car = carIterator.next();
                    if (car.getX() < -50 || car.getX() > 800 || car.getY() < -50 || car.getY() > 600) {
                        root.getChildren().remove(car);
                        carIterator.remove();
                        map.put(car.getDirection(),
                                map.getOrDefault(car.getDirection(), 1) - 1);
                        continue;
                    }
                }

                // Move cars
                int index = 0;
                for (Car car : cars) {
                    if (car.getDirection() == Direction.DOWN && car.getY() > 215 && downl && !car.getChanged()) {
                        // stop at red
                    } else if (car.getDirection() == Direction.UP && car.getY() < 353 && upl && !car.getChanged()) {
                        // stop at red
                    } else if (car.getDirection() == Direction.RIGHT && car.getX() > 310 && rightl && !car.getChanged()) {
                        // stop at red
                    } else if (car.getDirection() == Direction.LEFT && car.getX() < 455 && leftl && !car.getChanged()) {
                        // stop at red
                    } else {
                        car.move(index);
                    }
                    index++;
                }
            }
        };
        timer.start();

        primaryStage.setTitle("Crossroad Example with Traffic Lights");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setLights(Direction d) {
        switch (d) {
            case UP:
                upl = false; downl = true; leftl = true; rightl = true;
                break;
            case DOWN:
                upl = true; downl = false; leftl = true; rightl = true;
                break;
            case LEFT:
                upl = true; downl = true; leftl = false; rightl = true;
                break;
            case RIGHT:
                upl = true; downl = true; leftl = true; rightl = false;
                break;
        }
    }

    private void updateLightColors() {
        upLight.setFill(upl ? Color.RED : Color.GREEN);
        downLight.setFill(downl ? Color.RED : Color.GREEN);
        leftLight.setFill(leftl ? Color.RED : Color.GREEN);
        rightLight.setFill(rightl ? Color.RED : Color.GREEN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
