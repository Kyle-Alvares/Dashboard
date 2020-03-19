package code;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import java.util.Timer;
import java.util.TimerTask;

public class Home {

    public static VBox home() {
        VBox pane = new VBox();

        Text timeText = new Text();
        timeText.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, 30));
        timeText.setFill(Color.WHITE);

        Text dateText = new Text();
        dateText.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, 15));
        dateText.setFill(Color.WHITE);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Date date = new Date();
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("h:mm:ss a");
                String timeDisplay = formatter.format(date);
                formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
                String dateDisplay = formatter.format(date);
                timeText.setText(timeDisplay);
                dateText.setText(dateDisplay);
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

        pane.setStyle(Styling.BACKGROUND_STYLE);
        pane.getChildren().addAll(timeText, dateText);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

}
