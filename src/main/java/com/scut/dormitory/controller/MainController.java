package com.scut.dormitory.controller;

import com.scut.dormitory.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainBorderPane; // 对应 FXML 里的 fx:id

    // 1. 点击“宿舍房源管理”
    @FXML
    protected void onDormManageClick() {
        try {
            // 加载 dorm-view.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/dorm-view.fxml"));
            // 放入中心区域
            mainBorderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            // showAlert("错误", "加载失败");
        }
    }

    // 2. 点击“学生入住管理”
    @FXML
    protected void onStudentManageClick() {
        try {
            // 加载 student-view.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/student-view.fxml"));
            // 将加载出来的界面 (Node) 放到主界面的 Center 区域
            mainBorderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("错误", "无法加载学生管理界面");
        }
    }

    @FXML
    protected void onAbsenceRecordClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/absence-view.fxml"));
            mainBorderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            // showAlert("错误", "无法加载界面");
        }
    }

    // 4. 点击“退出登录”
    @FXML
    protected void onLogoutClick() {
        try {
            // 关闭当前窗口
            Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
            currentStage.close();

            // 重新打开登录窗口
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/login-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("学生宿舍管理系统 - 登录");
            loginStage.setScene(new Scene(fxmlLoader.load(), 400, 500));
            loginStage.setResizable(false);
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}