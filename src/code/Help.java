package code;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Help {

    //IO streams
    private static DataOutputStream toServer = null;
    private static DataInputStream fromServer = null;
    private static VBox messages;
    private static TextField textfield;

    public static VBox help() {

        // initialize
        messages = new VBox(10);
        promptMessage();

        // main layout
        VBox mainLayout = new VBox();

        // title
        ImageView helpIcon = new ImageView(Images.HELP);
        Text helpText = new Text();
        helpText.setText("Help & Feedback");
        helpText.setFill(Color.WHITE);
        helpText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 19));
        HBox titleTop = new HBox(10);
        titleTop.setAlignment(Pos.CENTER_LEFT);
        titleTop.setPadding(new Insets(2,0,5,0));
        titleTop.getChildren().addAll(helpIcon, helpText);

        // scroll pane for messages
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(550, 275);
        scroll.setContent(messages);
        scroll.setStyle(Styling.SCROLL_STYLE);

        // layout for input
        HBox input = new HBox();
        input.setAlignment(Pos.CENTER_LEFT);
        input.setPadding(new Insets(5,0,0,0));
        // textfield to get input
        textfield = new TextField();
        textfield.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        textfield.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        textfield.setMinWidth(534);
        // hit enter to send message
        textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    sendMessage();
                    textfield.clear();
                }
            }
        });
        // send icon for input
        ImageView sendIcon = new ImageView(Images.SEND_DEFAULT);
        sendIcon.setOnMouseEntered(e -> sendIcon.setImage(Images.SEND_ACTIVE));
        sendIcon.setOnMouseExited(e -> sendIcon.setImage(Images.SEND_DEFAULT));
        sendIcon.setOnMouseClicked(e -> {
            sendMessage();
            textfield.clear();
        });
        // input items
        input.getChildren().addAll(textfield, sendIcon);

        try {
            // socket to connect to server
            Socket socket = new Socket("localhost" , 8240);
            // server input stream
            fromServer = new DataInputStream(socket.getInputStream());
            // server output stream
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex){
            serverError();
            textfield.setEditable(false);
            sendIcon.setOnMouseClicked(e -> {});
        }

        mainLayout.getChildren().addAll(titleTop, scroll, input);
        mainLayout.setPadding(new Insets(10,10,10,10));

        return mainLayout;
    }

    private static void sendMessage() {
        String mssg = textfield.getText();
        if(mssg.charAt(0) != ' ' && mssg.charAt(0) != '\n') {
            try {
                // send message to server
                toServer.writeUTF(mssg);
                toServer.flush();

                // get response from the server
                String response = fromServer.readUTF();

                // create client message
                HBox clientBox = new HBox();
                clientBox.setAlignment(Pos.CENTER_RIGHT);
                Label clientMessage = new Label(mssg);
                clientMessage.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 12));
                clientMessage.setStyle(Styling.CLIENT_MESSAGE_STYLE);
                clientMessage.setPadding(new Insets(10,20,10,20));
                clientMessage.setWrapText(true);
                clientMessage.setMaxWidth(350);
                clientBox.getChildren().add(clientMessage);

                // create host message
                HBox hostBox = new HBox();
                hostBox.setAlignment(Pos.CENTER_LEFT);
                Label hostMessage = new Label(response);
                hostMessage.setStyle(Styling.HOST_MESSAGE_STYLE);
                hostMessage.setPadding(new Insets(10,20,10,20));
                hostMessage.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 12));
                hostMessage.setWrapText(true);
                hostMessage.setMaxWidth(350);
                clientBox.getChildren().add(hostMessage);

                BorderPane hostPane = new BorderPane();
                hostPane.setMinWidth(540);
                hostPane.setLeft(hostMessage);

                BorderPane clientPane = new BorderPane();
                clientPane.setMinWidth(540);
                clientPane.setRight(clientMessage);

                messages.getChildren().addAll(clientPane, hostPane);

            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    protected static void promptMessage() {
        // create prompt message
        String promptMessage = "Please enter a tab name to view its features or feedback - \"...\" to give feedback.";
        HBox promptBox = new HBox();
        promptBox.setAlignment(Pos.CENTER_LEFT);
        Label prompt = new Label(promptMessage);
        prompt.setStyle(Styling.HOST_MESSAGE_STYLE);
        prompt.setPadding(new Insets(10,20,10,20));
        prompt.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 12));
        prompt.setWrapText(true);
        prompt.setMaxWidth(350);
        promptBox.getChildren().add(prompt);
        messages.getChildren().add(promptBox);
    }

    protected static void serverError() {
        // create prompt message
        String promptMessage = "The server is not online. Please try again later. :(";
        HBox promptBox = new HBox();
        promptBox.setAlignment(Pos.CENTER_LEFT);
        Label prompt = new Label(promptMessage);
        prompt.setStyle(Styling.HOST_MESSAGE_STYLE);
        prompt.setPadding(new Insets(10,20,10,20));
        prompt.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 12));
        prompt.setWrapText(true);
        prompt.setMaxWidth(350);
        promptBox.getChildren().add(prompt);
        messages.getChildren().add(promptBox);
    }
}
