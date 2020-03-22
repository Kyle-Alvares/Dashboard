package code;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tab {

    public static final int tabHeight = 50;
    public static final int tabWidth = 180;
    public static final String[] tabNames = {"Home", "To Do", "Contacts", "Voice Memos", "Password", "Mail", "Help"};

    public static Rectangle tab(double x, double y, Color color) {
        Rectangle rect = new Rectangle();
        rect.setX(x);
        rect.setY(y);
        rect.setHeight(tabHeight);
        rect.setWidth(tabWidth);
        rect.setFill(color);
        rect.setStroke(Styling.DEFAULT_TAB_CLR);
        return rect;
    }

}
