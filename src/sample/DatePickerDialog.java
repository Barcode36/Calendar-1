package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.Calendar;

public class DatePickerDialog {
    public static void display(Calendar c, double x, double y)
    {
        Stage window = new Stage();
        window.setTitle("Chọn ngày");
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        DatePicker temp = new DatePicker();
        temp.setValue(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate date = temp.getValue();
                c.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
                c.set(Calendar.MONTH, date.getMonthValue());
                c.set(Calendar.YEAR, date.getYear());
                window.close();
            }
        });
        temp.setMinHeight(200);
        temp.setMinWidth(200);
        VBox.setVgrow(temp, Priority.ALWAYS);


        VBox layout = new VBox(0);
        layout.setPadding(new Insets(0, 0, 20, 0));
        layout.setMaxWidth(600);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(temp);

        if (primaryScreenBounds.getWidth() - x < window.getMinWidth()) {
            if (primaryScreenBounds.getHeight() - y < window.getMinHeight()) {
                window.setY(y - window.getMinHeight());
                window.setX(x - window.getMinWidth());
            } else {
                window.setY(y);
                window.setX(x - window.getMinWidth());
            }
        } else {
            if (primaryScreenBounds.getHeight() - y < window.getMinHeight()) {
                window.setY(y - window.getMinHeight());
                window.setX(x);
            } else {
                window.setY(y);
                window.setX(x);
            }
        }
        window.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                window.close();
            }
        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
