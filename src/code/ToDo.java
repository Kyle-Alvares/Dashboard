package code;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ToDo {

    // variables
    protected static LinkedList<String> list = new LinkedList<>();
    private static VBox items = new VBox(2);
    private static ScrollPane scroll = new ScrollPane();
    protected static String todoFilePath = "src/files/to-do-items.txt";

    public static BorderPane todo() {
        // layout
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10,10,10,10));
        pane.setStyle(Styling.BACKGROUND_STYLE);

        // scroll pane settings
        scroll.setPrefSize(550, 275);
        scroll.setContent(items);
        scroll.setStyle(Styling.SCROLL_STYLE);

        // outer-box with scroll pane
        VBox outer = new VBox();
        outer.getChildren().add(scroll);
        outer.setStyle(Styling.LIST_STYLE);

        // layout to get input: textfield, buttons
        HBox input = new HBox();
        input.setSpacing(10);

        // top pane
        ImageView checklist = new ImageView(Images.CHECKLIST);
        Text toDoText = new Text();
        toDoText.setText("To-Do List");
        toDoText.setFill(Color.WHITE);
        toDoText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 19));
        HBox titleTop = new HBox(10);
        titleTop.setAlignment(Pos.CENTER_LEFT);
        titleTop.setPadding(new Insets(2,0,5,0));
        titleTop.getChildren().addAll(checklist, toDoText);

        // textfield to get to do item
        TextField textfield = new TextField();
        textfield.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        textfield.setStyle(Styling.TEXTFIELD_STYLE);
        textfield.setMinWidth(440);

        // hit enter to add item
        textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    addItem(textfield.getText());
                    textfield.clear();
                }
            }
        });

        // add item by pressing button
        Button add = new Button("Add");
        add.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        add.setStyle(Styling.TEXT_BUTTON_STYLE);
        add.setOnMouseClicked(e -> {
            addItem(textfield.getText());
            textfield.clear();
        });

        // save to do list somewhere on computer
        Button save = new Button("Save");
        save.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        save.setStyle(Styling.TEXT_BUTTON_STYLE);
        save.setOnMouseClicked(e -> saveAs());

        // update layout

        input.getChildren().addAll(textfield, add, save);
        updateList();

        pane.setTop(titleTop);
        pane.setBottom(input);
        pane.setCenter(outer);
        return pane;
    }

    // adds item to list
    private static void addItem(String item) {
        try {
            if(item.charAt(0) != ' ' && item.charAt(0) != '\n') {
                list.add(item);
                HBox newItem = makeItem(item);
                items.getChildren().add(newItem);
            }
        } catch (Exception exception) {

        }
    }

    // creates a new item for the to do list
    private static HBox makeItem(String text) {
        // item layout
        HBox pane = new HBox();

        // label settings
        Label label = new Label();
        label.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        label.setPadding(new Insets(5,0,5,0));
        label.setText("   " + text);
        label.setMinWidth(529);
        label.setStyle(Styling.CONTACT_ITEM_LABEL_STYLE);

        // image used to get rid of items
        ImageView delete = new ImageView(Images.CHECK_DEFAULT);

        // mouse hovering
        delete.setOnMouseEntered(e -> {
            delete.setImage(Images.CHECK_ACTIVE);
        });
        // mouse no longer hovering
        delete.setOnMouseExited(e -> {
            delete.setImage(Images.CHECK_DEFAULT);
        });
        // mouse clicked - item deleted
        delete.setOnMouseClicked(e -> {
            list.remove(text);
            updateList();
        });

        pane.getChildren().addAll(delete, label);
        return pane;
    }

    // updates list after item is removed and during program startup
    private static void updateList() {
        items.getChildren().clear();
        for(String s : list) {
            HBox newItem = makeItem(s);
            items.getChildren().add(newItem);
        }
    }

    // loads to do data upon starting program
    protected static void loadData() {
        try {
            Scanner scanner = new Scanner(new File(todoFilePath));
            String line;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                ToDo.list.add(line);
            }
            scanner.close();
        } catch(FileNotFoundException f) {
            try {
                File file = new File(todoFilePath);
                file.createNewFile();
            } catch (Exception c) {
                System.err.println("FAILED");
            }
        }
    }

    // save to do items somewhere on computer
    public static void saveAs() {
        Stage fileWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("To-Do List");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File file = fileChooser.showSaveDialog(fileWindow);
        try {
            PrintWriter pw = new PrintWriter(file);
            for(String task : list)
                pw.println(task);
            pw.close();
        } catch(Exception t) {}
    }

    // updates list on close
    protected static void saveOnClose() {
        try {
            PrintWriter pw = new PrintWriter(todoFilePath);
            for(String task : list)
                pw.println(task);
            pw.close();
        } catch(Exception t) {}
    }
}
