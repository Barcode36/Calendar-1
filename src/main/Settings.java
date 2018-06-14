package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;

public class Settings {
    private Stage window;
    private DbConnection dbConnection;
    List<Alarm> alarms;

    public Settings() {
        window = new Stage();
        window.setMinWidth(550);
        window.setMinHeight(700);
        window.setHeight(700);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);
        window.setTitle("Thiết lập");

        dbConnection = new DbConnection();

        alarms = dbConnection.getAlarmList();

        //Sự kiện - nhóm HBox 1
        Label labelSuKien = new Label();
        labelSuKien.setText("Sự kiện:");
        labelSuKien.setFont(new Font("System", 18));
        HBox.setMargin(labelSuKien, new Insets(0, 0, 0, 20));

        HBox hBoxSuKienNhom1 = new HBox();
        hBoxSuKienNhom1.getChildren().add(labelSuKien);

        //Sự kiện - nhóm HBox 2
        ImageView imageViewMauSuKien = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerSuKien = new ColorPicker();
        colorPickerSuKien.setValue(Color.valueOf(dbConnection.getDefaultColor("event")));
        colorPickerSuKien.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxSuKienNhom2 = new HBox();
        hBoxSuKienNhom2.getChildren().addAll(imageViewMauSuKien, colorPickerSuKien);
        HBox.setMargin(imageViewMauSuKien, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerSuKien, new Insets(0, 0, 0, 30));


        //Sự kiện - nhóm HBox 3
        ImageView imageViewAmBaoSuKien = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox<Alarm> comboBoxAmBaoSuKien = new ComboBox<Alarm>();
        comboBoxAmBaoSuKien.setStyle("-fx-font: 18px \"System\";");
        comboBoxAmBaoSuKien.setItems(FXCollections.observableArrayList(alarms));
        comboBoxAmBaoSuKien.setConverter(new StringConverter<Alarm>() {
            @Override
            public String toString(Alarm object) {
                return object.getName();
            }

            @Override
            public Alarm fromString(String string) {
                return null;
            }
        });
        Alarm defaultEventAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("event"));
        comboBoxAmBaoSuKien.getSelectionModel().select(defaultEventAlarm); //Cần sửa lại

        HBox hBoxSuKienNhom3 = new HBox();
        hBoxSuKienNhom3.getChildren().addAll(imageViewAmBaoSuKien, comboBoxAmBaoSuKien);
        HBox.setMargin(imageViewAmBaoSuKien, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoSuKien, new Insets(10, 0, 0, 38));


        //Ngày lễ - nhóm HBox 1
        Label labelNgayLe = new Label();
        labelNgayLe.setText("Ngày lễ:");
        labelNgayLe.setFont(new Font("System", 18));
        HBox.setMargin(labelNgayLe, new Insets(20, 0, 0, 20));

        HBox hBoxNgayLeNhom1 = new HBox();
        hBoxNgayLeNhom1.getChildren().add(labelNgayLe);

        //Ngày lễ - nhóm HBox 2
        ImageView imageViewMauNgayLe = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerNgayLe = new ColorPicker();
        colorPickerNgayLe.setValue(Color.valueOf(dbConnection.getDefaultColor("holiday")));
        colorPickerNgayLe.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxNgayLeNhom2 = new HBox();
        hBoxNgayLeNhom2.getChildren().addAll(imageViewMauNgayLe, colorPickerNgayLe);
        HBox.setMargin(imageViewMauNgayLe, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerNgayLe, new Insets(0, 0, 0, 30));

        //Ngày lễ - nhóm HBox 3
//        ImageView imageViewAmBaoNgayLe = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
//        ComboBox<Alarm> comboBoxAmBaoNgayLe = new ComboBox<Alarm>();
//        comboBoxAmBaoNgayLe.setItems(FXCollections.observableArrayList(dbConnection.getAlarmList()));
//        comboBoxAmBaoNgayLe.setStyle("-fx-font: 18px \"System\";");
//        comboBoxAmBaoNgayLe.setConverter(new StringConverter<Alarm>() {
//            @Override
//            public String toString(Alarm object) {
//                return object.getName();
//            }
//
//            @Override
//            public Alarm fromString(String string) {
//                return null;
//            }
//        });
//        Alarm defaultHolidayAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("holiday"));
//        comboBoxAmBaoNgayLe.getSelectionModel().select(defaultHolidayAlarm); //Cần sửa lại

//        HBox hBoxNgayLeNhom3 = new HBox();
//        hBoxNgayLeNhom3.getChildren().addAll(imageViewAmBaoNgayLe, comboBoxAmBaoNgayLe);
//        HBox.setMargin(imageViewAmBaoNgayLe, new Insets(10, 0, 0, 50));
//        HBox.setMargin(comboBoxAmBaoNgayLe, new Insets(10, 0, 0, 38));


        //Sinh nhật - nhóm HBox 1
        Label labelSinhNhat = new Label();
        labelSinhNhat.setText("Sinh nhật:");
        labelSinhNhat.setFont(new Font("System", 18));
        HBox.setMargin(labelSinhNhat, new Insets(20, 0, 0, 20));

        HBox hBoxSinhNhatNhom1 = new HBox();
        hBoxSinhNhatNhom1.getChildren().add(labelSinhNhat);

        //Sinh nhật - nhóm HBox 2
        ImageView imageViewMauSinhNhat = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerSinhNhat = new ColorPicker();
        colorPickerSinhNhat.setValue(Color.valueOf(dbConnection.getDefaultColor("birthday")));
        colorPickerSinhNhat.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxSinhNhatNhom2 = new HBox();
        hBoxSinhNhatNhom2.getChildren().addAll(imageViewMauSinhNhat, colorPickerSinhNhat);
        HBox.setMargin(imageViewMauSinhNhat, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerSinhNhat, new Insets(0, 0, 0, 30));
//
//        //Sinh nhật - nhóm HBox 3
//        ImageView imageViewAmBaoSinhNhat = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
//        ComboBox<Alarm> comboBoxAmBaoSinhNhat = new ComboBox<Alarm>();
//        comboBoxAmBaoSinhNhat.setItems(FXCollections.observableArrayList(dbConnection.getAlarmList()));
//        comboBoxAmBaoSinhNhat.setStyle("-fx-font: 18px \"System\";");
//        comboBoxAmBaoSinhNhat.setConverter(new StringConverter<Alarm>() {
//            @Override
//            public String toString(Alarm object) {
//                return object.getName();
//            }
//
//            @Override
//            public Alarm fromString(String string) {
//                return null;
//            }
//        });
//        Alarm defaultBirthdayAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("birthday"));
//        comboBoxAmBaoSinhNhat.getSelectionModel().select(defaultBirthdayAlarm); //Cần sửa lại
//
//        HBox hBoxSinhNhatNhom3 = new HBox();
//        hBoxSinhNhatNhom3.getChildren().addAll(imageViewAmBaoSinhNhat, comboBoxAmBaoSinhNhat);
//        HBox.setMargin(imageViewAmBaoSinhNhat, new Insets(10, 0, 0, 50));
//        HBox.setMargin(comboBoxAmBaoSinhNhat, new Insets(10, 0, 0, 38));



        // Thiết lập màu và âm báo thông báo chung OEP
        Label labelThongBaoOEP = new Label();
        labelThongBaoOEP.setText("Thông báo chung OEP:");
        labelThongBaoOEP.setFont(new Font("System", 18));
        labelThongBaoOEP.setPadding(new Insets(20, 0, 0, 20));

        ImageView imageViewMauThongBaoOEP = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerThongBaoOEP = new ColorPicker();
        colorPickerThongBaoOEP.setValue(Color.valueOf(dbConnection.getDefaultColor("oepnews")));
        colorPickerThongBaoOEP.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxMauThongBaoOEP = new HBox();
        hBoxMauThongBaoOEP.getChildren().addAll(imageViewMauThongBaoOEP, colorPickerThongBaoOEP);
        HBox.setMargin(imageViewMauThongBaoOEP, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerThongBaoOEP, new Insets(0, 0, 0, 30));

        ImageView imageViewAmBaoThongBaoOEP = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox<Alarm> comboBoxAmBaoThongBaoOEP = new ComboBox<Alarm>();
        comboBoxAmBaoThongBaoOEP.setItems(FXCollections.observableArrayList(alarms));
        comboBoxAmBaoThongBaoOEP.setStyle("-fx-font: 18px \"System\";");
        comboBoxAmBaoThongBaoOEP.setConverter(new StringConverter<Alarm>() {
            @Override
            public String toString(Alarm object) {
                return object.getName();
            }

            @Override
            public Alarm fromString(String string) {
                return null;
            }
        });
        Alarm defaultOEPNewsAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("oepnews"));
        comboBoxAmBaoThongBaoOEP.getSelectionModel().select(defaultOEPNewsAlarm); //Cần sửa lại

        HBox hBoxAmBaoThongBaoOEP = new HBox();
        hBoxAmBaoThongBaoOEP.getChildren().addAll(imageViewAmBaoThongBaoOEP, comboBoxAmBaoThongBaoOEP);
        HBox.setMargin(imageViewAmBaoThongBaoOEP, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoThongBaoOEP, new Insets(10, 0, 0, 38));



        // thiết lập màu và âm báo thông báo nghỉ bù OEP
        Label labelThongBaoNghiBuOEP = new Label();
        labelThongBaoNghiBuOEP.setText("Thông báo nghỉ bù OEP:");
        labelThongBaoNghiBuOEP.setFont(new Font("System", 18));
        labelThongBaoNghiBuOEP.setPadding(new Insets(20, 0, 0, 20));

        ImageView imageViewMauThongBaoNghiBuOEP = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerThongBaoNghiBuOEP = new ColorPicker();
        colorPickerThongBaoNghiBuOEP.setValue(Color.valueOf(dbConnection.getDefaultColor("oepcoursenews")));
        colorPickerThongBaoNghiBuOEP.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxMauThongBaoNghiBuOEP = new HBox();
        hBoxMauThongBaoNghiBuOEP.getChildren().addAll(imageViewMauThongBaoNghiBuOEP, colorPickerThongBaoNghiBuOEP);
        HBox.setMargin(imageViewMauThongBaoNghiBuOEP, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerThongBaoNghiBuOEP, new Insets(0, 0, 0, 30));

        ImageView imageViewAmBaoThongBaoNghiBuOEP = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox<Alarm> comboBoxAmBaoThongBaoNghiBuOEP = new ComboBox<Alarm>();
        comboBoxAmBaoThongBaoNghiBuOEP.setItems(FXCollections.observableArrayList(alarms));
        comboBoxAmBaoThongBaoNghiBuOEP.setStyle("-fx-font: 18px \"System\";");
        comboBoxAmBaoThongBaoNghiBuOEP.setConverter(new StringConverter<Alarm>() {
            @Override
            public String toString(Alarm object) {
                return object.getName();
            }

            @Override
            public Alarm fromString(String string) {
                return null;
            }
        });
        Alarm defaultOEPCourseNewsAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("oepcoursenews"));
        comboBoxAmBaoThongBaoNghiBuOEP.getSelectionModel().select(defaultOEPCourseNewsAlarm); //Cần sửa lại

        HBox hBoxAmBaoThongBaoNghiBuOEP = new HBox();
        hBoxAmBaoThongBaoNghiBuOEP.getChildren().addAll(imageViewAmBaoThongBaoNghiBuOEP, comboBoxAmBaoThongBaoNghiBuOEP);
        HBox.setMargin(imageViewAmBaoThongBaoNghiBuOEP, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoThongBaoNghiBuOEP, new Insets(10, 0, 0, 38));




        // thiết lập màu và âm báo thông báo CTSV
        Label labelThongBaoCTSV = new Label();
        labelThongBaoCTSV.setText("Thông báo chung CTSV:");
        labelThongBaoCTSV.setFont(new Font("System", 18));
        labelThongBaoCTSV.setPadding(new Insets(20, 0, 0, 20));

        ImageView imageViewMauThongBaoCTSV = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerThongBaoCTSV = new ColorPicker();
        colorPickerThongBaoCTSV.setValue(Color.valueOf(dbConnection.getDefaultColor("ctsvnews")));
        colorPickerThongBaoCTSV.setStyle("-fx-font: 18px \"System\";");

        HBox hBoxMauThongBaoCTSV = new HBox();
        hBoxMauThongBaoCTSV.getChildren().addAll(imageViewMauThongBaoCTSV, colorPickerThongBaoCTSV);
        HBox.setMargin(imageViewMauThongBaoCTSV, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerThongBaoCTSV, new Insets(0, 0, 0, 30));

        ImageView imageViewAmBaoThongBaoCTSV = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox<Alarm> comboBoxAmBaoThongBaoCTSV = new ComboBox<Alarm>();
        comboBoxAmBaoThongBaoCTSV.setItems(FXCollections.observableArrayList(alarms));
        comboBoxAmBaoThongBaoCTSV.setStyle("-fx-font: 18px \"System\";");
        comboBoxAmBaoThongBaoCTSV.setConverter(new StringConverter<Alarm>() {
            @Override
            public String toString(Alarm object) {
                return object.getName();
            }

            @Override
            public Alarm fromString(String string) {
                return null;
            }
        });
        Alarm defaultCTSVNewsAlarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("ctsvnews"));
        comboBoxAmBaoThongBaoCTSV.getSelectionModel().select(defaultCTSVNewsAlarm);

        HBox hBoxAmBaoThongBaoCTSV = new HBox();
        hBoxAmBaoThongBaoCTSV.getChildren().addAll(imageViewAmBaoThongBaoCTSV, comboBoxAmBaoThongBaoCTSV);
        HBox.setMargin(imageViewAmBaoThongBaoCTSV, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoThongBaoCTSV, new Insets(10, 0, 0, 38));





        // Nhóm button
        Button buttonThietLap = new Button();
        buttonThietLap.setText("Thiết lập");
        buttonThietLap.setStyle("-fx-font: 18px \"System\";");
        buttonThietLap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbConnection.setDefaultColor("event", Utils.toRGBCode(colorPickerSuKien.getValue()));
                dbConnection.setDefaultColor("holiday", Utils.toRGBCode(colorPickerNgayLe.getValue()));
                dbConnection.setDefaultColor("birthday", Utils.toRGBCode(colorPickerSinhNhat.getValue()));
                dbConnection.setDefaultColor("oepnews", Utils.toRGBCode(colorPickerThongBaoOEP.getValue()));
                dbConnection.setDefaultColor("oepcoursenews", Utils.toRGBCode(colorPickerThongBaoNghiBuOEP.getValue()));
                dbConnection.setDefaultColor("ctsvnews", Utils.toRGBCode(colorPickerThongBaoCTSV.getValue()));

                Alarm defaultAlarm = comboBoxAmBaoSuKien.getSelectionModel().getSelectedItem();
                dbConnection.setDefaultAlarm("event", defaultAlarm.getAlarmid());
                defaultAlarm = comboBoxAmBaoThongBaoOEP.getSelectionModel().getSelectedItem();
                dbConnection.setDefaultAlarm("oepnews", defaultAlarm.getAlarmid());
                defaultAlarm = comboBoxAmBaoThongBaoNghiBuOEP.getSelectionModel().getSelectedItem();
                dbConnection.setDefaultAlarm("oepcoursenews", defaultAlarm.getAlarmid());
                defaultAlarm = comboBoxAmBaoThongBaoCTSV.getSelectionModel().getSelectedItem();
                dbConnection.setDefaultAlarm("ctsvnews", defaultAlarm.getAlarmid());

                window.close();
            }
        });

        Button buttonHuy = new Button();
        buttonHuy.setText("Hủy");
        buttonHuy.setStyle("-fx-font: 18px \"System\";");
        buttonHuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        HBox hBoxButton = new HBox();
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().addAll(buttonThietLap, buttonHuy);
        HBox.setMargin(buttonThietLap, new Insets(10, 10, 0, 0));
        HBox.setMargin(buttonHuy, new Insets(10, 0, 0, 0));

        Button addNewAlarm = new Button("Thêm nhạc chuông mới");
        addNewAlarm.setStyle("-fx-font: 18px \"System\";");
        addNewAlarm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("MP3 Format Sound", "*.mp3"));
                File selectedFiles = fc.showOpenDialog(null);
                if (selectedFiles != null) {
                    Alarm music = new Alarm();
                    music.setName(selectedFiles.getName());
                    if (selectedFiles.getPath().contains(" ")) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Tên file không được chưa dấu cách");
                        alert.showAndWait();
                        return;
                    }
                    music.setPath(selectedFiles.getPath().replaceAll("\\\\", "/"));
                    dbConnection.addAlarm(music);
                    alarms = dbConnection.getAlarmList();
                    comboBoxAmBaoSuKien.setItems(FXCollections.observableArrayList(alarms));
                    comboBoxAmBaoThongBaoNghiBuOEP.setItems(FXCollections.observableArrayList(alarms));
                    comboBoxAmBaoSuKien.setItems(FXCollections.observableArrayList(alarms));
                    Alarm defaultAlarm = comboBoxAmBaoSuKien.getSelectionModel().getSelectedItem();
                    dbConnection.setDefaultAlarm("event", defaultAlarm.getAlarmid());
                    defaultAlarm = comboBoxAmBaoThongBaoOEP.getSelectionModel().getSelectedItem();
                    dbConnection.setDefaultAlarm("oepnews", defaultAlarm.getAlarmid());
                    defaultAlarm = comboBoxAmBaoThongBaoNghiBuOEP.getSelectionModel().getSelectedItem();
                    dbConnection.setDefaultAlarm("oepcoursenews", defaultAlarm.getAlarmid());
                }
            }
        });
        VBox.setMargin(addNewAlarm, new Insets(15, 0, 0, 0));

        VBox vBoxMauAmBao = new VBox();
        vBoxMauAmBao.setMaxHeight(Double.MAX_VALUE);
        vBoxMauAmBao.getChildren().addAll(hBoxSuKienNhom1, hBoxSuKienNhom2, hBoxSuKienNhom3,
                hBoxNgayLeNhom1, hBoxNgayLeNhom2,
                hBoxSinhNhatNhom1, hBoxSinhNhatNhom2,
                labelThongBaoOEP, hBoxMauThongBaoOEP, hBoxAmBaoThongBaoOEP,
                labelThongBaoNghiBuOEP, hBoxMauThongBaoNghiBuOEP, hBoxAmBaoThongBaoNghiBuOEP,
                labelThongBaoCTSV, hBoxMauThongBaoCTSV, hBoxAmBaoThongBaoCTSV,
                addNewAlarm);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(vBoxMauAmBao);

        TabPane tabPane = new TabPane();
        tabPane.setMinHeight(500);
        Tab tabMauAmBao = new Tab("Màu & âm báo");
        tabMauAmBao.setContent(scrollPane);

        CheckBox autoThongBaoChungOEPCheckBox = new CheckBox("Tự động lấy thông báo chung từ OEP");
        autoThongBaoChungOEPCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                dbConnection.setOEPNewsNotifyStatus(newValue);
            }
        });
        autoThongBaoChungOEPCheckBox.setStyle("-fx-font: 18px \"System\";");
        if (dbConnection.getOEPNewsNotifyStatus()) {
            autoThongBaoChungOEPCheckBox.setSelected(true);
        } else
            autoThongBaoChungOEPCheckBox.setSelected(false);

        CheckBox autoThongBaoNghiBuOEPCheckBox = new CheckBox("Tự động lấy thông báo nghỉ, bù từ OEP");
        autoThongBaoNghiBuOEPCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                dbConnection.setOEPCourseNewsNotifyStatus(newValue);
            }
        });
        autoThongBaoNghiBuOEPCheckBox.setStyle("-fx-font: 18px \"System\";");
        if (dbConnection.getOEPCourseNewsNotifyStatus()) {
            autoThongBaoNghiBuOEPCheckBox.setSelected(true);
        } else
            autoThongBaoNghiBuOEPCheckBox.setSelected(false);

        CheckBox autoThongBaoChungCTSVCheckBox = new CheckBox("Tự động lấy thông báo từ CTSV");
        autoThongBaoChungCTSVCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                dbConnection.setCTSVNewsNotifyStatus(newValue);
            }
        });
        autoThongBaoChungCTSVCheckBox.setStyle("-fx-font: 18px \"System\";");
        if (dbConnection.getCTSVNewsNotifyStatus()) {
            autoThongBaoChungCTSVCheckBox.setSelected(true);
        } else
            autoThongBaoChungCTSVCheckBox.setSelected(false);

        VBox tuyChinhThongBaoVBox = new VBox();
        tuyChinhThongBaoVBox.setPadding(new Insets(10,0,0,0));
        tuyChinhThongBaoVBox.setSpacing(10);
        tuyChinhThongBaoVBox.getChildren().addAll(autoThongBaoChungOEPCheckBox, autoThongBaoNghiBuOEPCheckBox, autoThongBaoChungCTSVCheckBox);

        Tab tabTuyChinhThongBao = new Tab("Tùy chỉnh thông báo");
        tabTuyChinhThongBao.setContent(tuyChinhThongBaoVBox);

        ListView<String> listView = new ListView<String>();
        List<String> classIDList = dbConnection.getClassList();
        listView.setItems(FXCollections.observableArrayList(classIDList));

        TextField classIDTextField = new TextField();
        classIDTextField.setPromptText("Nhập mã lỡp cần đăng ký thông báo nghỉ, bù");
        classIDTextField.setStyle("-fx-font: 18px \"System\";");

        Button addClassID = new Button("Thêm");
        addClassID.setStyle("-fx-font: 18px \"System\";");
        addClassID.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbConnection.addClassID(classIDTextField.getText());
                List<String> classIDList = dbConnection.getClassList();
                listView.setItems(FXCollections.observableArrayList(classIDList));
            }
        });

        Button removeClassID = new Button("Xóa");
        removeClassID.setStyle("-fx-font: 18px \"System\";");
        removeClassID.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbConnection.removeClassID(classIDTextField.getText());
                List<String> classIDList = dbConnection.getClassList();
                listView.setItems(FXCollections.observableArrayList(classIDList));
            }
        });

        HBox controlClassIDHBox = new HBox();
        HBox.setMargin(addClassID, new Insets(0, 10, 0, 0));
        controlClassIDHBox.getChildren().addAll(addClassID, removeClassID);

        VBox classIDControlVBox = new VBox();
        classIDControlVBox.getChildren().addAll(listView, classIDTextField, controlClassIDHBox);

        Tab tabDanhSachMaLop = new Tab("Danh sách mã lớp");
        tabDanhSachMaLop.setContent(classIDControlVBox);

        tabMauAmBao.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }
        });
        tabDanhSachMaLop.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }
        });
        tabTuyChinhThongBao.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }
        });

        tabPane.getTabs().addAll(tabMauAmBao, tabTuyChinhThongBao, tabDanhSachMaLop);

        VBox vBoxSettings = new VBox();
        vBoxSettings.setFillWidth(true);
        vBoxSettings.setPadding(new Insets(0, 10, 10, 10));
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        vBoxSettings.getChildren().addAll(tabPane, hBoxButton);

        Scene scene = new Scene(vBoxSettings);
        window.setScene(scene);
    }

    public void display() {
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
}
