package com.scut.dormitory.controller;

import com.scut.dormitory.dao.AdminDao;
import com.scut.dormitory.model.Admin;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AdminDao adminDao = new AdminDao();

    /**
     * 登录按钮点击事件处理
     */
    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. 基础非空校验
        if (username == null || username.trim().isEmpty()) {
            showAlert("输入错误", "用户名不能为空");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            showAlert("输入错误", "密码不能为空");
            return;
        }

        // 2. 调用 DAO 进行数据库验证
        Admin admin = adminDao.login(username, password);

        if (admin != null) {
            System.out.println("[INFO] Login successful. User: " + admin.getUsername());
            showAlert("登录成功", "欢迎回来，" + admin.getName());

            // TODO: 这里后续将添加跳转到主界面的逻辑

        } else {
            System.out.println("[WARN] Login failed. Invalid credentials for user: " + username);
            showAlert("登录失败", "用户名或密码错误");
        }
    }

    /**
     * 辅助方法：显示弹窗提示
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("系统提示");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}