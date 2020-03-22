package code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Random;

public class Password {

    private static String path = "src/vid/liquid-loader.mp4";
    private static String password;
    private static TextField display = new TextField();

    private static Media media = new Media(new File(path).toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(media);
    private static MediaView mediaView = new MediaView(mediaPlayer);

    public static StackPane password() {
        StackPane layers = new StackPane();

        display.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        display.setMaxWidth(250);
        display.setAlignment(Pos.CENTER);
        display.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 16));

        //  media player settings
        mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        BorderPane layout = new BorderPane();

        HBox topPane = new HBox(5);
        topPane.setAlignment(Pos.CENTER_LEFT);
        topPane.setPadding(new Insets(13,0,5,20));

        ImageView key = new ImageView(Images.KEY);

        Text titleText = new Text();
        titleText.setText("Password Generator");
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 19));

        topPane.getChildren().addAll(key, titleText);

        VBox bottomPane = new VBox(5);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(0,0,20,0));

        Button generate = new Button("Generate");
        generate.setStyle(Styling.TEXT_BUTTON_STYLE);
        generate.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        generate.setOnMouseClicked(e -> generatePassword());
        generatePassword();

        bottomPane.getChildren().addAll(display, generate);
        layout.setBottom(bottomPane);
        layout.setTop(topPane);
        layers.getChildren().addAll(mediaView, layout);

        return layers;
    }


    private static class LowerCase implements Runnable{

        private int time;

        public LowerCase (int time){
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
                Random random = new Random();
                char character = (char) (random.nextInt(26) + 'a');
                Password.password += character + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class UpperCase implements Runnable{

        private int time;

        public UpperCase (int time){
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
                Random random = new Random();
                char character = (char) ((random.nextInt(26) + 'A'));
                Password.password += character + "";
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class SpecialCase implements Runnable{

        private int time;
        private String characters = "!@#$%^&*()_+-?";

        public SpecialCase (int time){
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
                Random random = new Random();
                char character = characters.charAt(random.nextInt(characters.length()));
                Password.password += character + "";
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void generatePassword() {

        Password.password = "";

        int length = 16;
        Runnable[] runners = new Runnable[length];
        Thread[] thread = new Thread[length];
        Random random = new Random();
        int time;

        for(int i=0; i < length / 2; i++) {
            time = random.nextInt(100);
            runners[i] = new LowerCase(time);

            if(time % 3 != 0) { // probability of upper case
                runners[i+(length/2)] = new UpperCase(time);
            } else {
                runners[i+(length/2)] = new SpecialCase(time);
            }

            thread[i] = new Thread(runners[i]);
            thread[i+(length/2)] = new Thread(runners[i+(length/2)]);

            thread[i].run();
            thread[i+(length/2)].run();
        }

        display.setText(Password.password);
    }

}
