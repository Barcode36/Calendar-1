package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Settings {
    public void display(){
        Stage window = new Stage();
        window.setMinWidth(450);
        window.setMinHeight(570);
        window.setTitle("Thiết lập");
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        DbConnection colorConnection = new DbConnection();

        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(true);
        window.initStyle(StageStyle.UTILITY);

        //Sự kiện - nhóm HBox 1
        Label labelSuKien = new Label();
        labelSuKien.setText("Sự kiện:");
        labelSuKien.setFont(new Font("System", 20));
        HBox.setMargin(labelSuKien, new Insets(0, 0, 0, 20));

        HBox hBoxSuKienNhom1 = new HBox();
        hBoxSuKienNhom1.getChildren().add(labelSuKien);

        //Sự kiện - nhóm HBox 2
        ImageView imageViewMauSuKien = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerSuKien = new ColorPicker();
        colorPickerSuKien.setValue(Color.valueOf(colorConnection.getDefaultColor("event")));
        colorPickerSuKien.setStyle("-fx-font: 20px \"System\";");

        HBox hBoxSuKienNhom2 = new HBox();
        hBoxSuKienNhom2.getChildren().addAll(imageViewMauSuKien, colorPickerSuKien);
        HBox.setMargin(imageViewMauSuKien, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerSuKien, new Insets(0, 0, 0, 30));


        //Sự kiện - nhóm HBox 3
        ImageView imageViewAmBaoSuKien = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox comboBoxAmBaoSuKien = new ComboBox<>();
        comboBoxAmBaoSuKien.setStyle("-fx-font: 20px \"System\";");
        comboBoxAmBaoSuKien.getItems().addAll(
                "Âm báo 1",
                "Âm báo 2",
                "Âm báo 3",
                "Âm báo 4",
                "Âm báo 5",
                "Thêm từ thư mục khác"
        ); //Cần sửa lại
        comboBoxAmBaoSuKien.getSelectionModel().selectFirst(); //Cần sửa lại

        HBox hBoxSuKienNhom3 = new HBox();
        hBoxSuKienNhom3.getChildren().addAll(imageViewAmBaoSuKien, comboBoxAmBaoSuKien);
        HBox.setMargin(imageViewAmBaoSuKien, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoSuKien, new Insets(10, 0, 0, 38));



        //Ngày lễ - nhóm HBox 1
        Label labelNgayLe = new Label();
        labelNgayLe.setText("Ngày lễ:");
        labelNgayLe.setFont(new Font("System", 20));
        HBox.setMargin(labelNgayLe, new Insets(20, 0, 0, 20));

        HBox hBoxNgayLeNhom1 = new HBox();
        hBoxNgayLeNhom1.getChildren().add(labelNgayLe);

        //Ngày lễ - nhóm HBox 2
        ImageView imageViewMauNgayLe= makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerNgayLe = new ColorPicker();
        colorPickerNgayLe.setValue(Color.valueOf(colorConnection.getDefaultColor("holiday")));
        colorPickerNgayLe.setStyle("-fx-font: 20px \"System\";");

        HBox hBoxNgayLeNhom2 = new HBox();
        hBoxNgayLeNhom2.getChildren().addAll(imageViewMauNgayLe, colorPickerNgayLe);
        HBox.setMargin(imageViewMauNgayLe, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerNgayLe, new Insets(0, 0, 0, 30));

        //Ngày lễ - nhóm HBox 3
        ImageView imageViewAmBaoNgayLe = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox comboBoxAmBaoNgayLe = new ComboBox<>();
        comboBoxAmBaoNgayLe.setStyle("-fx-font: 20px \"System\";");
        comboBoxAmBaoNgayLe.getItems().addAll(
                "Âm báo 1",
                "Âm báo 2",
                "Âm báo 3",
                "Âm báo 4",
                "Âm báo 5",
                "Thêm từ thư mục khác"
        ); //Cần sửa lại
        comboBoxAmBaoNgayLe.getSelectionModel().selectFirst(); //Cần sửa lại

        HBox hBoxNgayLeNhom3 = new HBox();
        hBoxNgayLeNhom3.getChildren().addAll(imageViewAmBaoNgayLe, comboBoxAmBaoNgayLe);
        HBox.setMargin(imageViewAmBaoNgayLe, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoNgayLe, new Insets(10, 0, 0, 38));



        //Sinh nhật - nhóm HBox 1
        Label labelSinhNhat = new Label();
        labelSinhNhat.setText("Sinh nhật:");
        labelSinhNhat.setFont(new Font("System", 20));
        HBox.setMargin(labelSinhNhat, new Insets(20, 0, 0, 20));

        HBox hBoxSinhNhatNhom1 = new HBox();
        hBoxSinhNhatNhom1.getChildren().add(labelSinhNhat);

        //Sinh nhật - nhóm HBox 2
        ImageView imageViewMauSinhNhat = makeImageView("colorchooser", "Chọn màu sự kiện", 0, 0, 0, 0);
        ColorPicker colorPickerSinhNhat = new ColorPicker();
        colorPickerSinhNhat.setValue(Color.valueOf(colorConnection.getDefaultColor("birthday")));
        colorPickerSinhNhat.setStyle("-fx-font: 20px \"System\";");

        HBox hBoxSinhNhatNhom2 = new HBox();
        hBoxSinhNhatNhom2.getChildren().addAll(imageViewMauSinhNhat, colorPickerSinhNhat);
        HBox.setMargin(imageViewMauSinhNhat, new Insets(0, 0, 0, 50));
        HBox.setMargin(colorPickerSinhNhat, new Insets(0, 0, 0, 30));

        //Sinh nhật - nhóm HBox 3
        ImageView imageViewAmBaoSinhNhat = makeImageView("notifyicon", "Thời gian thông báo", 30, 0, 0, 0);
        ComboBox comboBoxAmBaoSinhNhat = new ComboBox<>();
        comboBoxAmBaoSinhNhat.setStyle("-fx-font: 20px \"System\";");
        comboBoxAmBaoSinhNhat.getItems().addAll(
                "Âm báo 1",
                "Âm báo 2",
                "Âm báo 3",
                "Âm báo 4",
                "Âm báo 5",
                "Thêm từ thư mục khác"
        ); //Cần sửa lại
        comboBoxAmBaoSinhNhat.getSelectionModel().selectFirst(); //Cần sửa lại

        HBox hBoxSinhNhatNhom3 = new HBox();
        hBoxSinhNhatNhom3.getChildren().addAll(imageViewAmBaoSinhNhat, comboBoxAmBaoSinhNhat);
        HBox.setMargin(imageViewAmBaoSinhNhat, new Insets(10, 0, 0, 50));
        HBox.setMargin(comboBoxAmBaoSinhNhat, new Insets(10, 0, 0, 38));

        // Nhóm button
        Button buttonThietLap = new Button();
        buttonThietLap.setText("Thiết lập");
        buttonThietLap.setStyle("-fx-font: 20px \"System\";");
        buttonThietLap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorConnection.setDefaultColor("event", Utils.toRGBCode(colorPickerSuKien.getValue()));
                colorConnection.setDefaultColor("holiday", Utils.toRGBCode(colorPickerNgayLe.getValue()));
                colorConnection.setDefaultColor("birthday", Utils.toRGBCode(colorPickerSinhNhat.getValue()));

                //Cập nhật âm báo cho sự kiện, ngày lễ, sinh nhật vào CSDL

                window.close();
            }
        });

        Button buttonHuy = new Button();
        buttonHuy.setText("Hủy");
        buttonHuy.setStyle("-fx-font: 20px \"System\";");
        buttonHuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        HBox hBoxButton = new HBox();
        hBoxButton.getChildren().addAll(buttonThietLap, buttonHuy);
        HBox.setMargin(buttonThietLap, new Insets(40, 0, 0, 180));
        HBox.setMargin(buttonHuy, new Insets(40, 0, 0, 30));

        VBox vBoxSettings = new VBox();
        vBoxSettings.getChildren().addAll(hBoxSuKienNhom1, hBoxSuKienNhom2, hBoxSuKienNhom3,
                hBoxNgayLeNhom1, hBoxNgayLeNhom2, hBoxNgayLeNhom3,
                hBoxSinhNhatNhom1, hBoxSinhNhatNhom2, hBoxSinhNhatNhom3,
                hBoxButton);

        Scene scene = new Scene(vBoxSettings);
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
}
