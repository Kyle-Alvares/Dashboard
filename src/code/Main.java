package code;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {

    BorderPane pane; // main layout
    final int tabCount = Tab.tabNames.length;
    Rectangle[] inactiveTab = new Rectangle[tabCount];
    Rectangle[] activeTab = new Rectangle[tabCount];
    Text[] tabTitles = new Text[tabCount];
    ImageView[] tabIcons = new ImageView[tabCount];
    EventHandler[] menuInactiveHandler = new EventHandler[tabCount];
    EventHandler[] menuActiveHandler = new EventHandler[tabCount];

    @Override
    public void start(Stage primaryStage) throws Exception {
        pane = new BorderPane(); // main layout

        // initialize side menu items
        for(int i=0; i < tabCount; i++) {
            inactiveTab[i] = Tab.tab(0, Tab.tabHeight * i, Colors.DEFAULT_TAB_CLR);
            activeTab[i] = Tab.tab(0, Tab.tabHeight * i, Colors.ACTIVE_TAB_CLR);

            tabIcons[i] = new ImageView(new Image(Images.TABS[i]));
            tabIcons[i].setX(10);
            tabIcons[i].setY(i * Tab.tabHeight + 10);

            tabTitles[i] = new Text(Tab.tabNames[i]);
            tabTitles[i].setFont(Font.font("Symbol", FontWeight.NORMAL, 15));
            tabTitles[i].setX(50);
            tabTitles[i].setY(i * Tab.tabHeight + 32);
            tabTitles[i].setFill(Color.WHITE);
            pane.getChildren().addAll(inactiveTab[i], tabIcons[i], tabTitles[i]);
        }

        for(int i=0; i < tabCount; i++) {
            final int k = i;
            menuInactiveHandler[k] = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pane.getChildren().removeAll(inactiveTab[k], tabIcons[k], tabTitles[k]);
                    pane.getChildren().addAll(activeTab[k], tabIcons[k], tabTitles[k]);
                }
            };

            menuActiveHandler[k] = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pane.getChildren().removeAll(activeTab[k], tabIcons[k], tabTitles[k]);
                    pane.getChildren().addAll(inactiveTab[k], tabIcons[k], tabTitles[k]);
                }
            };

            inactiveTab[i].addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,menuInactiveHandler[k]);
            activeTab[i].addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,menuActiveHandler[k]);

        }

        // main stage settings
        primaryStage.setTitle("Dashboard");
        primaryStage.getIcons().add(Images.ICON);
        primaryStage.setScene(new Scene(pane,600,Tab.tabHeight * tabCount, Colors.BACKGROUND_CLR));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
