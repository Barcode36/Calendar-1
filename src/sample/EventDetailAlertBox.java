package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventDetailAlertBox {
    //    public static void display(String title, Long startTime, Long endTime, String description, Long notifyTime, String color, double x, double y) {
    public void display(Object object, double x, double y) {
        Stage window = new Stage();
        window.setTitle("Chi tiết sự kiện");

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        VBox layout = new VBox(0);
        layout.setPadding(new Insets(0, 0, 0, 0));
        layout.setMaxWidth(600);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Menu bar init
        HBox menubar = new HBox();
        menubar.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));
        menubar.setPadding(new Insets(20, 20, 20, 20));

        File editImagefile = new File("resources/edit.png");
        Image editImage = new Image(editImagefile.toURI().toString());
        ImageView editImageView = new ImageView();
        editImageView.setImage(editImage);
        editImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.close();
                CreateEventAlertBox createEventAlertBox = new CreateEventAlertBox();
                Calendar calendar = Calendar.getInstance();
                createEventAlertBox.display(1, calendar, event.getScreenX(), event.getScreenY(), true);
            }
        });
        HBox.setMargin(editImageView, new Insets(10, 10, 10, 10));
        Tooltip.install(editImageView, new Tooltip("Chỉnh sửa"));

        File deleteImagefile = new File("resources/delete.png");
        Image deleteImage = new Image(deleteImagefile.toURI().toString());
        ImageView deleteImageView = new ImageView();
        deleteImageView.setImage(deleteImage);
        HBox.setMargin(deleteImageView, new Insets(10, 10, 10, 10));
        Tooltip.install(deleteImageView, new Tooltip("Xóa"));

        menubar.getChildren().addAll(editImageView, deleteImageView);
        layout.getChildren().add(menubar);
        // End of menu bar init

        Label subTitleLabel = new Label();
        subTitleLabel.setFont(new Font("System", 22));
        subTitleLabel.setMaxWidth(Double.MAX_VALUE);
        subTitleLabel.setPadding(new Insets(10, 8, 10, 8));
        VBox.setVgrow(subTitleLabel, Priority.ALWAYS);
        subTitleLabel.setTextFill(Color.WHITE);
        subTitleLabel.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label();
        titleLabel.setFont(new Font("System", 32));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(20, 8, 30, 15));
        VBox.setVgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));

        if (object instanceof Event) {
            Event event = (Event) object;

            HBox eventTimeHBox = new HBox();
            VBox.setMargin(eventTimeHBox, new Insets(10, 0, 0, 0));
            eventTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            eventTimeHBox.setPadding(new Insets(0, 0, 10, 0));

            ImageView eventTimeImageView = makeImageView("eventtimeicon", "Thời gian",
                    0, 10, 0, 8);

            Label eventTimeLabel = new Label();
            eventTimeLabel.setFont(new Font("System", 22));
            eventTimeLabel.setTextFill(Color.BLACK);

            eventTimeHBox.getChildren().addAll(eventTimeImageView, eventTimeLabel);

            HBox eventNotifyTimeHBox = new HBox();
            eventNotifyTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            eventNotifyTimeHBox.setPadding(new Insets(0, 0, 10, 0));

            ImageView eventNotifyTimeImageView = makeImageView("notifyicon", "Thông báo lúc",
                    0, 6, 0, 8);

            Label eventNotifyTimeLabel = new Label();
            eventNotifyTimeLabel.setPadding(new Insets(5, 0, 0, 0));
            eventNotifyTimeLabel.setFont(new Font("System", 22));
            eventNotifyTimeLabel.setTextFill(Color.BLACK);

            if (event.getTitle().isEmpty()) {
                titleLabel.setText("Không có tiêu đề");
            } else {
                titleLabel.setText(event.getTitle());
            }
            titleLabel.setBackground(new Background(new BackgroundFill(Color.web(event.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));
            layout.getChildren().add(titleLabel);

            java.util.Date eventStartTime = new java.util.Date(event.getStartTime() * 1000);
            java.util.Date eventEndTime = new java.util.Date(event.getEndTime() * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
            eventTimeLabel.setText(formatter.format(eventStartTime) + "  -  " + formatter.format(eventEndTime));

            java.util.Date eventNotifyTime = new java.util.Date(event.getNotifyTime() * 1000);
            Calendar time = Calendar.getInstance();
            time.setTime(eventNotifyTime);
            time.add(Calendar.MILLISECOND, -time.getTimeZone().getOffset(time.getTimeInMillis()));
            Date date = time.getTime();

            if (event.getNotifyTime() > 0) {
                SimpleDateFormat formatter1 = new SimpleDateFormat();
                String format = "";
                if (event.getNotifyTime() >= (3600 * 24)) {
                    formatter1 = new SimpleDateFormat("dd");
                    format = "ngày";
                } else if (event.getNotifyTime() >= 3600) {
                    formatter1 = new SimpleDateFormat("HH:mm");
                    format = "giờ";
                } else if (event.getNotifyTime() < 3600) {
                    formatter1 = new SimpleDateFormat("mm");

                    format = "phút";
                }
                formatter1.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                eventNotifyTimeLabel.setText("Trước " + formatter1.format(date) + " " + format);
            } else if (event.getNotifyTime() == -1) {
                eventNotifyTimeLabel.setText("Không thông báo");
            } else if (event.getNotifyTime() == 0) {
                eventNotifyTimeLabel.setText("Tại thời điểm sự kiện");
            }

            eventNotifyTimeHBox.getChildren().addAll(eventNotifyTimeImageView, eventNotifyTimeLabel);

            layout.getChildren().addAll(eventTimeHBox, eventNotifyTimeHBox);
            window.setMinWidth(600);
            window.setMinHeight(350);

            if (!event.getDescription().isEmpty()) {
                HBox eventDescriptionHBox = new HBox();
                eventDescriptionHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                eventDescriptionHBox.setPadding(new Insets(5, 0, 10, 0));

                File eventDescriptionImageFile = new File("resources/description.png");
                Image eventDescriptionImage = new Image(eventDescriptionImageFile.toURI().toString());
                ImageView eventDescriptionImageView = new ImageView();
                HBox.setMargin(eventDescriptionImageView, new Insets(0, 10, 0, 8));
                eventDescriptionImageView.setImage(eventDescriptionImage);
                Tooltip.install(eventDescriptionImageView, new Tooltip("Mô tả sự kiện"));

                Label eventDescriptionLabel = new Label(event.getDescription());
                eventDescriptionLabel.setFont(new Font("System", 22));
                eventDescriptionLabel.setTextFill(Color.BLACK);

                eventDescriptionHBox.getChildren().addAll(eventDescriptionImageView, eventDescriptionLabel);
                layout.getChildren().add(eventDescriptionHBox);
                window.setMinWidth(600);
                window.setMinHeight(400);
            }
        }else if(object instanceof Holiday){
            subTitleLabel.setText("Ngày lễ");
            layout.getChildren().add(subTitleLabel);
            Holiday holiday = (Holiday)object;
            if(holiday.getName().isEmpty()){
                titleLabel.setText("Ngày lễ không có tiêu đề");
            }else {
                titleLabel.setText(holiday.getName());
            }
            layout.getChildren().add(titleLabel);
            window.setMinWidth(600);
            window.setMinHeight(280);
        }else if (object instanceof Birthday){
            subTitleLabel.setText("Sinh nhật");
            layout.getChildren().add(subTitleLabel);
            Birthday birthday = (Birthday) object;
            if(birthday.getName().isEmpty()){
                titleLabel.setText("Sinh nhật không có tiêu đề");
            }else {
                titleLabel.setText(birthday.getName());
            }
            layout.getChildren().add(titleLabel);
            window.setMinWidth(600);
            window.setMinHeight(280);
        }

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
//        window.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (!isNowFocused) {
//                window.close();
//            }
//        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private ImageView makeImageView(String fileName, String toolTip, double topPadding, double rightPadding, double bottomPadding, double leftPadding) {
        File imageFile = new File("resources/" + fileName + ".png");
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView();
        HBox.setMargin(imageView, new Insets(topPadding, rightPadding, bottomPadding, leftPadding));
        imageView.setImage(image);
        Tooltip.install(imageView, new Tooltip(toolTip));

        return imageView;
    }

    private String getObjectColor(Object object) {
        if (object instanceof Event) {
            Event event = (Event) object;
            return event.getColor();
        } else if (object instanceof Holiday) {
            DbConnection dbConnection = new DbConnection();
            return dbConnection.getDefaultColor("holiday");
        } else if (object instanceof Birthday) {
            DbConnection dbConnection = new DbConnection();
            return dbConnection.getDefaultColor("birthday");
        }
        return "";
    }
}
