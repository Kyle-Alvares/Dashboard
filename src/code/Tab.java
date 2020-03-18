package code;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tab {

    public static final int tabHeight = 50;
    public static final int tabWidth = 180;
    public static final String[] tabNames = {"Home", "ToDo", "Contacts", "Calculator", "Voice Memos", "Shortcuts"};

    public static Rectangle tab(double x, double y, Color color) {
        Rectangle rect = new Rectangle();
        rect.setX(x);
        rect.setY(y);
        rect.setHeight(tabHeight);
        rect.setWidth(tabWidth);
        rect.setFill(color);
        rect.setStroke(Colors.DEFAULT_TAB_CLR);
        return rect;
    }

}
