package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    public AnchorPane sampleroot;
    public Button currentDayButton;
    public TextField yearTextField;
    public ComboBox monthComboBox;
    public Button prevYearButton;
    public Button nextYearButton;
    public Button updateButton;
    public TextField mmhTextField;
    private CreateEventAlertBox createEventAlertBox;
    private EventDetailAlertBox eventDetailAlertBox;
    private Settings settings;
    private Calendar c = Calendar.getInstance();
    private DbConnection dbConnection;
    private AlarmModel alarmModel;
    private OEPNewsAlarmModel oepNewsAlarmModel;
    private OEPCourseNewsAlarmModel oepCourseNewsAlarmModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
        monthComboBox.setStyle("-fx-font: 20px \"System\";");
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
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String temp = mmhTextField.getText().trim().replaceAll(",,", ",").replaceAll(",,", ",");
                StringBuilder temp2 = new StringBuilder();
                ArrayList<String> arrayListMMH = new ArrayList<String>();
                for (int i = 0; i < temp.length(); i++) {
                    if (temp.charAt(i) == ',') {
                        arrayListMMH.add(temp2.toString());
                        temp2 = new StringBuilder();
                    } else if (i == temp.length() - 1) {
                        temp2.append(temp.charAt(temp.length() - 1));
                        arrayListMMH.add(temp2.toString());
                        temp2 = new StringBuilder();
                    } else if (!((int) (temp.charAt(i)) >= 0 && (int) (temp.charAt(i)) <= 45) && !((int) (temp.charAt(i)) >= 58 && (int) (temp.charAt(i)) <= 64)
                            && !((int) (temp.charAt(i)) >= 91 && (int) (temp.charAt(i)) <= 96) && !((int) (temp.charAt(i)) >= 123 && (int) (temp.charAt(i)) <= 127) && (int) (temp.charAt(i)) != 47)
                        temp2.append(temp.charAt(i));
                }
//                StringBuilder temp3 = new StringBuilder();
//                for (int i = 0; i < arrayListMMH.size(); i++) {
//                    temp3.append(arrayListMMH.get(i));
//                    temp3.append(" ");
//                }
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setContentText(temp3.toString());
//                alert.showAndWait();

                getUpdate(arrayListMMH);
                //getUpdateDAA();
            }
        });

        alarmModel = new AlarmModel();
        alarmModel.start();

        oepNewsAlarmModel = new OEPNewsAlarmModel();
        oepNewsAlarmModel.start();

        oepCourseNewsAlarmModel = new OEPCourseNewsAlarmModel();
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
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            java.util.Date eventStartTime = new java.util.Date((long) event.getStartTime() * 1000);
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            if (event.getTitle().isEmpty()) {
                label.setText(formatter.format(eventStartTime) + "  Không có tiêu đề");
            } else {
                label.setText(formatter.format(eventStartTime) + "  " + event.getTitle());
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

    private void getUpdate(ArrayList<String> arrayListMMH) {
        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=2");
        ArrayList<String> arrayListThongBao = new ArrayList<String>();
        ArrayList<Course> arrayListCourse = new ArrayList<Course>();
        boolean test;
        for (int i = 0; i < arrayListMMH.size(); i++) {//arrayListMMH.size(); i++) {
            for (int j = 0; j < arrayListURL.size(); j++) {
                test = false;
                try {
                    Document document = Jsoup.connect(arrayListURL.get(j)).get();
                    if (document != null) {
                        Elements elements = document.select("div.content > article");
                        for (Element element : elements) {
                            Element element2 = element.getElementsContainingText(arrayListMMH.get(i)).first();
                            if (element2 != null) {
                                String temptext = element2.text(); //getting text in the element

                                //Extract separated information in the whole text
                                int tempCBGD = temptext.indexOf("CBGD");
                                int tempKhoa = temptext.indexOf("Khoa/ Bộ môn");
                                int tempMon = temptext.indexOf("Môn học");
                                int tempLop = temptext.indexOf("Lớp");
                                int tempPhong = temptext.indexOf("Phòng");
                                int tempThoiGian = temptext.indexOf("Thời gian");
                                int tempXemThem = temptext.indexOf("Xem thêm");

                                String titletemp = temptext.substring(0, tempCBGD - 22 - 2); //CThongBao.Title
                                String timetemp = temptext.substring(tempCBGD - 22 - 1, tempCBGD - 1); //CThongBao.TimeStamp
                                //timetemp = timetemp.substring(4, timetemp.length()) + ":00";
                                String teachertemp = temptext.substring(tempCBGD, tempKhoa - 1);
                                String facultytemp = temptext.substring(tempKhoa, tempMon - 1);
                                String subjecttemp = temptext.substring(tempMon, tempLop - 1);
                                String classtemp = temptext.substring(tempLop, tempPhong - 1);
                                String roomtemp = temptext.substring(tempPhong, tempThoiGian - 1);
                                String timetemp2 = temptext.substring(tempThoiGian, tempXemThem - 1);
                                //timetemp2 = "" + timetemp2.charAt(29) + " " + timetemp2.charAt(49);

                                String[] startHour = {"07", "08", "09", "10", "10", "13", "13", "14", "15", "16"};
                                String[] startMinute = {"30", "15", "00", "00", "45", "00", "45", "30", "30", "15"};

                                String[] endHour = {"08", "09", "09", "10", "11", "13", "14", "15", "16", "17"};
                                String[] endMinute = {"15", "00", "45", "45", "30", "45", "30", "15", "15", "00"};

                                //timetemp2 = timetemp2.substring(timetemp2.length() - 10, timetemp2.length()) + " - " + startHour[Integer.parseInt("" + timetemp2.charAt(29)) - 1] + ":" + startMinute[Integer.parseInt("" + timetemp2.charAt(29)) - 1] + ":00" +
                                //         " - " + endHour[Integer.parseInt("" + timetemp2.charAt(49)) - 1] + ":" + endMinute[Integer.parseInt("" + timetemp2.charAt(49)) - 1] + ":00";

                                /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                try {
                                    Date date = dateFormat.parse(timetemp2);
                                    long dateInLong = date.getTime();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                    alert1.setContentText(Long.toString(dateInLong));
                                    alert1.showAndWait();
                                } catch (ParseException e) {

                                }*/


                                String temptext2 = titletemp + "\n" + timetemp + "\n" + teachertemp + "\n" + facultytemp + "\n" + subjecttemp + "\n" + classtemp + "\n" + roomtemp + "\n" + timetemp2;

                                for (int y = 0; y < arrayListThongBao.size(); y++) {
                                    if (arrayListThongBao.get(y).equals(temptext2)) {
                                        test = true;
                                        break;
                                    }
                                }

                                if (test == false) {
                                    arrayListThongBao.add(temptext2);
                                    //arrayListCourse.add(new Course(titletemp, timetemp, teachertemp, facultytemp, subjecttemp, classtemp, roomtemp, timetemp2));
                                }

                                /*arrayListThongBao.add(temptext2);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(temptext2);
                                alert.showAndWait();*/
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

//        for (int i = 0; i < arrayListCourse.size(); i++) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText(arrayListCourse.get(i).getDescription());
//            alert.showAndWait();
//        }
    }

    private void getUpdateDAA() {
        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=2");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=3");
        ArrayList<String> arrayListThongBao = new ArrayList<String>();
        ArrayList<String> arrayListThongBao2 = new ArrayList<String>();
        boolean test;
        for (int j = 0; j < arrayListURL.size(); j++) {
            test = false;
            try {
                Document document = Jsoup.connect(arrayListURL.get(j)).get();
                if (document != null) {
                    Elements elements = document.select("div.content > article");
                    elements = elements.select("h2 > a");
                    for (Element element : elements) {
                        Element element2 = element.getAllElements().first();
                        if (element2 != null) {
                            String temptext = element2.attr("abs:href"); //getting text in the element
                            temptext += "\n";
                            temptext += element2.text();

//                            for (int y = 0; y < arrayListThongBao.size(); y++) {
//                                if (arrayListThongBao.get(y).equals(temptext)) {
//                                    test = true;
//                                    break;
//                                }
//                            }
//
//                            if (test == false) {
//                                arrayListThongBao.add(temptext);
//                            }
                            arrayListThongBao.add(temptext);
                                /*arrayListThongBao.add(temptext2);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(temptext2);
                                alert.showAndWait();*/
                        }
                    }

                    Elements elements2 = document.select("div.content > article");
                    elements2 = elements2.select("div.submitted");
                    for (Element element : elements2) {
                        Element element2 = element.getAllElements().first();
                        if (element2 != null) {
                            String temptext = element2.text(); //getting text in the element

                            for (int y = 0; y < arrayListThongBao2.size(); y++) {
                                if (arrayListThongBao2.get(y).equals(temptext)) {
                                    test = true;
                                    break;
                                }
                            }

                            if (test == false) {
                                arrayListThongBao2.add(temptext.substring(4, temptext.length()));
                            }

                                /*arrayListThongBao.add(temptext2);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(temptext2);
                                alert.showAndWait();*/
                        }
                    }
                }
            } catch (Exception e) {

            }


            for (int i = 0; i < arrayListThongBao.size(); i++) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(arrayListThongBao.get(i) + "\n" + arrayListThongBao2.get(i));
                alert.showAndWait();
            }
        }

    }
}