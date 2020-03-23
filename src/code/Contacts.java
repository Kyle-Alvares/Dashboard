package code;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class Contacts {

    public static class Person {
        public String fName, lName, phoneNum, address;
        public Person(String fName, String lName, String phoneNum, String address) {
            this.fName = fName;
            this.lName = lName;
            this.phoneNum = phoneNum;
            this.address = address;
        }
        public String toString() {
            return fName + ":" + lName + ":" + phoneNum + ":" + address;
        }
    }

    // main contact variables
    private static BorderPane mainLayout;
    private static VBox mainTop;
    private static GridPane titleTop;
    private static LinkedList<Person> people = new LinkedList<>(); // stores contacts
    private static ScrollPane scroll = new ScrollPane();
    private static VBox  items = new VBox(2);
    protected static String contactsFilePath = "src/files/contacts.txt";
    private static TextField searchField;

    private static Label fLabel, lLabel, pLabel, aLabel;
    public static BorderPane contacts() {

        // layout
        mainLayout = new BorderPane();

        // top pane
        mainTop = new VBox(5);
        titleTop = new GridPane();

        // search icon top pane
        ImageView searchIcon = new ImageView(Images.SEARCH);
        titleTop.add(searchIcon, 0, 0);

        // text top pane
        Text searchText = new Text();
        searchText.setText("Search");
        searchText.setFill(Color.WHITE);
        searchText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 19));
        titleTop.add(searchText, 2, 0);

        // refresh contacts list button
        ImageView refreshIcon = new ImageView(Images.REFRESH_DEFAULT);
        refreshIcon.setOnMouseEntered(e -> refreshIcon.setImage(Images.REFRESH_ACTIVE));
        refreshIcon.setOnMouseExited(e -> refreshIcon.setImage(Images.REFRESH_DEFAULT));
        refreshIcon.setOnMouseClicked(e -> refresh());
        titleTop.add(refreshIcon,84,0);

        // create new contact button
        ImageView newIcon = new ImageView(Images.NEW_DEFAULT);
        newIcon.setOnMouseEntered(e -> newIcon.setImage(Images.NEW_ACTIVE));
        newIcon.setOnMouseExited(e -> newIcon.setImage(Images.NEW_DEFAULT));
        newIcon.setOnMouseClicked(e -> createContact());
        titleTop.add(newIcon,85,0);

        // top pane settings
        titleTop.setAlignment(Pos.CENTER_LEFT);
        titleTop.setHgap(5);

        // text field used to search contacts
        searchField = new TextField();
        searchField.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 14));
        searchField.setStyle(Styling.TEXTFIELD_STYLE);
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    items.getChildren().clear();
                    searchResults(searchField.getText());
                }
            }
        });

        // scroll pane settings
        scroll.setPrefSize(550, 275);
        scroll.setContent(items);
        scroll.setStyle(Styling.SCROLL_STYLE);

        mainTop.setAlignment(Pos.CENTER);
        mainTop.setPadding(new Insets(10,10,0, 10));
        mainTop.getChildren().addAll(titleTop, searchField, scroll);

        // layout
        mainLayout.setStyle(Styling.BACKGROUND_STYLE);
        mainLayout.setTop(mainTop);

        return mainLayout;
    }

    // creates new contact
    public static void createContact() {

        // new window for creating contact
        Stage window = new Stage();

        BorderPane createContactLayout = new BorderPane();

        // layout for text fields and labels
        GridPane contactPane = new GridPane();
        contactPane.setPadding(new Insets(35,20,0,20));
        contactPane.setVgap(10);
        contactPane.setHgap(15);
        contactPane.setStyle(Styling.BACKGROUND_STYLE);

        // new contact titles, label and text fields

        Text newContact = new Text();
        newContact.setText("New Contact");
        newContact.setFill(Color.WHITE);
        newContact.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 26));
        contactPane.add(newContact, 0, 0);

        Label firstName = new Label("First Name");
        firstName.setStyle(Styling.LABEL_CONTACT_STYLE);
        firstName.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 18));
        contactPane.add(firstName, 0, 2);

        TextField firstNameText = new TextField();
        firstNameText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        firstNameText.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        firstNameText.setMinWidth(250);
        contactPane.add(firstNameText, 0, 3);

        Label lastName = new Label("Last Name");
        lastName.setStyle(Styling.LABEL_CONTACT_STYLE);
        lastName.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 18));
        contactPane.add(lastName, 0, 4);

        TextField lastNameText = new TextField();
        lastNameText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        lastNameText.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        contactPane.add(lastNameText, 0, 5);

        Label phoneNumber = new Label("Phone Number");
        phoneNumber.setStyle(Styling.LABEL_CONTACT_STYLE);
        phoneNumber.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 18));
        contactPane.add(phoneNumber, 0, 6);

        TextField phoneNumberText = new TextField();
        phoneNumberText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        phoneNumberText.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        contactPane.add(phoneNumberText, 0, 7);

        Label address = new Label("Address");
        address.setStyle(Styling.LABEL_CONTACT_STYLE);
        address.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 18));
        contactPane.add(address, 0, 8);

        TextField addressText = new TextField();
        addressText.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 14));
        addressText.setStyle(Styling.CONTACT_TEXTFIELD_STYLE);
        contactPane.add(addressText, 0, 9);

        HBox registerPane = new HBox();
        registerPane.setAlignment(Pos.CENTER_RIGHT);
        registerPane.setPadding(new Insets(0,20,20,20));
        registerPane.setStyle(Styling.BACKGROUND_STYLE);

        // register adds new contact to list
        Button register = new Button("Add Contact");
        register.setFont(Font.font(Styling.MAIN_FONT, FontWeight.BOLD, 15));
        register.setStyle(Styling.TEXT_BUTTON_STYLE);
        register.setOnMouseClicked(e -> {
            try {
                // checks if fields are not left empty and if phone number is valid
                if (fieldNotEmpty(firstNameText.getText()) && fieldNotEmpty(lastNameText.getText()) &&
                fieldNotEmpty(phoneNumberText.getText()) && fieldNotEmpty(addressText.getText()) &&
                validPhoneNumber(phoneNumberText.getText())) {
                    people.add(new Person(firstNameText.getText(), lastNameText.getText(), phoneNumberText.getText(),
                            addressText.getText()));

                    HBox personDisplay = makePerson(firstNameText.getText(), lastNameText.getText(),
                            phoneNumberText.getText(), addressText.getText());
                    items.getChildren().add(personDisplay);

                window.close();
                }
            } catch(Exception exception) {}
        });

        // adds nodes to pane

        registerPane.getChildren().add(register);

        createContactLayout.setCenter(contactPane);
        createContactLayout.setBottom(registerPane);

        window.setScene(new Scene(createContactLayout, 275,425));
        window.setTitle("Create Contact");
        window.getIcons().add(Images.ICON);
        window.setResizable(false);
        window.show();
    }

    // checks if text field is not empty
    protected static boolean fieldNotEmpty(String text) {
        if(text.charAt(0) != ' ' && text.charAt(0) != '\n')
            return true;
        return false;
    }

    // checks for valid phone number
    protected static boolean validPhoneNumber(String number) {
        if(number.length() > 9) {
            int num;
            for(int i=0; i < number.length(); i++) {
                try {
                    num = Integer.parseInt(number.charAt(i) + "");
                } catch (Exception e) {
                    return false;
                }
                if(num < 0 && num > 9)
                    return false;
            }
            return true;
        }
        return false;
    }

    // creates new contact node for display
    private static HBox makePerson(String first, String last, String phone, String address) {
        ImageView contactDelete = new ImageView(Images.DELETE_CONTACT_DEFAULT);
        contactDelete.setOnMouseEntered(c -> contactDelete.setImage(Images.DELETE_CONTACT_ACTIVE));
        contactDelete.setOnMouseExited(c -> contactDelete.setImage(Images.DELETE_CONTACT_DEFAULT));
        contactDelete.setOnMouseClicked(c -> {
            for(int j=0; j < people.size(); j++) {
                if(people.get(j).fName.equals(first)) {
                    people.remove(j);
                    items.getChildren().clear();
                    for(Person p : people) {
                        HBox current = makePerson(p.fName, p.lName, p.phoneNum, p.address);
                        items.getChildren().add(current);
                    }
                }
            }
        });

        fLabel = new Label(first);
        fLabel.setStyle(Styling.CONTACT_ITEM_LABEL_STYLE);
        fLabel.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        fLabel.setAlignment(Pos.CENTER);
        fLabel.setPadding(new Insets(5,0,5,0));
        fLabel.setMinWidth(125);

        lLabel = new Label(last);
        lLabel.setStyle(Styling.CONTACT_ITEM_LABEL_STYLE);
        lLabel.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        lLabel.setAlignment(Pos.CENTER);
        lLabel.setPadding(new Insets(5,0,5,0));
        lLabel.setMinWidth(125);

        pLabel = new Label(phone);
        pLabel.setStyle(Styling.CONTACT_ITEM_LABEL_STYLE);
        pLabel.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        pLabel.setAlignment(Pos.CENTER);
        pLabel.setPadding(new Insets(5,0,5,0));
        pLabel.setMinWidth(125);

        aLabel = new Label(address);
        aLabel.setStyle(Styling.CONTACT_ITEM_LABEL_STYLE);
        aLabel.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        aLabel.setAlignment(Pos.CENTER);
        aLabel.setPadding(new Insets(5,0,5,0));
        aLabel.setMinWidth(154);

        HBox personDisplay = new HBox(0);
        personDisplay.getChildren().addAll(contactDelete, fLabel,lLabel, pLabel, aLabel);

        return personDisplay;
    }

    // loads contacts when program starts
    protected static void loadContacts() {
        try {
            Scanner scanner = new Scanner(new File(contactsFilePath));
            String line;
            String[] data;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                data = line.split(":");
                people.add(new Person(data[0], data[1], data[2], data[3]));
                HBox current = makePerson(data[0], data[1], data[2], data[3]);
                items.getChildren().add(current);
            }
            scanner.close();
        } catch(FileNotFoundException f) {
            try {
                File file = new File(contactsFilePath);
                file.createNewFile();
            } catch (Exception c) {
                System.err.println("FAILED");
            }
        }
    }

    // saves all changes made to contacts when program terminates
    protected static void saveOnClose() {
        try {
            PrintWriter pw = new PrintWriter(contactsFilePath);
            for(Person p : people)
                pw.println(p);
            pw.close();
        } catch(Exception t) {}
    }

    // gets search results
    private static void searchResults(String search) {
        for(Person p : people) {
            if(p.fName.toLowerCase().equals(search.toLowerCase()) ||
                    p.lName.toLowerCase().equals(search.toLowerCase())) {
                HBox current = makePerson(p.fName, p.lName, p.phoneNum, p.address);
                items.getChildren().add(current);
            }
        }
    }

    // refreshs search results
    private static void refresh() {
        items.getChildren().clear();
        for(Person p : people) {
            HBox current = makePerson(p.fName, p.lName, p.phoneNum, p.address);
            items.getChildren().add(current);
        }
        searchField.setText("");
    }

}
