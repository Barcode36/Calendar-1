package main;

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
    private Stage window;
    private Rectangle2D primaryScreenBounds;
    private ImageView editImageView;
    private ImageView deleteImageView;
    private Label eventTimeLabel;
    private Label eventSubtitleLabel;
    private Label eventTitleLabel;
    private Label eventNotifyTimeLabel;
    private Label eventDescriptionLabel;
    private VBox layout;
    private HBox menuBarHBox;
    private HBox eventTimeHBox;
    private HBox eventNotifyTimeHBox;
    private HBox eventDescriptionHBox;

    public EventDetailAlertBox(){
        window = new Stage();
        window.setTitle("Chi tiết sự kiện");

        layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        eventTitleLabel = new Label();
        eventTitleLabel.setFont(new Font("System", 32));
        eventTitleLabel.setMaxWidth(Double.MAX_VALUE);
        eventTitleLabel.setPadding(new Insets(20, 8, 30, 15));
        VBox.setVgrow(eventTitleLabel, Priority.ALWAYS);
        eventTitleLabel.setTextFill(Color.WHITE);

        Label subTitleLabel = new Label();
        subTitleLabel.setFont(new Font("System", 22));
        subTitleLabel.setMaxWidth(Double.MAX_VALUE);
        subTitleLabel.setPadding(new Insets(10, 8, 10, 8));
        VBox.setVgrow(subTitleLabel, Priority.ALWAYS);
        subTitleLabel.setTextFill(Color.WHITE);

        menuBarHBox = new HBox();
        //menubar.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));
        menuBarHBox.setPadding(new Insets(20, 20, 20, 20));

        editImageView = makeImageView("edit", "Chỉnh sửa", 10,10,10,10);

        deleteImageView = makeImageView("delete", "Xóa",5,10,10,10);

        menuBarHBox.getChildren().addAll(editImageView, deleteImageView);

        eventTimeHBox = new HBox();
        VBox.setMargin(eventTimeHBox, new Insets(10, 0, 0, 0));
        eventTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        ImageView eventTimeImageView = makeImageView("eventtimeicon", "Thời gian",
                0, 10, 0, 8);

        eventTimeLabel = new Label();
        eventTimeLabel.setFont(new Font("System", 22));
        eventTimeLabel.setTextFill(Color.BLACK);

        eventTimeHBox.getChildren().addAll(eventTimeImageView, eventTimeLabel);

        eventNotifyTimeHBox = new HBox();
        eventNotifyTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventNotifyTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        ImageView eventNotifyTimeImageView = makeImageView("notifyicon", "Thông báo lúc",
                0, 6, 0, 8);

        eventNotifyTimeLabel = new Label();
        eventNotifyTimeLabel.setPadding(new Insets(5, 0, 0, 0));
        eventNotifyTimeLabel.setFont(new Font("System", 22));
        eventNotifyTimeLabel.setTextFill(Color.BLACK);

        eventDescriptionHBox = new HBox();
        eventDescriptionHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventDescriptionHBox.setPadding(new Insets(5, 0, 10, 0));

        ImageView eventDescriptionImageView = makeImageView("description", "Mô tả sự kiện", 0,10,0,8);

        Label eventDescriptionLabel = new Label();
        eventDescriptionLabel.setFont(new Font("System", 22));
        eventDescriptionLabel.setTextFill(Color.BLACK);

        eventDescriptionHBox.getChildren().addAll(eventDescriptionImageView, eventDescriptionLabel);

        layout.getChildren().add(menuBarHBox);

        Scene scene = new Scene(layout);
        window.setScene(scene);
    }

    public void display(Object object, double x, double y) {
        layout.getChildren().remove(eventSubtitleLabel);
        layout.getChildren().remove(eventTitleLabel);
        layout.getChildren().remove(eventTimeHBox);
        layout.getChildren().remove(eventNotifyTimeHBox);
        layout.getChildren().remove(eventDescriptionHBox);

        menuBarHBox.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));

        editImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.close();
                CreateEventAlertBox createEventAlertBox = new CreateEventAlertBox();
                Calendar calendar = Calendar.getInstance();
                createEventAlertBox.display(1, calendar, event.getScreenX(), event.getScreenY(), true);
            }
        });

        if (object instanceof Event) {
            Event event = (Event) object;

            if (event.getTitle().isEmpty()) {
                eventTitleLabel.setText("Không có tiêu đề");
            } else {
                eventTitleLabel.setText(event.getTitle());
            }
            eventTitleLabel.setBackground(new Background(new BackgroundFill(Color.web(event.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));

            java.util.Date eventStartTime = new java.util.Date(event.getStartTime() * 1000);
            java.util.Date eventEndTime = new java.util.Date(event.getEndTime() * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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

            layout.getChildren().addAll(eventTitleLabel , eventTimeHBox, eventNotifyTimeHBox);
            window.setMinWidth(600);
            window.setMinHeight(350);

            if (!event.getDescription().isEmpty()) {
                eventDescriptionLabel.setText(event.getDescription());

                layout.getChildren().add(eventDescriptionHBox);
                window.setMinWidth(600);
                window.setMinHeight(400);
            }
        }else if(object instanceof Holiday){
            eventSubtitleLabel.setText("Ngày lễ");
            Holiday holiday = (Holiday)object;
            if(holiday.getName().isEmpty()){
                eventTitleLabel.setText("Ngày lễ không có tiêu đề");
            }else {
                eventTitleLabel.setText(holiday.getName());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel);
            window.setMinWidth(600);
            window.setMinHeight(280);
        }else if (object instanceof Birthday){
            eventSubtitleLabel.setText("Sinh nhật");
            Birthday birthday = (Birthday) object;
            if(birthday.getName().isEmpty()){
                eventTitleLabel.setText("Sinh nhật không có tiêu đề");
            }else {
                eventTitleLabel.setText(birthday.getName());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel);
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
        window.show();
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
