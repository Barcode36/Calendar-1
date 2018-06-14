package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


public class CreateEventAlertBox {
    private int day;
    private Calendar calendar;
    private String oldStartTime;
    private String oldEndTime;
    private boolean isChanged = false;

    private DatePicker startDatePicker;
    private TextField startTimeTextField;
    private DatePicker endDatePicker;
    private TextField endTimeTextField;
    private ComboBox notifyTimeComboBox;
    private TextField notifyTimeTextField;
    private ComboBox notifyTimeUnitComboBox;
    private TextArea eventDescriptionTextArea;
    private ColorPicker colorPicker;
    private TextField titleTextField;
    private ComboBox<String> optionCombobox;
    private ComboBox<Alarm> alarmComboBox;
    private Button acceptButton;

    private HBox eventTimeHBox;
    private HBox eventNotifyTimeHBox;
    private HBox eventDescriptionHBox;
    private HBox eventColorHBox;
    private HBox eventAlarmHBox;
    private HBox controlHBox;
    private VBox layout;

    private Stage window;
    private Rectangle2D primaryScreenBounds;

    private Event resultEvent = null;
    private SimpleDateFormat timeFormat;

    private DbConnection dbConnection;

    public CreateEventAlertBox() {
        timeFormat = new SimpleDateFormat("HH:mm");
        dbConnection = new DbConnection();
        window = new Stage();
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        titleTextField = new TextField();
        titleTextField.setFont(new Font("Calibri", 32));
        titleTextField.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(titleTextField, Priority.ALWAYS);
        VBox.setMargin(titleTextField, new Insets(20, 20, 10, 20));
        titleTextField.setPromptText("Thêm tiêu đề");
        titleTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                isChanged = true;
            }
        });

        ImageView eventTimeImageView = makeImageView("eventtimeicon", "Thời gian", 30, 10, 0, 8);

        HBox eventStartTimeHBox = makeStartTimeHBox();

        HBox eventEndTimeHBox = makeEndTimeHBox();

        VBox eventTimeVBox = new VBox();
        eventTimeVBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventTimeVBox.setPadding(new Insets(0, 0, 10, 0));
        eventTimeVBox.getChildren().addAll(eventStartTimeHBox, eventEndTimeHBox);

        eventTimeHBox = new HBox();
        eventTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventTimeHBox.setPadding(new Insets(0, 0, 10, 0));
        eventTimeHBox.getChildren().addAll(eventTimeImageView, eventTimeVBox);

        eventNotifyTimeHBox = makeEventNotifyTimeHBox();

        eventDescriptionHBox = makeEventDescriptionHBox();

        eventColorHBox = makeColorChooseHBox();

        eventAlarmHBox = makeEventAlarmHBox();

        acceptButton = new Button();
        acceptButton.setFont(new Font("System", 20));
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (optionCombobox.getSelectionModel().getSelectedItem().equals("Sự kiện")) {
                    if (acceptButton.getText().equals("Thêm")) {
                        if (addNewEvent()) {
                            isChanged = false;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Xác nhận");
                            alert.setContentText("Thêm thành công!");
                            alert.showAndWait();
                            window.close();
                        }
                    } else if (acceptButton.getText().equals("Cập nhật")) {
                        if (updateEvent()) {
                            isChanged = false;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Xác nhận");
                            alert.setContentText("Cập nhật thành công!");
                            alert.showAndWait();
                            window.close();
                        }
                    }
                } else if (optionCombobox.getSelectionModel().getSelectedItem().equals("Ngày sinh nhật")) {
                    if (acceptButton.getText().equals("Thêm")) {
                        if (addNewBirthday(day, calendar.get(Calendar.MONTH))) {
                            isChanged = false;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Xác nhận");
                            alert.setContentText("Thêm thành công!");
                            alert.showAndWait();
                            window.close();
                        }
                    }else if (acceptButton.getText().equals("Cập nhật")){
                        if (updateBirthday()) {
                            isChanged = false;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Xác nhận");
                            alert.setContentText("Cập nhật thành công!");
                            alert.showAndWait();
                            window.close();
                        }
                    }
                } else if (optionCombobox.getSelectionModel().getSelectedItem().equals("Ngày lễ")) {
//                    if (acceptButton.getText().equals("Thêm")) {
//                        if (addNewHoliday(day, calendar.get(Calendar.MONTH))) {
//                            isChanged = false;
//                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                            alert.setTitle("Xác nhận");
//                            alert.setContentText("Thêm thành công!");
//                            alert.showAndWait();
//                            window.close();
//                        }
//                    }else if (acceptButton.getText().equals("Cập nhật")){
//                        if (updateHoliday()) {
//                            isChanged = false;
//                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                            alert.setTitle("Xác nhận");
//                            alert.setContentText("Cập nhật thành công!");
//                            alert.showAndWait();
//                            window.close();
//                        }
//                    }
                }
            }
        });

        controlHBox = new HBox();
        controlHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        controlHBox.setPadding(new Insets(0, 20, 10, 10));
        controlHBox.setAlignment(Pos.CENTER_RIGHT);
        VBox.setVgrow(controlHBox, Priority.ALWAYS);
        controlHBox.getChildren().addAll(acceptButton);

        layout = new VBox(0);
        layout.setPadding(new Insets(0, 0, 20, 0));
        layout.setMaxWidth(600);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(titleTextField, eventTimeHBox, eventNotifyTimeHBox, eventDescriptionHBox, eventColorHBox, eventAlarmHBox, controlHBox);

        ObservableList<String> data = FXCollections.observableArrayList("Sự kiện", "Ngày lễ", "Ngày sinh nhật");
        optionCombobox = new ComboBox<>(data);
        optionCombobox.getSelectionModel().selectFirst();
        VBox.setMargin(optionCombobox, new Insets(0, 0, 0, 10));
        optionCombobox.setStyle("-fx-font: 20px \"System\";");
        optionCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Ngày lễ")) {
                    layout.getChildren().remove(eventTimeHBox);
                    layout.getChildren().remove(eventNotifyTimeHBox);
                    layout.getChildren().remove(eventDescriptionHBox);
                    layout.getChildren().remove(eventColorHBox);
                    layout.getChildren().remove(eventAlarmHBox);
                    layout.getChildren().remove(controlHBox);
                    layout.getChildren().add(controlHBox);
                    titleTextField.setPromptText("Nhập tên ngày lễ");
                    window.setMinHeight(250f);
                    window.setMaxHeight(250f);
                } else if (newValue.equals("Ngày sinh nhật")) {
                    layout.getChildren().remove(eventTimeHBox);
                    layout.getChildren().remove(eventNotifyTimeHBox);
                    layout.getChildren().remove(eventDescriptionHBox);
                    layout.getChildren().remove(eventColorHBox);
                    layout.getChildren().remove(eventAlarmHBox);
                    layout.getChildren().remove(controlHBox);
                    layout.getChildren().add(controlHBox);
                    titleTextField.setPromptText("Ngày sinh nhật của");
                    window.setMinHeight(250f);
                    window.setMaxHeight(250f);
                } else if (newValue.equals("Sự kiện")) {
                    layout.getChildren().remove(controlHBox);
                    layout.getChildren().addAll(eventTimeHBox, eventNotifyTimeHBox, eventDescriptionHBox, eventColorHBox, eventAlarmHBox);
                    layout.getChildren().add(controlHBox);
                    titleTextField.setPromptText("Thêm tiêu đề");
                    window.setMinHeight(620f);
                    window.setMaxHeight(1000f);
                }
            }
        });
        layout.getChildren().add(0, optionCombobox);

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (isChanged) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setContentText("Sự kiện chưa được lưu, bạn có muốn hủy sự kện?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        window.close();
                    } else {
                        alert.close();
                        event.consume();
                    }
                }
            }
        });

        Scene scene = new Scene(layout);
        window.setMinHeight(610f);
        window.setScene(scene);
    }

    public Event display(int day, Calendar c, double x, double y, boolean update, Object object) {
        resultEvent = null;
        titleTextField.clear();
        eventDescriptionTextArea.clear();
        optionCombobox.setDisable(false);
        isChanged = false;

        if (update) {
            window.setTitle("Cập nhật sự kiện");
            acceptButton.setText("Cập nhật");
        } else {
            window.setTitle("Thêm sự kiện mới");
            acceptButton.setText("Thêm");
        }

        if (object != null) {
            optionCombobox.setDisable(true);
            if (object instanceof Event) {
                optionCombobox.getSelectionModel().select("Sự kiện");
                Event event = (Event) object;
                titleTextField.setUserData(event.getEventid());
                if (!event.getTitle().isEmpty())
                    titleTextField.setText(event.getTitle());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Calendar eventTime = Calendar.getInstance();

                eventTime.setTimeInMillis(event.getStartTime() * 1000);
                startDatePicker.setValue(LocalDate.of(eventTime.get(Calendar.YEAR), eventTime.get(Calendar.MONTH) + 1, eventTime.get(Calendar.DATE)));
                startTimeTextField.setText(simpleDateFormat.format(eventTime.getTime()));

                eventTime.setTimeInMillis(event.getEndTime() * 1000);
                endDatePicker.setValue(LocalDate.of(eventTime.get(Calendar.YEAR), eventTime.get(Calendar.MONTH) + 1, eventTime.get(Calendar.DATE)));
                endTimeTextField.setText(simpleDateFormat.format(eventTime.getTime()));

                if (event.getNotifyTime().equals(-1)) {
                    notifyTimeComboBox.getSelectionModel().select("Không thông báo");
                } else if (event.getNotifyTime().equals(0L)) {
                    notifyTimeComboBox.getSelectionModel().select("Tại thời điểm sự kiện");
                } else if (event.getNotifyTime().equals(10 * 60L)) {
                    notifyTimeComboBox.getSelectionModel().select("Trướo 10 phút");
                } else if (event.getNotifyTime().equals(30 * 60L)) {
                    notifyTimeComboBox.getSelectionModel().select("Trước 30 phút");
                } else {
                    notifyTimeComboBox.getSelectionModel().select("Tùy chọn");
                    notifyTimeTextField.setText("" + event.getNotifyTime() / 60L);
                    notifyTimeUnitComboBox.getSelectionModel().select("phút");
                }

                if (!event.getDescription().isEmpty()) {
                    eventDescriptionTextArea.setText(event.getDescription());
                }

                colorPicker.setValue(Color.valueOf(event.getColor()));

                Alarm alarm = dbConnection.getAlarm(event.getAlarmID());
                alarmComboBox.getSelectionModel().select(alarm);
            } else if (object instanceof Holiday) {
                optionCombobox.getSelectionModel().select("Ngày lễ");
                Holiday holiday = (Holiday) object;
                titleTextField.setUserData(holiday.getHolidayid());
                if (!holiday.getName().isEmpty()) {
                    titleTextField.setText(holiday.getName());
                }
            } else if (object instanceof Birthday) {
                optionCombobox.getSelectionModel().select("Ngày sinh nhật");
                Birthday birthday = (Birthday) object;
                titleTextField.setUserData(birthday.getBirthdayid());
                if (!birthday.getName().isEmpty()) {
                    titleTextField.setText(birthday.getName());
                }
            }
        } else {
            this.day = day;
            this.calendar = c;
            startDatePicker.setValue(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day));
            endDatePicker.setValue(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day));
            Calendar currentTime = Calendar.getInstance();
            startTimeTextField.setText(timeFormat.format(currentTime.getTime()));
            currentTime.add(Calendar.MINUTE, 30);
            endTimeTextField.setText(timeFormat.format(currentTime.getTime()));
            Alarm alarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("event"));
            alarmComboBox.getSelectionModel().select(alarm);
        }

        window.setMinWidth(610);

        if (primaryScreenBounds.getWidth() - x < window.getMinWidth()) {
            if (primaryScreenBounds.getHeight() - y < window.getMinHeight()) {
                window.setY(Math.abs(y - window.getMinHeight() + 30));
                window.setX(x - window.getMinWidth());
            } else {
                window.setY(y);
                window.setX(x - window.getMinWidth());
            }
        } else {
            if (primaryScreenBounds.getHeight() - y < window.getMinHeight()) {
                window.setY(Math.abs(y - window.getMinHeight() + 30));
                window.setX(x);
            } else {
                window.setY(y);
                window.setX(x);
            }
        }

        window.showAndWait();
        return resultEvent;
    }

    private boolean addNewEvent() {
        Event newEvent = new Event();
        if (!titleTextField.getText().isEmpty()) {
            newEvent.setTitle(titleTextField.getText());
        } else {
            newEvent.setTitle("");
        }

        if (!eventDescriptionTextArea.getText().isEmpty()) {
            newEvent.setDescription(eventDescriptionTextArea.getText());
        } else {
            newEvent.setDescription("");
        }

        LocalDate localDate = startDatePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        String[] hourAndMinute = startTimeTextField.getText().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
        newEvent.setStartTime(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis() / 1000);

        localDate = endDatePicker.getValue();
        instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        date = Date.from(instant);
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        hourAndMinute = endTimeTextField.getText().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
        newEvent.setEndTime(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis() / 1000);

        if (newEvent.getStartTime() > newEvent.getEndTime()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Thời gian bắt đầu sự kiện phải trước thời gian kết thúc! ", ButtonType.OK);
            alert.setTitle("Lỗi");
            alert.showAndWait();
            return false;
        }

        String selection = notifyTimeComboBox.getSelectionModel().getSelectedItem().toString();
        if (selection.equals("Không thông báo")) {
            newEvent.setNotifyTime(-1L);
        } else if (selection.equals("Tại thời điểm sự kiện")) {
            newEvent.setNotifyTime(0L);
        } else if (selection.equals("Trước 10 phút")) {
            newEvent.setNotifyTime(10 * 60L);
        } else if (selection.equals("Trước 30 phút")) {
            newEvent.setNotifyTime(30 * 60L);
        } else if (selection.equals("Tùy chọn")) {
            if (notifyTimeTextField.getText().isEmpty()) {
                Alert noinput = new Alert(Alert.AlertType.WARNING);
                noinput.setTitle("Cảnh báo");
                noinput.setContentText("Bạn chưa nhập thời gian thông báo tùy chọn");
                return false;
            } else {
                String unitSelection = notifyTimeUnitComboBox.getSelectionModel().getSelectedItem().toString();
                if (unitSelection.equals("phút")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 60);
                } else if (unitSelection.equals("giờ")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 60 * 60);
                } else if (unitSelection.equals("ngày")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 24 * 60 * 60);
                }
            }
        }

        if ((newEvent.getStartTime() - newEvent.getNotifyTime()) < (System.currentTimeMillis() / 1000))
            newEvent.setIsnotified(true);

        newEvent.setColor(Utils.toRGBCode(colorPicker.getValue()));

        newEvent.setAlarmID(alarmComboBox.getSelectionModel().getSelectedItem().getAlarmid());
        calendar.setTimeInMillis(newEvent.getStartTime()*1000);
        boolean result = dbConnection.addEventToYearEventTable(newEvent, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        if (result) {
            resultEvent = dbConnection.getLastEvent(year);
        }
        return result;
    }

    private boolean updateEvent() {
        Event newEvent = new Event();
        newEvent.setEventid((int) titleTextField.getUserData());
        if (!titleTextField.getText().isEmpty()) {
            newEvent.setTitle(titleTextField.getText());
        } else {
            newEvent.setTitle("");
        }

        if (!eventDescriptionTextArea.getText().isEmpty()) {
            newEvent.setDescription(eventDescriptionTextArea.getText());
        } else {
            newEvent.setDescription("");
        }

        LocalDate localDate = startDatePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        String[] hourAndMinute = startTimeTextField.getText().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
        newEvent.setStartTime(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis() / 1000);

        localDate = endDatePicker.getValue();
        instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        date = Date.from(instant);
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        hourAndMinute = endTimeTextField.getText().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
        newEvent.setEndTime(calendar.getTimeInMillis() / 1000);
        System.out.println(calendar.getTimeInMillis() / 1000);

        if (newEvent.getStartTime() > newEvent.getEndTime()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Thời gian bắt đầu sự kiện phải trước thời gian kết thúc! ", ButtonType.OK);
            alert.setTitle("Lỗi");
            alert.showAndWait();
            return false;
        }

        String selection = notifyTimeComboBox.getSelectionModel().getSelectedItem().toString();
        if (selection.equals("Không thông báo")) {
            newEvent.setNotifyTime(-1L);
        } else if (selection.equals("Tại thời điểm sự kiện")) {
            newEvent.setNotifyTime(0L);
        } else if (selection.equals("Trước 10 phút")) {
            newEvent.setNotifyTime(10 * 60L);
        } else if (selection.equals("Trước 30 phút")) {
            newEvent.setNotifyTime(30 * 60L);
        } else if (selection.equals("Tùy chọn")) {
            if (notifyTimeTextField.getText().isEmpty()) {
                Alert noinput = new Alert(Alert.AlertType.WARNING);
                noinput.setTitle("Cảnh báo");
                noinput.setContentText("Bạn chưa nhập thời gian thông báo tùy chọn");
                return false;
            } else {
                String unitSelection = notifyTimeUnitComboBox.getSelectionModel().getSelectedItem().toString();
                if (unitSelection.equals("phút")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 60);
                } else if (unitSelection.equals("giờ")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 60 * 60);
                } else if (unitSelection.equals("ngày")) {
                    newEvent.setNotifyTime(Long.parseLong(notifyTimeTextField.getText()) * 24 * 60 * 60);
                }
            }
        }

        if ((newEvent.getStartTime() - newEvent.getNotifyTime()) < (System.currentTimeMillis() / 1000))
            newEvent.setIsnotified(true);

        newEvent.setColor(Utils.toRGBCode(colorPicker.getValue()));

        newEvent.setAlarmID(alarmComboBox.getSelectionModel().getSelectedItem().getAlarmid());

        boolean result = dbConnection.updateEvent(newEvent);
        resultEvent = newEvent;
        return result;
    }

    private boolean addNewBirthday(int day, int month) {
        Birthday newBirthday = new Birthday();
        if (!titleTextField.getText().isEmpty()) {
            newBirthday.setName(titleTextField.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên! ", ButtonType.OK);
            alert.setTitle("Lỗi");
            alert.showAndWait();
            return false;
        }
        return dbConnection.addBirthday(newBirthday, day, month);
    }

    private boolean updateBirthday() {
        Birthday newBirthday = new Birthday();
        newBirthday.setBirthdayid((int) titleTextField.getUserData());
        if (!titleTextField.getText().isEmpty()) {
            newBirthday.setName(titleTextField.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên!", ButtonType.OK);
            alert.setTitle("Lỗi");
            alert.showAndWait();
            return false;
        }
        return dbConnection.updateBirthday(newBirthday);
    }

//    private boolean addNewHoliday(int day, int month) {
//        Holiday newHoliday = new Holiday();
//        if (!titleTextField.getText().isEmpty()) {
//            newHoliday.setName(titleTextField.getText());
//        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên ngày lễ! ", ButtonType.OK);
//            alert.setTitle("Lỗi");
//            alert.showAndWait();
//            return false;
//        }
//        return dbConnection.addHoliday(newHoliday, day, month);
//    }

//    private boolean updateHoliday() {
//        Holiday newHoliday = new Holiday();
//        newHoliday.setHolidayid((int) titleTextField.getUserData());
//        if (!titleTextField.getText().isEmpty()) {
//            newHoliday.setName(titleTextField.getText());
//        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên!", ButtonType.OK);
//            alert.setTitle("Lỗi");
//            alert.showAndWait();
//            return false;
//        }
//        return dbConnection.updateHoliday(newHoliday);
//    }

    private HBox makeStartTimeHBox() {
        Label start = new Label("Từ");
        HBox.setMargin(start, new Insets(8, 32, 0, 5));
        start.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        start.setStyle("-fx-font: 18px \"System\";");

        startDatePicker = makeDatePicker();

        ComboBox<Time> startTimeComboBox = makeTimeComboBox();

        startTimeTextField = new TextField();
        startTimeTextField.setMaxWidth(100);
        startTimeTextField.setFont(new Font("System", 18));
        Calendar current = Calendar.getInstance();
        oldStartTime = (current.get(Calendar.HOUR_OF_DAY) >= 10 ? "" + current.get(Calendar.HOUR_OF_DAY) : "0" + current.get(Calendar.HOUR_OF_DAY))
                + ":" +
                (current.get(Calendar.MINUTE) >= 10 ? "" + current.get(Calendar.MINUTE) : "0" + current.get(Calendar.MINUTE));
        startTimeTextField.setText(oldStartTime);
        startTimeTextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {//when focus lost
                startTimeComboBox.setVisible(false);
                if (!startTimeTextField.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
                    startTimeTextField.setText(oldStartTime);
                } else {
                    oldStartTime = startTimeTextField.getText();
                }
            } else if (newValue) {
                startTimeComboBox.setVisible(true);
            }
        });

        startTimeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                oldStartTime = startTimeComboBox.getSelectionModel().getSelectedItem().getTime();
                startTimeTextField.setText(oldStartTime);
            }
        });

        HBox eventStartTimeHBox = new HBox();
        eventStartTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventStartTimeHBox.setPadding(new Insets(0, 0, 10, 0));
        eventStartTimeHBox.getChildren().addAll(start, startDatePicker, startTimeTextField, startTimeComboBox);

        return eventStartTimeHBox;
    }

    private HBox makeEndTimeHBox() {
        Label end = new Label("Đến");
        HBox.setMargin(end, new Insets(8, 20, 0, 5));
        end.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        end.setStyle("-fx-font: 18px \"System\";");

        endDatePicker = makeDatePicker();

        ComboBox<Time> endTimeComboBox = makeTimeComboBox();

        endTimeTextField = new TextField();
        endTimeTextField.setMaxWidth(100);
        endTimeTextField.setFont(new Font("System", 18));
        Calendar current = Calendar.getInstance();
        current.add(Calendar.MINUTE, 30);
        oldEndTime = (current.get(Calendar.HOUR_OF_DAY) >= 10 ? "" + current.get(Calendar.HOUR_OF_DAY) : "0" + current.get(Calendar.HOUR_OF_DAY))
                + ":" +
                (current.get(Calendar.MINUTE) >= 10 ? "" + current.get(Calendar.MINUTE) : "0" + current.get(Calendar.MINUTE));
        endTimeTextField.setText(oldEndTime);
        endTimeTextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {//when focus lost
                endTimeComboBox.setVisible(false);
                if (!endTimeTextField.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
                    endTimeTextField.setText(oldEndTime);
                } else {
                    oldEndTime = endTimeTextField.getText();
                }
            } else if (newValue) {
                endTimeComboBox.setVisible(true);
            }
        });

        endTimeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                oldEndTime = endTimeComboBox.getSelectionModel().getSelectedItem().getTime();
                endTimeTextField.setText(oldEndTime);
            }
        });

        HBox eventEndTimeHBox = new HBox();
        eventEndTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventEndTimeHBox.setPadding(new Insets(0, 0, 10, 0));
        eventEndTimeHBox.getChildren().addAll(end, endDatePicker, endTimeTextField, endTimeComboBox);

        return eventEndTimeHBox;
    }

    private HBox makeEventNotifyTimeHBox() {
        ImageView eventNotifyTimeImageView = makeImageView("notifyicon", "Thời gian thông báo", 5, 10, 0, 8);

        notifyTimeComboBox = new ComboBox<>();
        notifyTimeComboBox.getItems().addAll(
                "Tại thời điểm sự kiện",
                "Trước 10 phút",
                "Trước 30 phút",
                "Tùy chọn",
                "Không thông báo"
        );
        notifyTimeComboBox.setStyle("-fx-font: 18px \"System\";");
        notifyTimeComboBox.getSelectionModel().selectFirst();

        notifyTimeTextField = new TextField();
        notifyTimeTextField.setFont(new Font("System", 20));
        notifyTimeTextField.setPromptText("Trước");
        notifyTimeTextField.setMaxWidth(110);
        notifyTimeTextField.setVisible(false);
        notifyTimeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    notifyTimeTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        notifyTimeUnitComboBox = new ComboBox<>();
        notifyTimeUnitComboBox.getItems().addAll(
                "phút",
                "giờ",
                "ngày"
        );
        notifyTimeUnitComboBox.setStyle("-fx-font: 18px \"System\";");
        notifyTimeUnitComboBox.setMaxWidth(120);
        notifyTimeUnitComboBox.getSelectionModel().selectFirst();
        notifyTimeUnitComboBox.setVisible(false);
        notifyTimeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (notifyTimeComboBox.getSelectionModel().getSelectedItem().toString().equals("Tùy chọn")) {
                    notifyTimeTextField.setVisible(true);
                    notifyTimeUnitComboBox.setVisible(true);
                } else {
                    notifyTimeTextField.setVisible(false);
                    notifyTimeUnitComboBox.setVisible(false);
                }
            }
        });

        HBox notifyTimeHBox = new HBox();
        notifyTimeHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        notifyTimeHBox.setPadding(new Insets(0, 0, 10, 0));
        notifyTimeHBox.getChildren().addAll(eventNotifyTimeImageView, notifyTimeComboBox, notifyTimeTextField, notifyTimeUnitComboBox);

        return notifyTimeHBox;
    }

    private HBox makeEventAlarmHBox() {
        ImageView alarmImageView = makeImageView("alarm", "Nhạc chuông thông báo", 5, 10, 0, 8);

        alarmComboBox = new ComboBox<Alarm>();
        alarmComboBox.setItems(FXCollections.observableArrayList(dbConnection.getAlarmList()));
        alarmComboBox.setStyle("-fx-font: 18px \"System\";");
        alarmComboBox.setPadding(new Insets(7, 0, 0, 0));
        alarmComboBox.setConverter(new StringConverter<Alarm>() {
            @Override
            public String toString(Alarm object) {
                return object.getName();
            }

            @Override
            public Alarm fromString(String string) {
                return null;
            }
        });

        HBox alarmHBox = new HBox();
        alarmHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        alarmHBox.setPadding(new Insets(0, 0, 10, 0));
        alarmHBox.getChildren().addAll(alarmImageView, alarmComboBox);

        return alarmHBox;
    }

    private HBox makeEventDescriptionHBox() {
        ImageView eventDescriptionImageView = makeImageView("description", "Mô tả sự kiện", 5, 10, 0, 8);

        eventDescriptionTextArea = new TextArea();
        eventDescriptionTextArea.setPromptText("Thêm mô tả sự kiện");
        eventDescriptionTextArea.setWrapText(true);
        eventDescriptionTextArea.setPrefRowCount(5);
        eventDescriptionTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                isChanged = true;
            }
        });
        HBox.setMargin(eventDescriptionTextArea, new Insets(0, 10, 0, 0));
        eventDescriptionTextArea.setFont(new Font("System", 18));
        HBox.setHgrow(eventDescriptionTextArea, Priority.ALWAYS);

        HBox eventDescriptionHBox = new HBox();
        eventDescriptionHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventDescriptionHBox.setPadding(new Insets(0, 0, 10, 0));
        VBox.setVgrow(eventDescriptionHBox, Priority.ALWAYS);
        eventDescriptionHBox.getChildren().addAll(eventDescriptionImageView, eventDescriptionTextArea);

        return eventDescriptionHBox;
    }

    private HBox makeColorChooseHBox() {
        ImageView colorChooser = makeImageView("colorchooser", "Chọn màu sự kiện", 5, 10, 0, 8);
        colorPicker = new ColorPicker();
        colorPicker.setStyle("-fx-font: 18px \"System\";");
        DbConnection dbConnection = new DbConnection();
        colorPicker.setValue(Color.web(dbConnection.getDefaultColor("event")));

        HBox colorChooserHBox = new HBox();
        colorChooserHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        colorChooserHBox.setPadding(new Insets(0, 0, 10, 0));
        colorChooserHBox.getChildren().addAll(colorChooser, colorPicker);

        return colorChooserHBox;
    }

    private ComboBox<Time> makeTimeComboBox() {
        ComboBox<Time> comboBox = new ComboBox<Time>();
        comboBox.setItems(FXCollections.observableArrayList(
                new Time(0, 0),
                new Time(0, 30),
                new Time(1, 0),
                new Time(1, 30),
                new Time(2, 0),
                new Time(2, 30),
                new Time(3, 0),
                new Time(3, 30),
                new Time(4, 0),
                new Time(4, 30),
                new Time(5, 0),
                new Time(5, 30),
                new Time(6, 0),
                new Time(6, 30),
                new Time(7, 0),
                new Time(7, 30),
                new Time(8, 0),
                new Time(8, 30),
                new Time(9, 0),
                new Time(9, 30),
                new Time(10, 30),
                new Time(11, 0),
                new Time(11, 30),
                new Time(12, 0),
                new Time(12, 30),
                new Time(13, 0),
                new Time(13, 30),
                new Time(14, 0),
                new Time(14, 30),
                new Time(15, 0),
                new Time(15, 30),
                new Time(16, 0),
                new Time(16, 30),
                new Time(17, 0),
                new Time(17, 30),
                new Time(18, 0),
                new Time(18, 30),
                new Time(19, 0),
                new Time(19, 30),
                new Time(20, 0),
                new Time(20, 30),
                new Time(21, 0),
                new Time(21, 30),
                new Time(22, 0),
                new Time(22, 3),
                new Time(23, 0),
                new Time(23, 30)
        ));
        comboBox.setConverter(new StringConverter<Time>() {
            @Override
            public String toString(Time object) {
                return object.getTime();
            }

            @Override
            public Time fromString(String string) {
                return null;
            }
        });
        comboBox.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue)  //when focus lost
                comboBox.setVisible(false);
        });
        ;
        comboBox.setStyle("-fx-font: 18px \"System\";");
        comboBox.setVisible(false);
        return comboBox;
    }

    private DatePicker makeDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setMaxWidth(200);
        datePicker.setStyle("-fx-font: 18px \"System\";");
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
        //datePicker.setValue(LocalDate.of(defaultYear, defaultMonth + 1, defaultDay));
        return datePicker;
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


}