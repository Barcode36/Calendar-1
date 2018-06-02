package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class LoginDialog {
    public void display(){
        Stage window = new Stage();
        window.setTitle("Đăng nhập");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.setMinWidth(450);
        window.setWidth(450);
        window.setMinHeight(300);
        window.setHeight(300);
        window.initStyle(StageStyle.UTILITY);

        VBox vBox = new VBox();
        // dòng user
        HBox hBoxUser= new HBox();
        Label lbUser= new Label();
        lbUser.setText("Tài khoản:");
        lbUser.setFont(new Font("System", 22));
        //
        lbUser.setMaxWidth(Double.MAX_VALUE);
        HBox.setMargin(lbUser, new Insets(0, 10, 0, 5));
        TextField txUser =new TextField();
        txUser.setFont(new Font("System", 22));


        hBoxUser.getChildren().add(lbUser);
        hBoxUser.getChildren().add(txUser);
        hBoxUser.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBoxUser);
        // dòng password
        HBox hBoxPassword= new HBox();
        Label lbPassword= new Label();

        lbPassword.setText("Mật khẩu:");
        lbPassword.setFont(new Font("System", 22));
        HBox.setMargin(lbPassword, new Insets(0, 10, 0, 5));
        PasswordField txPassword =new PasswordField();
        txPassword.setFont(new Font("System", 22));

        hBoxPassword.getChildren().add(lbPassword);
        hBoxPassword.getChildren().add(txPassword);
        hBoxPassword.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBoxPassword);
        //dòng button đăng nhặp và hủy
        HBox hBoxButton =new HBox();
        Button btnLogin = new Button();
        Button btnCancel = new Button();
        btnLogin.setText("Đăng nhập");
        btnLogin.setFont(new Font("System", 22));
        HBox.setMargin(btnLogin, new Insets(0, 10, 0, 5));

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String user =txUser.getText();
                String password =txPassword.getText();
                DbConnection connection =new DbConnection();
                boolean check = connection.checkUser(user,password);
                if(check)
                {
                    window.close();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Tên đăng nhập hoặc mật khẩu đã sai");
                    alert.showAndWait();
                }
            }
        });
        btnCancel.setText("Hủy");
        btnCancel.setFont(new Font("System", 22));
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.getChildren().add(btnLogin);
        hBoxButton.getChildren().add(btnCancel);
        //xét khoảng cách giửa các hbox

        VBox.setMargin(hBoxUser,new Insets(40, 0, 10, 0));
        VBox.setMargin(hBoxPassword,new Insets(0, 0, 10, 0));
        VBox.setMargin(hBoxButton,new Insets(0, 0, 10, 0));
        vBox.getChildren().add(hBoxButton);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }
}
