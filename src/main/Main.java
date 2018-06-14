package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("CalendarGrid.fxml"));
        File imageFile = new File("resources/icon.png");
        Image image = new Image(imageFile.toURI().toString());
        primaryStage.getIcons().add(image);
        primaryStage.setMinWidth(1368);
        primaryStage.setMinHeight(768);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(new Scene(root, 1368, 768));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
