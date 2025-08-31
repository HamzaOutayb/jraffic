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
        
        // Initialize traffic lights with corrected positions
        upLight = new Circle(400, 200, 12); // Before intersection
        downLight = new Circle(400, 400, 12); // Before intersection
        leftLight = new Circle(300, 300, 12); // Before intersection
        rightLight = new Circle(500, 300, 12); // Before intersection
        
        // Set black stroke for visibility
        upLight.setStroke(Color.BLACK);
        downLight.setStroke(Color.BLACK);
        leftLight.setStroke(Color.BLACK);
        rightLight.setStroke(Color.BLACK);
        
        // Initialize lights to a starting state (UP green)
        setLights(Direction.UP);
        currentGreenDirection = Direction.UP;
        updateLightColors();
        
        root.getChildren().addAll(horizontalRoad, verticalRoad, hLine, vLine, upLight, downLight, leftLight, rightLight);
        
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
                    map.put(newCar.getDirection(), map.getOrDefault(newCar.getDirection(), 0) + 1);
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
                    if (carCenterX >= 350 && carCenterX <= 450 && carCenterY >= 250 && carCenterY <= 350) {
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
                
                // Only change lights if intersection is empty
                if (centerIsEmpty && maxDirection != null && maxDirection != currentGreenDirection) {
                    setLights(maxDirection);
                    currentGreenDirection = maxDirection;
                    updateLightColors();
                }
                
                Iterator<Car> carIterator = cars.iterator();
                while (carIterator.hasNext()) {
                    Car car = carIterator.next();
                    if (car.getX() < -50 || car.getX() > 800 || car.getY() < -50 || car.getY() > 600) {
                        root.getChildren().remove(car);
                        carIterator.remove();
                        map.put(car.getDirection(), map.getOrDefault(car.getDirection(), 1) - 1);
                        continue;
                    }
                }
                
                int index = 0;
                for (Car car : cars) {
                    // Handle BLUE cars (turn left)
                    if (car.getColor() == Color.BLUE && car.getY() > 253 && car.getDirection() == Direction.DOWN
                            && !car.getChanged()) {
                        map.put(Direction.DOWN, map.getOrDefault(Direction.DOWN, 1) - 1);
                        car.setDirection(Direction.LEFT);
                        map.put(Direction.LEFT, map.getOrDefault(Direction.LEFT, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.BLUE && car.getY() < 300 && car.getDirection() == Direction.UP
                            && !car.getChanged()) {
                        map.put(Direction.UP, map.getOrDefault(Direction.UP, 1) - 1);
                        car.setDirection(Direction.RIGHT);
                        map.put(Direction.RIGHT, map.getOrDefault(Direction.RIGHT, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.BLUE && car.getX() > 356 && car.getDirection() == Direction.RIGHT
                            && !car.getChanged()) {
                        map.put(Direction.RIGHT, map.getOrDefault(Direction.RIGHT, 1) - 1);
                        car.setDirection(Direction.DOWN);
                        map.put(Direction.DOWN, map.getOrDefault(Direction.DOWN, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.BLUE && car.getX() < 405 && car.getDirection() == Direction.LEFT
                            && !car.getChanged()) {
                        map.put(Direction.LEFT, map.getOrDefault(Direction.LEFT, 1) - 1);
                        car.setDirection(Direction.UP);
                        map.put(Direction.UP, map.getOrDefault(Direction.UP, 0) + 1);
                        car.setChanged(true);
                    }
                    
                    // Handle RED cars (turn right)
                    if (car.getColor() == Color.RED && car.getY() > 306 && car.getDirection() == Direction.DOWN
                            && !car.getChanged()) {
                        map.put(Direction.DOWN, map.getOrDefault(Direction.DOWN, 1) - 1);
                        car.setDirection(Direction.RIGHT);
                        map.put(Direction.RIGHT, map.getOrDefault(Direction.RIGHT, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.RED && car.getY() < 262 && car.getDirection() == Direction.UP
                            && !car.getChanged()) {
                        map.put(Direction.UP, map.getOrDefault(Direction.UP, 1) - 1);
                        car.setDirection(Direction.LEFT);
                        map.put(Direction.LEFT, map.getOrDefault(Direction.LEFT, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.RED && car.getX() > 406 && car.getDirection() == Direction.RIGHT
                            && !car.getChanged()) {
                        map.put(Direction.RIGHT, map.getOrDefault(Direction.RIGHT, 1) - 1);
                        car.setDirection(Direction.UP);
                        map.put(Direction.UP, map.getOrDefault(Direction.UP, 0) + 1);
                        car.setChanged(true);
                    } else if (car.getColor() == Color.RED && car.getX() < 355 && car.getDirection() == Direction.LEFT
                            && !car.getChanged()) {
                        map.put(Direction.LEFT, map.getOrDefault(Direction.LEFT, 1) - 1);
                        car.setDirection(Direction.DOWN);
                        map.put(Direction.DOWN, map.getOrDefault(Direction.DOWN, 0) + 1);
                        car.setChanged(true);
                    }
                    
                    // Handle YELLOW cars (go straight) - no direction change needed
                    if (car.getColor() == Color.YELLOW && car.getY() > 253 && car.getDirection() == Direction.DOWN
                            && !car.getChanged()) {
                        car.setChanged(true);
                    } else if (car.getColor() == Color.YELLOW && car.getY() < 300 && car.getDirection() == Direction.UP
                            && !car.getChanged()) {
                        car.setChanged(true);
                    } else if (car.getColor() == Color.YELLOW && car.getX() > 356
                            && car.getDirection() == Direction.RIGHT
                            && !car.getChanged()) {
                        car.setChanged(true);
                    } else if (car.getColor() == Color.YELLOW && car.getX() < 405
                            && car.getDirection() == Direction.LEFT
                            && !car.getChanged()) {
                        car.setChanged(true);
                    }
                    
                    // Traffic light checks
                    if (car.getDirection() == Direction.DOWN && car.getY() > 215 && downl && !car.getChanged()) {
                        // Stop at red light
                    } else if (car.getDirection() == Direction.UP && car.getY() < 353 && upl && !car.getChanged()) {
                        // Stop at red light
                    } else if (car.getDirection() == Direction.RIGHT && car.getX() > 310 && rightl && !car.getChanged()) {
                        // Stop at red light
                    } else if (car.getDirection() == Direction.LEFT && car.getX() < 455 && leftl && !car.getChanged()) {
                        // Stop at red light
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
                this.upl = false; // Green for UP
                this.downl = true;  // Red for others
                this.leftl = true;
                this.rightl = true;
                break;
            case DOWN:
                this.upl = true;
                this.downl = false; // Green for DOWN
                this.leftl = true;
                this.rightl = true;
                break;
            case LEFT:
                this.upl = true;
                this.downl = true;
                this.leftl = false; // Green for LEFT
                this.rightl = true;
                break;
            case RIGHT:
                this.upl = true;
                this.downl = true;
                this.leftl = true;
                this.rightl = false; // Green for RIGHT
                break;
            default:
                break;
        }
    }
    
    private void updateLightColors() {
        // Fixed: Use correct variable for each light
        upLight.setFill(upl ? Color.RED : Color.GREEN);
        downLight.setFill(downl ? Color.RED : Color.GREEN);
        leftLight.setFill(leftl ? Color.RED : Color.GREEN);
        rightLight.setFill(rightl ? Color.RED : Color.GREEN);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}