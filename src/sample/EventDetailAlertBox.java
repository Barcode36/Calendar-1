package sample;

import javafx.beans.InvalidationListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventDetailAlertBox {
    public static void display(String title, Long startTime, Long endTime, String description, Long notifyTime, double x, double y) {
        Stage window = new Stage();
        window.setTitle("Chi tiết sự kiện");
        window.setMinWidth(600);
        window.setMinHeight(282);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        // Menu bar init
        HBox menubar = new HBox();
        menubar.setBackground(new Background(new BackgroundFill(Color.rgb(3, 155, 229), CornerRadii.EMPTY, Insets.EMPTY)));
        menubar.setPadding(new Insets(20, 20, 20, 20));

        File editImagefile = new File("resources/edit.png");
        Image editImage = new Image(editImagefile.toURI().toString());
        ImageView editImageView = new ImageView();
        editImageView.setImage(editImage);
        HBox.setMargin(editImageView, new Insets(10, 10, 10, 10));
        Tooltip.install(editImageView, new Tooltip("Chỉnh sửa"));

        File deleteImagefile = new File("resources/delete.png");
        Image deleteImage = new Image(deleteImagefile.toURI().toString());
        ImageView deleteImageView = new ImageView();
        deleteImageView.setImage(deleteImage);
        HBox.setMargin(deleteImageView, new Insets(10, 10, 10, 10));
        Tooltip.install(deleteImageView, new Tooltip("Xóa"));

        menubar.getChildren().addAll(editImageView, deleteImageView);
        // End of menu bar init

        Label titleLabel = new Label("   " + title);
        titleLabel.setFont(new Font("System", 32));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(20, 8, 30, 8));
        VBox.setVgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setBackground(new Background(new BackgroundFill(Color.rgb(3, 155, 229), CornerRadii.EMPTY, Insets.EMPTY)));

        HBox eventTimeHBox = new HBox();
        VBox.setMargin(eventTimeHBox, new Insets(10, 0, 0, 0));
        eventTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        File eventTimeImageFile = new File("resources/eventtimeicon.png");
        Image eventTimeImage = new Image(eventTimeImageFile.toURI().toString());
        ImageView eventTimeImageView = new ImageView();
        HBox.setMargin(eventTimeImageView, new Insets(0, 10, 0, 8));
        eventTimeImageView.setImage(eventTimeImage);
        Tooltip.install(eventTimeImageView, new Tooltip("Thời gian"));

        java.util.Date eventStartTime = new java.util.Date((long) startTime * 1000);
        java.util.Date eventEndTime = new java.util.Date((long) endTime * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        Label eventTimeLabel = new Label(formatter.format(eventStartTime) + "  -  " + formatter.format(eventEndTime));
        eventTimeLabel.setFont(new Font("System", 22));
        eventTimeLabel.setTextFill(Color.BLACK);

        eventTimeHBox.getChildren().addAll(eventTimeImageView, eventTimeLabel);

        HBox eventNotifyTimeHBox = new HBox();
        eventNotifyTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventNotifyTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        File eventNotifyTimeImageFile = new File("resources/notifyicon.png");
        Image eventNotifyTimeImage = new Image(eventNotifyTimeImageFile.toURI().toString());
        ImageView eventNotifyTimeImageView = new ImageView();
        HBox.setMargin(eventNotifyTimeImageView, new Insets(0, 6, 0, 8));
        eventNotifyTimeImageView.setImage(eventNotifyTimeImage);
        Tooltip.install(eventNotifyTimeImageView, new Tooltip("Thông báo lúc"));

        java.util.Date eventNotifyTime = new java.util.Date((long) notifyTime * 1000);
        Calendar time = Calendar.getInstance();
        time.setTime(eventNotifyTime);
        time.add(Calendar.MILLISECOND, -time.getTimeZone().getOffset(time.getTimeInMillis()));
        Date date = time.getTime();

        SimpleDateFormat formatter1 = new SimpleDateFormat();
        String format = "";
        if (notifyTime >= (3600 * 24)) {
            formatter1 = new SimpleDateFormat("dd");
            format = "ngày";
        } else if (notifyTime >= 3600) {
            formatter1 = new SimpleDateFormat("HH:mm");
            format = "giờ";
        } else if (notifyTime < 3600) {
            formatter1 = new SimpleDateFormat("mm");

            format = "phút";
        }
        formatter1.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Label eventNotifyTimeLabel = new Label("Trước " + formatter1.format(date) + " " + format);
        eventNotifyTimeLabel.setPadding(new Insets(5, 0, 0, 0));
        eventNotifyTimeLabel.setFont(new Font("System", 22));
        eventNotifyTimeLabel.setTextFill(Color.BLACK);

        eventNotifyTimeHBox.getChildren().addAll(eventNotifyTimeImageView, eventNotifyTimeLabel);


        HBox eventDescriptionHBox = new HBox();
        eventDescriptionHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventDescriptionHBox.setPadding(new Insets(5, 0, 10, 0));

        File eventDescriptionImageFile = new File("resources/description.png");
        Image eventDescriptionImage = new Image(eventDescriptionImageFile.toURI().toString());
        ImageView eventDescriptionImageView = new ImageView();
        HBox.setMargin(eventDescriptionImageView, new Insets(0, 10, 0, 8));
        eventDescriptionImageView.setImage(eventDescriptionImage);
        Tooltip.install(eventDescriptionImageView, new Tooltip("Mô tả sự kiện"));

        Label eventDescriptionLabel = new Label(description);
        eventDescriptionLabel.setFont(new Font("System", 22));
        eventDescriptionLabel.setTextFill(Color.BLACK);

        eventDescriptionHBox.getChildren().addAll(eventDescriptionImageView, eventDescriptionLabel);

        VBox layout = new VBox(0);
        layout.setPadding(new Insets(0, 0, 20, 0));
        layout.setMaxWidth(600);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(menubar, titleLabel, eventTimeHBox, eventNotifyTimeHBox, eventDescriptionHBox);

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
        window.show();
    }
}
