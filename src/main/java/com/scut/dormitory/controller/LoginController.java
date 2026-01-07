package com.scut.dormitory.controller;

import com.scut.dormitory.dao.AdminDao;
import com.scut.dormitory.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    // 1. 这里对应 FXML 里的 fx:id
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AdminDao adminDao = new AdminDao();

    // 2. 这里对应 FXML 里的 onAction="#onLoginButtonClick"
    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "提示", "账号或密码不能为空！");
            return;
        }

        // 调用 DAO 查数据库
        Admin admin = adminDao.login(username, password);

        if (admin != null) {
            // 登录成功
            // showAlert(Alert.AlertType.INFORMATION, "成功", "欢迎回来，" + admin.getName() + "！");
            // 建议把上面的弹窗注释掉，直接跳转体验更好

            try {
                // 1. 获取当前登录窗口的 Stage (舞台)
                Stage loginStage = (Stage) usernameField.getScene().getWindow();

                // 2. 加载主界面 FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/main-view.fxml"));

                // 3. 创建新场景
                Scene mainScene = new Scene(fxmlLoader.load(), 900, 600); // 宽900 高600

                // 4. 在当前舞台上切换场景 (或者你可以 new 一个新 Stage，但切换场景更平滑)
                loginStage.setScene(mainScene);
                loginStage.setTitle("学生宿舍管理系统 - 主界面");
                loginStage.setResizable(true); // 主界面通常允许调整大小
                loginStage.centerOnScreen();   // 居中显示

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "系统错误", "无法加载主界面：" + e.getMessage());
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "错误", "账号或密码错误！");
        }
    }

    // 封装一个弹窗方法，方便使用
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}