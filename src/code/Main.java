package code;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {

    // variables
    BorderPane layout = new BorderPane(); // main layout
    Pane menu = new Pane(); // menu layout
    final static int tabCount = Tab.tabNames.length;
    final static int windowWidth = 750;
    final static int windowHeight = Tab.tabHeight * tabCount;
    Rectangle[] inactiveTab = new Rectangle[tabCount];
    Rectangle[] activeTab = new Rectangle[tabCount];
    Text[] tabTitles = new Text[tabCount];
    ImageView[] tabIcons = new ImageView[tabCount];
    EventHandler[] menuInactiveHandler = new EventHandler[tabCount];
    EventHandler[] menuActiveHandler = new EventHandler[tabCount];
    EventHandler[] menuClickedHandler = new EventHandler[tabCount];
    Rectangle side = new Rectangle(Tab.tabWidth,windowHeight + 1000, Styling.DEFAULT_TAB_CLR);
    Scene scene = new Scene(layout,windowWidth,windowHeight, Styling.BACKGROUND_CLR);

    @Override
    public void start(Stage primaryStage) throws Exception {
        menu.getChildren().add(side);

        // initialize side menu items
        for(int i=0; i < tabCount; i++) {
            // initialize tabs
            inactiveTab[i] = Tab.tab(0, Tab.tabHeight * i, Styling.DEFAULT_TAB_CLR);
            activeTab[i] = Tab.tab(0, Tab.tabHeight * i, Styling.ACTIVE_TAB_CLR);

            // initialize tab icons
            tabIcons[i] = new ImageView(new Image(Images.TABS[i]));
            tabIcons[i].setX(10);
            tabIcons[i].setY(i * Tab.tabHeight + 10);

            // initialize tab icons
            tabTitles[i] = new Text(Tab.tabNames[i]);
            tabTitles[i].setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
            tabTitles[i].setX(50);
            tabTitles[i].setY(i * Tab.tabHeight + 32);
            tabTitles[i].setFill(Color.WHITE);
            menu.getChildren().addAll(inactiveTab[i], tabIcons[i], tabTitles[i]);
        }

        // inital layout
        layout.setLeft(menu);
        layout.setCenter(Home.home());
        layout.setStyle(Styling.BACKGROUND_STYLE);

        // load to-do items in list
        ToDo.loadData();
        Contacts.loadContacts();
        VoiceMemo.loadMemos();

        // add events to tabs
        for(int i=0; i < tabCount; i++) {
            final int k = i;

            menuInactiveHandler[k] = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menu.getChildren().removeAll(inactiveTab[k], tabIcons[k], tabTitles[k]);
                    menu.getChildren().addAll(activeTab[k], tabIcons[k], tabTitles[k]);
                }
            };

            menuActiveHandler[k] = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menu.getChildren().removeAll(activeTab[k], tabIcons[k], tabTitles[k]);
                    menu.getChildren().addAll(inactiveTab[k], tabIcons[k], tabTitles[k]);
                }
            };

            menuClickedHandler[k] = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(k == 0) {
                        layout.setCenter(Home.home());
                    } else if(k == 1) {
                        layout.setCenter(ToDo.todo());
                    } else if(k == 2) {
                        layout.setCenter(Contacts.contacts());
                    } else if(k == 3) {
                        layout.setCenter(VoiceMemo.voiceMemo());
                    } else if(k == 4) {
                        layout.setCenter(Password.password());
                    } else if(k == 5) {
                        layout.setCenter(Mail.mail());
                    } else if(k == 6) {
                        layout.setCenter(Help.help());
                    }
                }
            };

            inactiveTab[i].addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,menuInactiveHandler[k]);
            activeTab[i].addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,menuActiveHandler[k]);
            activeTab[i].addEventHandler(MouseEvent.MOUSE_CLICKED,menuClickedHandler[k]);

        }

        // main stage settings
        primaryStage.setTitle("Dashboard");
        primaryStage.getIcons().add(Images.ICON);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            ToDo.saveOnClose();
            Contacts.saveOnClose();
            VoiceMemo.saveOnClose();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
