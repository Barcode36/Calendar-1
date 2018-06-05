package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarGridController implements Initializable {
    public GridPane calendarGridPane;
    public VBox vBox01;
    public VBox vBox11;
    public VBox vBox21;
    public VBox vBox31;
    public VBox vBox41;
    public VBox vBox51;
    public VBox vBox61;
    public VBox vBox02;
    public VBox vBox12;
    public VBox vBox22;
    public VBox vBox32;
    public VBox vBox42;
    public VBox vBox52;
    public VBox vBox62;
    public VBox vBox03;
    public VBox vBox13;
    public VBox vBox23;
    public VBox vBox33;
    public VBox vBox43;
    public VBox vBox53;
    public VBox vBox63;
    public VBox vBox04;
    public VBox vBox14;
    public VBox vBox24;
    public VBox vBox34;
    public VBox vBox44;
    public VBox vBox54;
    public VBox vBox64;
    public VBox vBox05;
    public VBox vBox15;
    public VBox vBox25;
    public VBox vBox35;
    public VBox vBox45;
    public VBox vBox55;
    public VBox vBox65;
    public Button SettingsButton;
    public ScrollPane scrollPane06;
    public ScrollPane scrollPane16;
    public ScrollPane scrollPane26;
    public ScrollPane scrollPane36;
    public ScrollPane scrollPane46;
    public ScrollPane scrollPane56;
    public ScrollPane scrollPane66;
    public AnchorPane RootPane;
    private VBox dayGrid[][];
    public VBox vBox06;
    public VBox vBox16;
    public VBox vBox26;
    public VBox vBox36;
    public VBox vBox46;
    public VBox vBox56;
    public VBox vBox66;
    public Button currentDayButton;
    public TextField yearTextField;
    public ComboBox monthComboBox;
    public Button prevYearButton;
    public Button nextYearButton;
    private CreateEventAlertBox createEventAlertBox;
    private EventDetailAlertBox eventDetailAlertBox;
    private Settings settings;
    private Calendar c = Calendar.getInstance();
    private DbConnection dbConnection;
    private AlarmModel alarmModel;
    private SimpleDateFormat HHmmFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HHmmFormatter = new SimpleDateFormat("HH:mm");
        HHmmFormatter.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
//        LoginDialog loginDialog =new LoginDialog();
//        loginDialog.display();
        SettingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                settings.display();
            }
        });

        settings = new Settings();
        createEventAlertBox = new CreateEventAlertBox();
        eventDetailAlertBox = new EventDetailAlertBox();

        dbConnection = new DbConnection();
        //DbConnection.connectToDB();
        dayGrid = new VBox[][]{
                {vBox01, vBox11, vBox21, vBox31, vBox41, vBox51, vBox61},
                {vBox02, vBox12, vBox22, vBox32, vBox42, vBox52, vBox62},
                {vBox03, vBox13, vBox23, vBox33, vBox43, vBox53, vBox63},
                {vBox04, vBox14, vBox24, vBox34, vBox44, vBox54, vBox64},
                {vBox05, vBox15, vBox25, vBox35, vBox45, vBox55, vBox65},
                {vBox06, vBox16, vBox26, vBox36, vBox46, vBox56, vBox66}
        };
        c.set(Calendar.DAY_OF_MONTH, 1);
        calendarGridPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
            }
        });
        calendarGridPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() < 0) {
                    c.add(Calendar.MONTH, 1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
                    //refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                    yearTextField.setText("" + c.get(Calendar.YEAR));
                } else if (event.getDeltaY() > 0) {
                    c.add(Calendar.MONTH, -1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
                    //refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                    yearTextField.setText("" + c.get(Calendar.YEAR));
                }
            }
        });
        currentDayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Calendar current = Calendar.getInstance();
                current.set(Calendar.DAY_OF_MONTH, 1);
                c = current;
                refreshCalendarGrid(current.getActualMaximum(Calendar.DAY_OF_MONTH), current.get(Calendar.DAY_OF_WEEK));
                monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
                yearTextField.setText("" + c.get(Calendar.YEAR));
            }
        });
        monthComboBox.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
        monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
        monthComboBox.setStyle("-fx-font: 18px \"System\";");
        monthComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.set(Calendar.MONTH, monthComboBox.getSelectionModel().getSelectedIndex());
                c.set(Calendar.DAY_OF_MONTH, 1);
                refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
            }
        });
        yearTextField.setText("" + c.get(Calendar.YEAR));
        prevYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.add(Calendar.YEAR, -1);
                c.set(Calendar.DAY_OF_MONTH, 1);
                refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                yearTextField.setText("" + c.get(Calendar.YEAR));
            }
        });
        nextYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.add(Calendar.YEAR, 1);
                c.set(Calendar.DAY_OF_MONTH, 1);
                refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                yearTextField.setText("" + c.get(Calendar.YEAR));
            }
        });

        alarmModel = new AlarmModel();
        alarmModel.start();

        OEPNewsAlarmModel oepNewsAlarmModel = new OEPNewsAlarmModel();
        oepNewsAlarmModel.start();

        OEPCourseNewsAlarmModel oepCourseNewsAlarmModel = new OEPCourseNewsAlarmModel();
        oepCourseNewsAlarmModel.start();
    }

    private void refreshCalendarGrid(int maxDay, int dayOfWeek) {
        System.out.println("refreshing");
        showLastRow(); // Hiện hàng cuối của lịch cho các tháng hiện đủ 6 dòng
        removeDayVBoxContent(); // Xóa các ngày đang hiển thị trong lịch
        removeVBoxEvent();
        int j = -1;
        switch (dayOfWeek) {
            case 1:
                j = 6;
                break;
            case 2:
                j = 0;
                break;
            case 3:
                j = 1;
                break;
            case 4:
                j = 2;
                break;
            case 5:
                j = 3;
                break;
            case 6:
                j = 4;
                break;
            case 7:
                j = 5;
                break;
        }
        int i = 0;
        int dayCount = 1;
        Calendar current = Calendar.getInstance();
        for (; i <= 5; i++) {
            for (; j <= 6; j++) {
                Label day = new Label("" + dayCount);
                VBox.setMargin(day, new Insets(5, 0, 0, 5));
                day.setPickOnBounds(false);
                if (dayCount == current.get(Calendar.DAY_OF_MONTH) && (current.get(Calendar.MONTH) == c.get(Calendar.MONTH)) && (current.get(Calendar.YEAR) == c.get(Calendar.YEAR))) {
                    day.setBackground(new Background(new BackgroundFill(Color.rgb(66, 133, 244), CornerRadii.EMPTY, Insets.EMPTY)));
                    day.setTextFill(Color.WHITE);
                }
                day.setFont(new Font("System", 18));
                dayGrid[i][j].getChildren().add(day);
                dayGrid[i][j].setUserData(dayCount);
                addMouseClickEvent(dayGrid[i][j]);

                List<OEPNews> oepnews = dbConnection.getOEPNewsList(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (oepnews != null) {
                    for (OEPNews k : oepnews) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                }

                List<Class> classes = dbConnection.getOEPCourseNewsList(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (classes != null) {
                    for (Class k : classes) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                }

                List<Holiday> holidays = dbConnection.getDayHoliday(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (holidays != null) {
                    for (Holiday k : holidays) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                }

                List<Birthday> birthdays = dbConnection.getDayBirthday(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (birthdays != null) {
                    for (Birthday k : birthdays) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                }

                List<Event> events = dbConnection.getDayEvent(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (events != null) {
                    for (Event k : events) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                }
                dayCount++;
                if (dayCount > maxDay) {
                    break;
                }
            }
            if (dayCount > maxDay)
                break;
            j = 0;
        }
        if (i == 4)
            hideLastRow();
    }

    private Label makeLabel(Object object) {
        Label label = new Label();
        label.setFont(new Font("System", 15));
        label.setPadding(new Insets(0, 0, 0, 5));
        label.setMaxWidth(Double.MAX_VALUE);
        if (object instanceof Event) {
            Event event = (Event) object;
            label.setBackground(new Background(new BackgroundFill(Color.web(event.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));
            java.util.Date eventStartTime = new java.util.Date((long) event.getStartTime() * 1000);

            if (event.getTitle().isEmpty()) {
                label.setText(HHmmFormatter.format(eventStartTime) + "  Không có tiêu đề");
            } else {
                label.setText(HHmmFormatter.format(eventStartTime) + "  " + event.getTitle());
            }
        } else if (object instanceof Holiday) {
            Holiday holiday = (Holiday) object;
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("holiday")), CornerRadii.EMPTY, Insets.EMPTY)));
            if (holiday.getName().isEmpty()) {
                label.setText("Ngày lễ không có tiêu đề");
            } else {
                label.setText(holiday.getName());
            }
        } else if (object instanceof Birthday) {
            Birthday birthday = (Birthday) object;
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("birthday")), CornerRadii.EMPTY, Insets.EMPTY)));
            if (birthday.getName().isEmpty()) {
                label.setText("Sinh nhật");
            } else {
                label.setText("Ngày sinh nhật của " + birthday.getName());
            }
        } else if (object instanceof OEPNews){
            OEPNews oepNews = (OEPNews) object;
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("oepnews")), CornerRadii.EMPTY, Insets.EMPTY)));
            java.util.Date eventStartTime = new java.util.Date((long) oepNews.getNotifyTime() * 1000);

            if (oepNews.getTitle().isEmpty()) {
                label.setText(HHmmFormatter.format(eventStartTime) + "  Không có tiêu đề");
            } else {
                label.setText(HHmmFormatter.format(eventStartTime) + "  " + oepNews.getTitle());
            }
        }else if (object instanceof Class){
            Class aClass = (Class) object;
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("oepcoursenews")), CornerRadii.EMPTY, Insets.EMPTY)));
            java.util.Date eventStartTime = new java.util.Date((long) aClass.getNotifyTime() * 1000);

            if (aClass.getTitle().isEmpty()) {
                label.setText("  Không có tiêu đề");
            } else {
                label.setText(aClass.getTitle());
            }
        }

        label.setTextFill(Color.WHITE);
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                Event resultEvent = eventDetailAlertBox.display(object, event.getSceneX(), event.getSceneY(), false);
                if (resultEvent != null) {
                    System.out.println("resultEvent: not null");
                    alarmModel.addAlarm(resultEvent);
                } else {
                    System.out.println("resultEvent: null");
                }
            }
        });


        return label;
    }

    private void removeVBoxEvent() {
        vBox01.setOnMouseClicked(null);
        vBox02.setOnMouseClicked(null);
        vBox03.setOnMouseClicked(null);
        vBox04.setOnMouseClicked(null);
        vBox05.setOnMouseClicked(null);
        vBox06.setOnMouseClicked(null);
        vBox11.setOnMouseClicked(null);
        vBox12.setOnMouseClicked(null);
        vBox13.setOnMouseClicked(null);
        vBox14.setOnMouseClicked(null);
        vBox15.setOnMouseClicked(null);
        vBox16.setOnMouseClicked(null);
        vBox21.setOnMouseClicked(null);
        vBox22.setOnMouseClicked(null);
        vBox23.setOnMouseClicked(null);
        vBox24.setOnMouseClicked(null);
        vBox25.setOnMouseClicked(null);
        vBox26.setOnMouseClicked(null);
        vBox31.setOnMouseClicked(null);
        vBox32.setOnMouseClicked(null);
        vBox33.setOnMouseClicked(null);
        vBox34.setOnMouseClicked(null);
        vBox35.setOnMouseClicked(null);
        vBox36.setOnMouseClicked(null);
        vBox41.setOnMouseClicked(null);
        vBox42.setOnMouseClicked(null);
        vBox43.setOnMouseClicked(null);
        vBox44.setOnMouseClicked(null);
        vBox45.setOnMouseClicked(null);
        vBox46.setOnMouseClicked(null);
        vBox51.setOnMouseClicked(null);
        vBox52.setOnMouseClicked(null);
        vBox53.setOnMouseClicked(null);
        vBox54.setOnMouseClicked(null);
        vBox55.setOnMouseClicked(null);
        vBox56.setOnMouseClicked(null);
        vBox61.setOnMouseClicked(null);
        vBox62.setOnMouseClicked(null);
        vBox63.setOnMouseClicked(null);
        vBox64.setOnMouseClicked(null);
        vBox65.setOnMouseClicked(null);
        vBox66.setOnMouseClicked(null);
    }

    private void addMouseClickEvent(VBox vBox) {
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                Event resultEvent = createEventAlertBox.display((int) vBox.getUserData(), c, event.getScreenX(), event.getScreenY(), false, null);
                c.set(Calendar.DAY_OF_MONTH, 1);
                //refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                if (resultEvent != null) {
                    alarmModel.addAlarm(resultEvent);
                }
            }
        });
    }

    private void removeDayVBoxContent() {
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                dayGrid[i][j].getChildren().clear();
            }
        }
    }

    private void showLastRow() {
        for (int i = 5; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                dayGrid[i][j].setVisible(true);
                dayGrid[i][j].setManaged(true);
            }
        }
        scrollPane06.setVisible(true);
        scrollPane06.setManaged(true);
        scrollPane16.setVisible(true);
        scrollPane16.setManaged(true);
        scrollPane26.setVisible(true);
        scrollPane26.setManaged(true);
        scrollPane36.setVisible(true);
        scrollPane36.setManaged(true);
        scrollPane46.setVisible(true);
        scrollPane46.setManaged(true);
        scrollPane56.setVisible(true);
        scrollPane56.setManaged(true);
        scrollPane66.setVisible(true);
        scrollPane66.setManaged(true);
        calendarGridPane.getRowConstraints().get(6).setMinHeight(calendarGridPane.getRowConstraints().get(1).getMinHeight());
        calendarGridPane.getRowConstraints().get(6).setPrefHeight(calendarGridPane.getRowConstraints().get(1).getPrefHeight());
        calendarGridPane.getRowConstraints().get(6).setMaxHeight(calendarGridPane.getRowConstraints().get(1).getMaxHeight());
    }

    private void hideLastRow() {
        for (int i = 5; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                dayGrid[i][j].setVisible(false);
                dayGrid[i][j].setManaged(false);
            }
        }

        scrollPane06.setVisible(false);
        scrollPane06.setManaged(false);
        scrollPane16.setVisible(false);
        scrollPane16.setManaged(false);
        scrollPane26.setVisible(false);
        scrollPane26.setManaged(false);
        scrollPane36.setVisible(false);
        scrollPane36.setManaged(false);
        scrollPane46.setVisible(false);
        scrollPane46.setManaged(false);
        scrollPane56.setVisible(false);
        scrollPane56.setManaged(false);
        scrollPane66.setVisible(false);
        scrollPane66.setManaged(false);

        calendarGridPane.getRowConstraints().get(6).setMinHeight(0);
        calendarGridPane.getRowConstraints().get(6).setPrefHeight(0);
        calendarGridPane.getRowConstraints().get(6).setMaxHeight(0);
    }
}