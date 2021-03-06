package main;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

public class EventDetailAlertBox {
    private Stage window;
    private Rectangle2D primaryScreenBounds;
    private ImageView editImageView;
    private ImageView deleteImageView;
    private ImageView openInBrowserImageView;
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
    private DbConnection dbConnection;
    private CreateEventAlertBox createEventAlertBox;
    private Event resultEvent;

    public EventDetailAlertBox() {
        createEventAlertBox = new CreateEventAlertBox();
        dbConnection = new DbConnection();

        window = new Stage();
        window.setTitle("Chi tiết sự kiện");

        layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);
        window.setMinWidth(600);
        window.setWidth(600);
        window.setHeight(350);
        window.setMinHeight(350);

        eventTitleLabel = new Label();
        eventTitleLabel.setFont(new Font("System", 24));
        eventTitleLabel.setMaxWidth(Double.MAX_VALUE);
        eventTitleLabel.setPrefWidth(600f);
        eventTitleLabel.setPadding(new Insets(15, 8, 30, 15));
        VBox.setVgrow(eventTitleLabel, Priority.ALWAYS);
        eventTitleLabel.setTextFill(Color.WHITE);

        eventSubtitleLabel = new Label();
        eventSubtitleLabel.setFont(new Font("System", 16));
        eventSubtitleLabel.setMaxWidth(Double.MAX_VALUE);
        eventSubtitleLabel.setPadding(new Insets(10, 8, 10, 8));
        VBox.setVgrow(eventSubtitleLabel, Priority.ALWAYS);
        eventSubtitleLabel.setTextFill(Color.WHITE);

        menuBarHBox = new HBox();
        //menubar.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));
        menuBarHBox.setPadding(new Insets(20, 20, 15, 20));

        editImageView = makeImageView("edit", "Chỉnh sửa", 10, 10, 10, 10);

        deleteImageView = makeImageView("delete", "Xóa", 5, 10, 10, 10);

        openInBrowserImageView = makeImageView("openbrowser", "Mở trong trình duyệt", 5, 5, 5, 5);

        menuBarHBox.getChildren().addAll(editImageView, deleteImageView, openInBrowserImageView);

        eventTimeHBox = new HBox();
        VBox.setMargin(eventTimeHBox, new Insets(10, 0, 0, 0));
        eventTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        ImageView eventTimeImageView = makeImageView("eventtimeicon", "Thời gian",
                0, 10, 0, 8);

        eventTimeLabel = new Label();
        eventTimeLabel.setFont(new Font("System", 18));
        eventTimeLabel.setTextFill(Color.BLACK);

        eventTimeHBox.getChildren().addAll(eventTimeImageView, eventTimeLabel);

        eventNotifyTimeHBox = new HBox();
        eventNotifyTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventNotifyTimeHBox.setPadding(new Insets(0, 0, 10, 0));

        ImageView eventNotifyTimeImageView = makeImageView("notifyicon", "Thông báo lúc",
                0, 6, 0, 8);

        eventNotifyTimeLabel = new Label();
        eventNotifyTimeLabel.setPadding(new Insets(5, 0, 0, 0));
        eventNotifyTimeLabel.setFont(new Font("System", 18));
        eventNotifyTimeLabel.setTextFill(Color.BLACK);
        eventNotifyTimeHBox.getChildren().addAll(eventNotifyTimeImageView, eventNotifyTimeLabel);

        eventDescriptionHBox = new HBox();
        eventDescriptionHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventDescriptionHBox.setPadding(new Insets(5, 0, 10, 0));

        ImageView eventDescriptionImageView = makeImageView("description", "Mô tả sự kiện", 0, 10, 0, 8);

        eventDescriptionLabel = new Label();
        eventDescriptionLabel.setFont(new Font("System", 18));
        eventDescriptionLabel.setTextFill(Color.BLACK);

        eventDescriptionHBox.getChildren().addAll(eventDescriptionImageView, eventDescriptionLabel);

        layout.getChildren().add(menuBarHBox);

        Scene scene = new Scene(layout);
        window.setScene(scene);
    }

    public Event display(Object object, double x, double y, boolean notify) {
        resultEvent = null;

        layout.getChildren().remove(eventSubtitleLabel);
        layout.getChildren().remove(eventTitleLabel);
        layout.getChildren().remove(eventTimeHBox);
        layout.getChildren().remove(eventNotifyTimeHBox);
        layout.getChildren().remove(eventDescriptionHBox);

        menuBarHBox.getChildren().remove(editImageView);
        menuBarHBox.getChildren().remove(deleteImageView);
        menuBarHBox.getChildren().remove(openInBrowserImageView);

        menuBarHBox.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));
        eventTitleLabel.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));
        eventSubtitleLabel.setBackground(new Background(new BackgroundFill(Color.web(getObjectColor(object)), CornerRadii.EMPTY, Insets.EMPTY)));

        editImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.setAlwaysOnTop(false);
                Calendar calendar = Calendar.getInstance();
                resultEvent = createEventAlertBox.display(0, null, x, y, true, object);
                window.close();
            }
        });

        if (object instanceof Event) {
            menuBarHBox.getChildren().addAll(editImageView, deleteImageView);

            Event event = (Event) object;

            deleteImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setContentText("Bạn có chắc chắn muốn xóa?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        event.setIsdeleted(true);
                        resultEvent = event;
                        dbConnection.updateEvent(event);
                        window.close();
                    } else {
                        alert.close();
                    }
                }
            });

            if (event.getTitle().isEmpty()) {
                eventTitleLabel.setText("Không có tiêu đề");
            } else {
                eventTitleLabel.setText(event.getTitle());
            }

            Calendar startTimeCalendar = Calendar.getInstance();
            startTimeCalendar.setTimeInMillis(event.getStartTime() * 1000);
            Calendar endTimeCalendar = Calendar.getInstance();
            endTimeCalendar.setTimeInMillis(event.getEndTime() * 1000);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            eventTimeLabel.setText(getDateOfWeek(startTimeCalendar.get(Calendar.DAY_OF_WEEK)) + ", "
                    + formatter.format(startTimeCalendar.getTime()) + "  -  "
                    + getDateOfWeek(endTimeCalendar.get(Calendar.DAY_OF_WEEK)) + ", " + formatter.format(endTimeCalendar.getTime()));

            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(event.getNotifyTime() * 1000);
            time.add(Calendar.MILLISECOND, -time.getTimeZone().getOffset(time.getTimeInMillis()));

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
                eventNotifyTimeLabel.setText("Trước " + formatter1.format(time.getTime()) + " " + format);
            } else if (event.getNotifyTime() == -1) {
                eventNotifyTimeLabel.setText("Không thông báo");
            } else if (event.getNotifyTime() == 0) {
                eventNotifyTimeLabel.setText("Tại thời điểm sự kiện");
            }

            layout.getChildren().addAll(eventTitleLabel, eventTimeHBox, eventNotifyTimeHBox);
            window.setMinWidth(650);
            window.setMinHeight(350);

            if (!event.getDescription().isEmpty()) {
                eventDescriptionLabel.setText(event.getDescription());

                layout.getChildren().add(eventDescriptionHBox);
                window.setMinWidth(600);
                window.setMinHeight(400);
            }
        } else if (object instanceof Holiday) {
            menuBarHBox.getChildren().addAll(editImageView, deleteImageView);

            eventSubtitleLabel.setText("Ngày lễ");
            Holiday holiday = (Holiday) object;

            deleteImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setContentText("Bạn có chắc chắn muốn xóa?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        holiday.setDeleted(true);
                        dbConnection.updateHoliday(holiday);
                        window.close();
                    } else {
                        alert.close();
                    }
                    window.close();
                }
            });

            int dateid = dbConnection.getHolidayDateId(holiday.getHolidayid());
            Calendar calendar = dbConnection.getDate(dateid);
            SimpleDateFormat formatter = new SimpleDateFormat("dd");
            eventTimeLabel.setText("Ngày " + formatter.format(calendar.getTime()) + ", " + getMonthString(calendar.get(Calendar.MONTH)));
            if (holiday.getName().isEmpty()) {
                eventTitleLabel.setText("Ngày lễ không có tiêu đề");
            } else {
                eventTitleLabel.setText(holiday.getName());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel, eventTimeHBox);
            window.setMinWidth(650);
            window.setMinHeight(400);
        } else if (object instanceof Birthday) {
            menuBarHBox.getChildren().addAll(editImageView, deleteImageView);

            eventSubtitleLabel.setText("Sinh nhật");
            Birthday birthday = (Birthday) object;

            deleteImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setContentText("Bạn có chắc chắn muốn xóa?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        birthday.setDeleted(true);
                        dbConnection.updateBirthday(birthday);
                        window.close();
                    } else {
                        alert.close();
                    }
                }
            });

            int dateid = dbConnection.getBirthdayDateId(birthday.getBirthdayid());
            Calendar calendar = dbConnection.getDate(dateid);
            SimpleDateFormat formatter = new SimpleDateFormat("dd");
            eventTimeLabel.setText("Ngày " + formatter.format(calendar.getTime()) + ", " + getMonthString(calendar.get(Calendar.MONTH)));
            if (birthday.getName().isEmpty()) {
                eventTitleLabel.setText("Sinh nhật không có tiêu đề");
            } else {
                eventTitleLabel.setText(birthday.getName());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel, eventTimeHBox);
            window.setMinWidth(650);
            window.setMinHeight(350);
        } else if (object instanceof OEPNews) {
            menuBarHBox.getChildren().add(openInBrowserImageView);

            eventSubtitleLabel.setText("Thông báo chung OEP");
            OEPNews oepNews = (OEPNews) object;

            openInBrowserImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome " + oepNews.getUrl()});
                    } catch (IOException e) {

                    }
                }
            });

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(oepNews.getNotifyTime() * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
            eventTimeLabel.setText(formatter.format(calendar.getTime()));
            if (oepNews.getTitle().isEmpty()) {
                eventTitleLabel.setText("Không có tiêu đề");
            } else {
                eventTitleLabel.setText(oepNews.getTitle());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel, eventTimeHBox);
            window.setMinWidth(650);
            window.setMinHeight(400);
            window.setHeight(400);
        } else if (object instanceof CTSVNews) {
            menuBarHBox.getChildren().add(openInBrowserImageView);

            eventSubtitleLabel.setText("Thông báo CTSV");
            CTSVNews ctsvNews = (CTSVNews) object;

            openInBrowserImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome " + ctsvNews.getUrl()});
                    } catch (IOException e) {

                    }
                }
            });

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ctsvNews.getNotifyTime() * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
            eventTimeLabel.setText(formatter.format(calendar.getTime()));
            if (ctsvNews.getTitle().isEmpty()) {
                eventTitleLabel.setText("Không có tiêu đề");
            } else {
                eventTitleLabel.setText(ctsvNews.getTitle());
            }
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel, eventTimeHBox);
            window.setMinWidth(650);
            window.setMinHeight(400);
            window.setHeight(400);
        } else if (object instanceof Class) {
            eventSubtitleLabel.setText("Thông báo nghỉ, học bù OEP");
            Class aClass = (Class) object;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (aClass.getTitle().isEmpty()) {
                eventTitleLabel.setText("Không có tiêu đề");
            } else {
                eventTitleLabel.setText(aClass.getTitle());
            }
            calendar.setTimeInMillis(aClass.getCourseTime() * 1000);
            eventDescriptionLabel.setText("Giáo viên: " + aClass.getTeacher() + "\n" + "Khoa/Bộ môn: " + aClass.getFaculty() + "\n" + "Môn học: " + aClass.getSubject() + "\n" + "Lớp: " + aClass.getClassid() + "\n" + "Phòng: " + aClass.getRoom() + "\n" + "Tiết bắt đầu: " + aClass.getStartTime() + "\n" + "Tiết kết thúc: " + aClass.getEndTime() + "\n" + "ngày " + formatter.format(calendar.getTime()));
            layout.getChildren().addAll(eventSubtitleLabel, eventTitleLabel, eventDescriptionHBox);
            window.setMinWidth(650);
            window.setMinHeight(450);
            window.setHeight(450);
        }

        if (!notify) {
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
        } else {
            window.setY(y);
            window.setX(x);
            window.setAlwaysOnTop(true);
        }
        window.showAndWait();
        return resultEvent;
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

    private String getDateOfWeek(int day) {
        switch (day) {
            case 1:
                return "Chủ nhật";
            case 2:
                return "Thứ hai";
            case 3:
                return "Thứ ba";
            case 4:
                return "Thứ tư";
            case 5:
                return "Thứ năm";
            case 6:
                return "Thứ sáu";
            case 7:
                return "Thứ bảy";
        }
        return "";
    }

    private String getMonthString(int month) {
        switch (month) {
            case 0:
                return "Tháng 1";
            case 1:
                return "Tháng 2";
            case 2:
                return "Tháng 3";
            case 3:
                return "Tháng 4";
            case 4:
                return "Tháng 5";
            case 5:
                return "Tháng 6";
            case 6:
                return "Tháng 7";
            case 7:
                return "Tháng 8";
            case 8:
                return "Tháng 9";
            case 9:
                return "Tháng 10";
            case 10:
                return "Tháng 11";
            case 11:
                return "Tháng 12";
        }
        return "";
    }

    private String getObjectColor(Object object) {
        if (object instanceof Event) {
            Event event = (Event) object;
            return event.getColor();
        } else if (object instanceof Holiday) {
            return dbConnection.getDefaultColor("holiday");
        } else if (object instanceof Birthday) {
            return dbConnection.getDefaultColor("birthday");
        } else if (object instanceof OEPNews) {
            return dbConnection.getDefaultColor("oepnews");
        } else if (object instanceof CTSVNews) {
            return dbConnection.getDefaultColor("ctsvnews");
        } else if (object instanceof Class) {
            return dbConnection.getDefaultColor("oepcoursenews");
        }
        return "";
    }

    public double getStageWidth() {
        return window.getWidth();
    }

    public double getStageHeight() {
        return window.getHeight();
    }
}
