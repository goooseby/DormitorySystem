package com.scut.dormitory;

import atlantafx.base.theme.PrimerLight; // 引入现代主题
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. 全局应用 AtlantaFX 主题 (Primer Light 风格，类似 GitHub 的干净风格)
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // 2. 加载 FXML 界面
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/login-view.fxml"));

        // 3. 设置场景大小
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        stage.setTitle("学生宿舍管理系统 - 登录");
        stage.setScene(scene);
        stage.setResizable(false); // 禁止调整窗口大小
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}