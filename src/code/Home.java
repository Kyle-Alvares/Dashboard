package code;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import java.util.Timer;
import java.util.TimerTask;
import javafx.geometry.Insets;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import java.io.PrintWriter;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class Home {

    // variables
    protected static LinkedList<String> quotes = new LinkedList<>();
    protected static String quotesFilePath = "src/files/quotes.txt";
    protected static ImageView[] backgrounds = new ImageView[Images.THEMES.length];
    protected static ImageView currentBG = new ImageView();
    protected static boolean dataLoaded = false;

    // home variables
    private static StackPane stackpane = new StackPane();
    private static BorderPane layout = new BorderPane();

    // theme picker variables
    private static Scene themeScene;
    protected static String themeFilePath = "src/files/themes.txt";
    protected static ImageView currentImg = new ImageView();
    private static StackPane sp;
    private static BorderPane bp = new BorderPane();
    private static String[] themeTitles = {"Forest", "City", "Bridge", "Mountain", "Anemone"};
    private static Text title;
    protected static int imageIndex = 0;

    public static StackPane home() {

        // load data
        if(!dataLoaded) {
            loadQuotes();
            loadBackgrounds();
            loadPreferences();
        }
        dataLoaded = true;

        // set background image
        currentBG = backgrounds[imageIndex];

        // pane settings for layout

        VBox topPane = new VBox();
        topPane.setPadding(new Insets(20,20,0,0));
        topPane.setAlignment(Pos.CENTER_RIGHT);

        VBox centerPane = new VBox();
        centerPane.setPadding(new Insets(0,0,0,0));

        VBox bottomPane = new VBox();
        bottomPane.setPadding(new Insets(0,0,20,0));
        bottomPane.setAlignment(Pos.CENTER);

        // text for time
        Text timeText = new Text();
        timeText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 80));
        timeText.setFill(Color.WHITE);

        // text for date
        Text dateText = new Text();
        dateText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 30));
        dateText.setFill(Styling.PALE_PURPLE_CLR);

        // text for message
        Text mssgText = new Text();
        mssgText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.THIN, 20));
        mssgText.setFill(Color.WHITE);

        // text for quote
        Text quoteText = new Text();
        quoteText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.EXTRA_LIGHT, 14));
        quoteText.setFill(Color.WHITE);
        quoteText.setText(Home.quotes.getFirst());

        // image used as button to access themes
        ImageView themes = new ImageView(Images.GALLERY_DEFAULT);
        themes.setOnMouseEntered(e -> themes.setImage(Images.GALLERY_ACTIVE));
        themes.setOnMouseExited(e -> themes.setImage(Images.GALLERY_DEFAULT));
        themes.setOnMouseClicked(e -> themeSelector());

        // timer used for time
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // variables
                Date date = new Date();
                SimpleDateFormat formatter;
                String mssgDisplay = "Good ";

                // use formatter for time
                formatter = new SimpleDateFormat("h:mm:ss a");
                String timeDisplay = formatter.format(date);
                if(timeDisplay.charAt(timeDisplay.length()-2)=='A') {
                    mssgDisplay += "Morning";
                } else {
                    if((Integer.parseInt(timeDisplay.charAt(0)+"")) < 6) {
                        mssgDisplay += "Afternoon";
                    } else {
                        mssgDisplay += "Evening";
                    }
                }

                // use formatter for date
                formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
                String dateDisplay = formatter.format(date);
                timeText.setText(timeDisplay.substring(0,timeDisplay.length()-3));
                dateText.setText(dateDisplay);
                mssgText.setText(mssgDisplay);
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

        topPane.getChildren().addAll(themes);

        centerPane.getChildren().addAll(timeText, dateText, mssgText);
        centerPane.setAlignment(Pos.CENTER);

        bottomPane.getChildren().addAll(quoteText);

        layout.setTop(topPane);
        layout.setCenter(centerPane);
        layout.setBottom(bottomPane);

        applyChanges();

        return stackpane;
    }

    protected static void loadQuotes() {
        try {
            // read from quotes file
            Scanner scanner = new Scanner(new File(quotesFilePath));
            String line;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                quotes.add(line);
            }
            scanner.close();

            // cycle quotes
            Home.quotes.add(Home.quotes.remove(0));

            // rewrite quotes file
            PrintWriter pw = new PrintWriter(new File(quotesFilePath));
            for(String q : quotes) {
                pw.println(q);
            }
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void loadBackgrounds() {
        for(int i=0; i < Images.THEMES.length; i++) {
            try {
                backgrounds[i] = new ImageView(new Image(Images.THEMES[i]));
            } catch (Exception p) {
                System.out.println(Images.THEMES[i]);
            }
        }
    }

    // selects alternate background on different stage
    protected static void themeSelector() {

        Stage window = new Stage();
        sp = new StackPane();

        sp.setStyle(Styling.BACKGROUND_STYLE);

        VBox centerPane = new VBox(10);
        centerPane.setAlignment(Pos.CENTER);

        // current viewed image on theme selector
        currentImg = new ImageView(new Image(Images.THEMES[imageIndex]));
        currentImg.setFitHeight(300);
        currentImg.setFitWidth(500);

        // apply changes button
        Button apply = new Button("Apply");
        apply.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        apply.setStyle(Styling.TEXT_BUTTON_STYLE);
        apply.setOnMouseClicked(e -> { applyChanges(); });

        // title of background
        title = new Text(themeTitles[imageIndex]);
        title.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 20));
        title.setFill(Color.WHITE);

        centerPane.getChildren().addAll(title,apply);
        bp.setCenter(centerPane);

        Pane leftPane = new Pane();

        // left button
        ImageView default_left = new ImageView(new Image(Images.NAVIGATION[0]));
        ImageView active_left = new ImageView(new Image(Images.NAVIGATION[1]));

        leftPane.getChildren().add(default_left);
        leftPane.setOnMouseEntered(e -> {
            leftPane.getChildren().clear();
            leftPane.getChildren().add(active_left);
        });
        leftPane.setOnMouseExited(e -> {
            leftPane.getChildren().clear();
            leftPane.getChildren().add(default_left);
        });
        leftPane.setOnMouseClicked(e -> { nextImage(false); });

        bp.setLeft(leftPane);

        // right button
        Pane rightPane = new Pane();

        ImageView default_right = new ImageView(new Image(Images.NAVIGATION[2]));
        ImageView active_right = new ImageView(new Image(Images.NAVIGATION[3]));

        rightPane.getChildren().add(default_right);
        rightPane.setOnMouseEntered(e -> {
            rightPane.getChildren().clear();
            rightPane.getChildren().add(active_right);
        });
        rightPane.setOnMouseExited(e -> {
            rightPane.getChildren().clear();
            rightPane.getChildren().add(default_right);
        });
        rightPane.setOnMouseClicked(e -> { nextImage(true); });

        bp.setRight(rightPane);

        sp.getChildren().clear();
        sp.getChildren().addAll(currentImg, bp);

        // stage settings
        window.setTitle("Theme Picker");
        window.getIcons().add(Images.ICON);
        window.setResizable(false);
        window.setScene(new Scene(sp,500,300, Styling.BACKGROUND_CLR));
        window.show();
    }

    // cycles to next image on theme selector - true (right), false (left)
    protected static void nextImage(boolean dir) {
        if(dir) {
            if(++imageIndex > Images.THEMES.length - 1) { imageIndex = 0; }
            currentImg = new ImageView(new Image(Images.THEMES[imageIndex]));
            currentImg.setFitHeight(300);
            currentImg.setFitWidth(500);
        } else {
            if(--imageIndex < 0) { imageIndex = Images.THEMES.length - 1; }
            currentImg = new ImageView(new Image(Images.THEMES[imageIndex]));
            currentImg.setFitHeight(300);
            currentImg.setFitWidth(500);
        }
        title.setText(themeTitles[imageIndex]);
        sp.getChildren().clear();
        sp.getChildren().addAll(currentImg, bp);
    }

    // apply background changes
    protected static void applyChanges() {
        try {
            PrintWriter pw = new PrintWriter(new File(themeFilePath)); // save preference in file
            pw.println(imageIndex);
            pw.close();
        } catch(Exception i) {}
        currentBG = backgrounds[imageIndex];
        stackpane.getChildren().clear();
        stackpane.getChildren().addAll(currentBG, layout);
    }

    // load background preference
    protected static void loadPreferences() {
        try {
            Scanner scanner = new Scanner(new File(themeFilePath));
            String line = "0";
            if(scanner.hasNextLine()) {
                line = scanner.nextLine();
            }
            scanner.close();
            imageIndex = Integer.parseInt(line);
        } catch(FileNotFoundException f) {
            try {
                File file = new File(themeFilePath);
                file.createNewFile();
            } catch (Exception c) {
                System.err.println("FAILED");
            }
        }
    }

}
