package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {
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
    private VBox dayGrid[][];
    public VBox vBox06;
    public VBox vBox16;
    public VBox vBox26;
    public VBox vBox36;
    public VBox vBox46;
    public VBox vBox56;
    public VBox vBox66;
    public VBox eventDetailDialog;
    public AnchorPane sampleroot;
    public Button currentDayButton;
    public TextField yearTextField;
    public ComboBox monthComboBox;
    public Button prevYearButton;
    public Button nextYearButton;
    public Button updateButton;
    public TextField mmhTextField;
    private MediaPlayer mediaPlayer;
    private Calendar c = Calendar.getInstance();
    private List<Event> monthEvent;
    private List<Holiday> monthHoliday;
    private List<Birthday> monthBirthday;
    private DbConnection dbConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
        calendarGridPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                //monthEvent = new ArrayList<Event>();
                if (event.getDeltaY() < 0) {
                    c.add(Calendar.MONTH, 1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
                    refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                    yearTextField.setText("" + c.get(Calendar.YEAR));
                } else if (event.getDeltaY() > 0) {
                    c.add(Calendar.MONTH, -1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthComboBox.getSelectionModel().select(c.get(Calendar.MONTH));
                    refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
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
                /*StringBuilder temp3 = new StringBuilder();
                for (int i = 0; i < arrayListMMH.size(); i++) {
                    temp3.append(arrayListMMH.get(i));
                    temp3.append(" ");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(temp3.toString());
                alert.showAndWait();*/

                getUpdate(arrayListMMH);
                //getUpdateDAA();
            }
        });
        //Utils.playSound("alarm.mp3");
        initDayVBoxEvent();

    }

    public void refreshCalendarGrid(int maxDay, int dayOfWeek) {
        monthEvent = new ArrayList<Event>();
        monthHoliday = new ArrayList<Holiday>();
        monthBirthday = new ArrayList<Birthday>();
        showLastRow(); // Hiện hàng cuối của lịch cho các tháng hiện đủ 6 dòng
        removeDayVBoxContent(); // Xóa các ngày đang hiển thị trong lịch
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
                day.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        //EventDetailAlertBox.display("s", 1524934800L, 1522849860L, "This is a description", 8640L, event.getScreenX(), event.getScreenY());
                    }
                });
                dayGrid[i][j].getChildren().add(day);
                dayGrid[i][j].setUserData(dayCount);

                List<Holiday> holidays = dbConnection.getDayHoliday(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (holidays != null) {
                    for (Holiday k : holidays) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                    monthHoliday.addAll(holidays);
                }

                List<Birthday> birthdays = dbConnection.getDayBirthday(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (birthdays != null) {
                    for (Birthday k : birthdays) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                    monthBirthday.addAll(birthdays);
                }

                List<Event> events = dbConnection.getDayEvent(dayCount, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                if (events != null) {
                    for (Event k : events) {
                        Label label = makeLabel(k);
                        VBox.setMargin(label, new Insets(0, 0, 2, 0));
                        dayGrid[i][j].getChildren().add(label);
                    }
                    monthEvent.addAll(events);
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
        if (object instanceof Event) {
            Event event = (Event) object;
            //label.setUserData(event.getEventid());
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
            //label.setUserData(holiday.getDateid());
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("holiday")), CornerRadii.EMPTY, Insets.EMPTY)));
            if (holiday.getName().isEmpty()) {
                label.setText("Ngày lễ không có tiêu đề");
            } else {
                label.setText(holiday.getName());
            }
        } else if (object instanceof Birthday) {
            Birthday birthday = (Birthday) object;
            //label.setUserData(birthday.getDateid());
            label.setBackground(new Background(new BackgroundFill(Color.web(dbConnection.getDefaultColor("birthday")), CornerRadii.EMPTY, Insets.EMPTY)));
            if (birthday.getName().isEmpty()) {
                label.setText("Sinh nhật");
            } else {
                label.setText(birthday.getName());
            }
        }

        label.setTextFill(Color.WHITE);
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                EventDetailAlertBox eventDetailAlertBox = new EventDetailAlertBox();
                eventDetailAlertBox.display(object, event.getSceneX(), event.getSceneY());
            }
        });


        return label;
    }

    private Event getEventFromList(int eventID) {
        for (Event i : monthEvent) {
            if (i.getEventid() == eventID)
                return i;
        }
        return null;
    }


    private void initDayVBoxEvent() {
        addMouseClickEvent(vBox01);
        addMouseClickEvent(vBox11);
        addMouseClickEvent(vBox21);
        addMouseClickEvent(vBox31);
        addMouseClickEvent(vBox41);
        addMouseClickEvent(vBox51);
        addMouseClickEvent(vBox61);
        addMouseClickEvent(vBox02);
        addMouseClickEvent(vBox12);
        addMouseClickEvent(vBox22);
        addMouseClickEvent(vBox32);
        addMouseClickEvent(vBox42);
        addMouseClickEvent(vBox52);
        addMouseClickEvent(vBox62);
        addMouseClickEvent(vBox03);
        addMouseClickEvent(vBox13);
        addMouseClickEvent(vBox23);
        addMouseClickEvent(vBox33);
        addMouseClickEvent(vBox43);
        addMouseClickEvent(vBox53);
        addMouseClickEvent(vBox63);
        addMouseClickEvent(vBox04);
        addMouseClickEvent(vBox14);
        addMouseClickEvent(vBox24);
        addMouseClickEvent(vBox34);
        addMouseClickEvent(vBox44);
        addMouseClickEvent(vBox54);
        addMouseClickEvent(vBox64);
        addMouseClickEvent(vBox05);
        addMouseClickEvent(vBox15);
        addMouseClickEvent(vBox25);
        addMouseClickEvent(vBox35);
        addMouseClickEvent(vBox45);
        addMouseClickEvent(vBox55);
        addMouseClickEvent(vBox65);
        addMouseClickEvent(vBox06);
        addMouseClickEvent(vBox16);
        addMouseClickEvent(vBox26);
        addMouseClickEvent(vBox36);
        addMouseClickEvent(vBox46);
        addMouseClickEvent(vBox56);
        addMouseClickEvent(vBox66);
    }

    private void addMouseClickEvent(Node node) {
        if (node.getClass().getName().equals(VBox.class.getName())) {
            final VBox vBox = (VBox) node;
            if (vBox.getChildren().isEmpty())
                return;
            else {
                node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        event.consume();
                        CreateEventAlertBox createEventAlertBox = new CreateEventAlertBox();
                        boolean result = createEventAlertBox.display((int) node.getUserData(), c, event.getScreenX(), event.getScreenY(), false);
                        System.out.println("result: " + result);
                        if (result) {
                            c.set(Calendar.DAY_OF_MONTH, 1);
                            refreshCalendarGrid(c.getActualMaximum(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK));
                        }
                    }
                });
            }
        }
    }

    public void playAlarm() {
        Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/" + "resources/alarm.mp3");
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    private void removeDayVBoxContent() {
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                dayGrid[i][j].getChildren().clear();
            }
        }
    }

//    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
//        Node result = null;
//        ObservableList<Node> childrens = gridPane.getChildren();
//
//        for (Node node : childrens) {
//            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
//                result = node;
//                break;
//            }
//        }
//
//        return result;
//    }

    private void showLastRow() {
        for (int i = 5; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                dayGrid[i][j].setVisible(true);
                dayGrid[i][j].setManaged(true);
            }
        }
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
                                timetemp = timetemp.substring(4, 14);
                                String teachertemp = temptext.substring(tempCBGD, tempKhoa - 1);
                                String facultytemp = temptext.substring(tempKhoa, tempMon - 1);
                                String subjecttemp = temptext.substring(tempMon, tempLop - 1);
                                String classtemp = temptext.substring(tempLop, tempPhong - 1);
                                String roomtemp = temptext.substring(tempPhong, tempThoiGian - 1);
                                String timetemp2 = temptext.substring(tempThoiGian, tempXemThem - 1);

                                String temptext2 = titletemp + "\n" + timetemp + "\n" + teachertemp + "\n" + facultytemp + "\n" + subjecttemp + "\n" + classtemp + "\n" + roomtemp + "\n" + timetemp2;

                                for (int y = 0; y < arrayListThongBao.size(); y++) {
                                    if (arrayListThongBao.get(y).equals(temptext2)) {
                                        test = true;
                                        break;
                                    }
                                }

                                if (test == false) {
                                    arrayListThongBao.add(temptext2);
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

        for (int i = 0; i < arrayListThongBao.size(); i++) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(arrayListThongBao.get(i));
            alert.showAndWait();
        }
    }

    private void getUpdateDAA() {
        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=2");
        ArrayList<String> arrayListThongBao = new ArrayList<String>();
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

                            for (int y = 0; y < arrayListThongBao.size(); y++) {
                                if (arrayListThongBao.get(y).equals(temptext)) {
                                    test = true;
                                    break;
                                }
                            }

                            if (test == false) {
                                arrayListThongBao.add(temptext);
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
                alert.setContentText(arrayListThongBao.get(i));
                alert.showAndWait();
            }
        }

    }
}

