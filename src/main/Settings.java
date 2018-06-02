package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Settings {
    public void display(){
        Stage window = new Stage();
        window.setMinWidth(610);
        window.setMinHeight(500);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        DbConnection colorConnection = new DbConnection();

        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);



        Label labelMauSuKien = new Label();
        labelMauSuKien.setText("Màu sự kiện:");
        labelMauSuKien.setFont(new Font("System", 20));

        ColorPicker colorPickerSuKien = new ColorPicker();
        colorPickerSuKien.setValue(Color.valueOf(colorConnection.getDefaultColor("event")));

        HBox hBoxMauSuKien = new HBox();
        hBoxMauSuKien.getChildren().addAll(labelMauSuKien, colorPickerSuKien);

        Button buttonOk = new Button();
        buttonOk.setText("Ok Thiện =))");
        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorConnection.setDefaultColor("event", Utils.toRGBCode(colorPickerSuKien.getValue()));
                window.close();
            }
        });

        VBox vBoxSettings = new VBox();
        vBoxSettings.getChildren().addAll(hBoxMauSuKien, buttonOk);

        Scene scene = new Scene(vBoxSettings);
        window.setScene(scene);
        window.showAndWait();
    }

}
