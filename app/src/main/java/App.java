import javafx.geometry.Point2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Pane carLayer = new Pane();

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

        // Add everything to root
        root.getChildren().addAll(horizontalRoad, verticalRoad, hLine, vLine, carLayer);

        Scene scene = new Scene(root, 800, 600, Color.DARKGREEN);
        List<Car> cars = new ArrayList<>();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> cars.add(new Car(new Point2D(760, 270), Color.RED, 5, -1, 0));
                case RIGHT -> cars.add(new Car(new Point2D(0, 320), Color.BLUE, 5, 1, 0)); 
                case UP -> cars.add(new Car(
                        new Point2D(420, 560), Color.GREEN, 5, 0, -1));
                case DOWN -> cars.add(new Car(
                        new Point2D(370, 0), Color.YELLOW, 5, 0, 1));
                case R -> System.out.println("R key pressed");
                case ESCAPE -> System.out.println("ESC key pressed");
                default -> {
                }
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            carLayer.getChildren().clear();

            for (Car car : cars) {
                car.move(); // update position
                carLayer.getChildren().add(car.getShape()); // draw car
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setTitle("Crossroad Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
