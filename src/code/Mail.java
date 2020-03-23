package code;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.border.EmptyBorder;

public class Mail {

    // links to different mailing services
    protected final static String gmail = "https://accounts.google.com/signin/v2/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
    protected final static String yahoo = "https://login.yahoo.com/?.src=ym&.lang=en-US&.intl=us&.done=https%3A%2F%2Fmail.yahoo.com%2Fd";

    public static VBox mail() {

        // web view
        WebView myWebView = new WebView();
        WebEngine engine = myWebView.getEngine();
        engine.load("https://gmail.com");

        HBox buttons = new HBox();

        // choose from different mailing services - idea was not implemented by decision
        Button gMail = new Button("Gmail");
        gMail.setStyle(Styling.EMAIL_BUTTON_STYLE);
        gMail.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        gMail.setOnMouseClicked(e -> engine.load(gmail));

        Button yaHoo = new Button("Yahoo");
        yaHoo.setStyle(Styling.EMAIL_BUTTON_STYLE);
        yaHoo.setFont(Font.font(Styling.MAIN_FONT, FontWeight.NORMAL, 15));
        yaHoo.setOnMouseClicked(e -> engine.load(yahoo));

        buttons.getChildren().addAll(gMail, yaHoo);

        VBox root = new VBox();
        root.getChildren().addAll(myWebView);

        return root;
    }

}
