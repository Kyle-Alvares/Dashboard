package code;

import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.PrintWriter;

public class VoiceMemo {

    private static BorderPane mainLayout;
    private static VBox mainTop;
    private static ScrollPane scroll = new ScrollPane();
    private static VBox items = new VBox(2);
    private static LinkedList<String> recordings = new LinkedList<>();
    protected static String voiceMemosFilePath = "src/files/voice-memos.txt";

    private static AudioFormat format;
    private static DataLine.Info info;
    private static TargetDataLine targetDataLine;
    private static boolean isRecording = false;
    private static Thread stopper;

    public static BorderPane voiceMemo() {

        // sound settings
        format = new AudioFormat(16000, 8, 2, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);

        if(!AudioSystem.isLineSupported(info)) {
            System.out.println("Line is not supported");
        }

        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        } catch (Exception tdl) {}

        // main layout
        mainLayout = new BorderPane();

        // top pane
        mainTop = new VBox();
        mainTop.setStyle(Styling.SCROLL_STYLE);
        mainTop.setPadding(new Insets(15,10,0,10));

        ImageView recordIMG = new ImageView(Images.NEW_RECORDING_DEFAULT);
        recordIMG.setOnMouseEntered(e -> recordIMG.setImage(Images.NEW_RECORDING_ACTIVE));
        recordIMG.setOnMouseExited(e -> recordIMG.setImage(Images.NEW_RECORDING_DEFAULT));
        recordIMG.setOnMouseClicked(e -> newRecording());

        mainTop.getChildren().add(recordIMG);

        items.setPadding(new Insets(0,10,10,10));

        scroll.setPrefSize(550, 150);
        scroll.setContent(items);
        scroll.setStyle(Styling.SCROLL_STYLE);

        mainLayout.setTop(mainTop);
        mainLayout.setBottom(scroll);
        return mainLayout;

    }

    private static void newRecording() {
        Stage window = new Stage();

        BorderPane newRecordingLayout = new BorderPane();
        newRecordingLayout.setPadding(new Insets(20,20,20,20));
        newRecordingLayout.setStyle(Styling.BACKGROUND_STYLE);

        VBox centerPane = new VBox(10);
        centerPane.setAlignment(Pos.CENTER_LEFT);

        Label prompt = new Label("Filename");
        prompt.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 22));
        prompt.setStyle(Styling.LABEL_CONTACT_STYLE);

        TextField filename = new TextField();
        filename.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        filename.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);

        HBox bottomPane = new HBox();
        bottomPane.setStyle(Styling.BACKGROUND_STYLE);
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        bottomPane.setPadding(new Insets(0,20,20,20));

        Button ok = new Button("Ok");
        ok.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        ok.setStyle(Styling.TEXT_BUTTON_STYLE);
        ok.setOnMouseClicked(e -> {
            String path = filename.getText();
            if(path.charAt(0) != ' ' || path.charAt(0) != '\n') {
                window.close();
                recordSound(path);
            }
        });

        centerPane.getChildren().addAll(prompt, filename);
        bottomPane.getChildren().addAll(ok);
        newRecordingLayout.setCenter(centerPane);
        newRecordingLayout.setBottom(bottomPane);

        window.setScene(new Scene(newRecordingLayout));
        window.setTitle("Filename");
        window.getIcons().add(Images.ICON);
        window.setResizable(false);
        window.show();
    }

    private static void recordSound(String path) {
        final String url = "src/snd/" + path + ".wav";

        BorderPane pane = new BorderPane();
        pane.setStyle(Styling.BACKGROUND_STYLE);
        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);

        Text display = new Text();
        display.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 26));
        display.setFill(Color.WHITE);
        display.setText("RECORDING");
        center.getChildren().add(display);
        pane.setCenter(center);

        Stage recordingWindow = new Stage();
        recordingWindow.setScene(new Scene(pane, 200,100));
        recordingWindow.setTitle("Recording");
        recordingWindow.setResizable(false);
        recordingWindow.getIcons().add(Images.ICON);
        recordingWindow.show();



        try {
            targetDataLine.open();
            targetDataLine.start();
        } catch(Exception e) {
            isRecording = false;
        }
        stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AudioInputStream audioStream = new AudioInputStream(targetDataLine);
                    File file = new File(url);
                    recordings.add(url);
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, file);
                } catch (Exception r) {

                }
            }
        });
        try {
            stopper.start();
            stopper.sleep(5000);
        } catch (Exception t) {}
        targetDataLine.stop();
        targetDataLine.close();
        updateItems();

        recordingWindow.close();
    }

    private static HBox makeItem(String path) {
        ImageView recordingDelete = new ImageView(Images.DELETE_CONTACT_DEFAULT);
        recordingDelete.setOnMouseEntered(c -> recordingDelete.setImage(Images.DELETE_CONTACT_ACTIVE));
        recordingDelete.setOnMouseExited(c -> recordingDelete.setImage(Images.DELETE_CONTACT_DEFAULT));
        recordingDelete.setOnMouseClicked(c -> {
            try {
                File cur = new File(path);
                cur.delete();
            } catch (Exception e) { }
            for(int i=0; i < recordings.size(); i++) {
                if(recordings.get(i).equals(path)){
                    recordings.remove(i);
                    break;
                }
            }
            updateItems();
        });

        ImageView playButton = new ImageView(Images.PLAY_DEFAULT);
        playButton.setOnMouseEntered(c -> playButton.setImage(Images.PLAY_ACTIVE));
        playButton.setOnMouseExited(c -> playButton.setImage(Images.PLAY_DEFAULT));
        playButton.setOnMouseClicked(c -> {
            playSound(path);
        });

        Label newItem = new Label("   " + path.substring(8, path.length()-4));
        newItem.setStyle(Styling.VOICE_ITEM_LABEL_STYLE);
        newItem.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        newItem.setAlignment(Pos.CENTER_LEFT);
        newItem.setPadding(new Insets(5,0,5,0));
        newItem.setMinWidth(500);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(recordingDelete, playButton, newItem);
        return hbox;
    }

    private static void updateItems() {
        items.getChildren().clear();
        for(String s : recordings) {
            HBox current = makeItem(s);
            items.getChildren().add(current);
        }
    }

    private static void playSound(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(path)));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1000);
        } catch(Exception s) {

        }
    }

    protected static void loadMemos() {
        try {
            Scanner scanner = new Scanner(new File(voiceMemosFilePath));
            String line;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                recordings.add(line);
                HBox current = makeItem(line);
                items.getChildren().add(current);
            }
            updateItems();
            scanner.close();
        } catch(FileNotFoundException f) {
            try {
                File file = new File(voiceMemosFilePath);
                file.createNewFile();
            } catch (Exception c) {
                System.err.println("FAILED");
            }
        }
    }

    protected static void saveOnClose() {
        try {
            PrintWriter pw = new PrintWriter(voiceMemosFilePath);
            for(String s : recordings)
                pw.println(s);
            pw.close();
        } catch(Exception t) {}
    }

}
