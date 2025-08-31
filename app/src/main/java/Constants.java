import javafx.scene.paint.Color;
public class Constants {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final int VEHICLE_WIDTH  = 40;
    public static final int VEHICLE_HEIGHT = 40;
    public static final int WINDOW_WIDTH   = 800;
    public static final int WINDOW_HEIGHT  = 600;
    public static final int STOP_BOX_MULTIPLIER = 2;
    public static final int STOP_BOX_WIDTH  = VEHICLE_WIDTH * STOP_BOX_MULTIPLIER;
    public static final int VEHICLE_SPEED = 1;
    public static final int DELAY_BETWEEN_VEHICLES = 50 * (VEHICLE_WIDTH / VEHICLE_SPEED);
    public static final int LANE_HEIGHT = VEHICLE_HEIGHT + 10;
    public static final Color COLOR_RED    = Color.RED;
    public static final Color COLOR_YELLOW = Color.YELLOW;
    public static final Color COLOR_GREEN  = Color.GREEN;
    public static final Color ROAD_COLOR   = Color.GRAY;
    public static final Color DIVIDER_COLOR = Color.WHITE;
}
