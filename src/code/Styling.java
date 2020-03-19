package code;

import javafx.scene.paint.Color;

public class Styling {

    public static final Color BACKGROUND_CLR = Color.web("#1D1D3B");

    public static final String BACKGROUND_STYLE = "-fx-background-color: #1D1D3B;";

    public static final Color DEFAULT_TAB_CLR = Color.web("#121212");

    public static final Color ACTIVE_TAB_CLR = Color.web("#212121");

    public static final String TEXTFIELD_STYLE =
            "-fx-text-fill: white;" +
            "-fx-background-radius: 0 ;" +
            "-fx-background-color: #1D1D3B , white , #2A2854;" +
            "-fx-background-insets: -2 -2 0 -2, 0 2 -1 2, 0 0 0 0;" +
            "-fx-border-color: #1D1D3B";

    public static final String TEXT_BUTTON_STYLE =
            "-fx-background-radius: 3;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 10 6 10;" +
            "-fx-background-color: #1C1339";

    public static final String TEXT_BUTTON_CLICK_STYLE =
            "-fx-background-radius: 0;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 10 5 10;" +
            "-fx-background-color: #0C0A1F";

    public static final String LABEL_STYLE =
            "-fx-background-color: #05122D;" +
            "-fx-text-fill: white";

    public static final String SCROLL_STYLE =
            "-fx-background-color: #1C1339;" +
            "-fx-background: #1C1339";

    public static final String LIST_STYLE =
            "-fx-background-color: #1D1D3B";
}