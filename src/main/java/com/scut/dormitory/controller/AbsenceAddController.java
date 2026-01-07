package com.scut.dormitory.controller;

import com.scut.dormitory.dao.AbsenceDao;
import com.scut.dormitory.dao.StudentDao;
import com.scut.dormitory.model.AbsenceRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AbsenceAddController {

    @FXML private TextField stuNoField;
    @FXML private DatePicker datePicker;
    @FXML private TextArea detailArea;

    private StudentDao studentDao = new StudentDao();
    private AbsenceDao absenceDao = new AbsenceDao();
    private boolean isSaveClicked = false;

    @FXML
    public void initialize() {
        // 默认选中今天
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    protected void onSave() {
        String stuNo = stuNoField.getText().trim();
        LocalDate date = datePicker.getValue();
        String detail = detailArea.getText().trim();

        // 1. 基础校验
        if (stuNo.isEmpty() || date == null || detail.isEmpty()) {
            showAlert("请填写完整信息！");
            return;
        }

        // 2. 检查学号是否存在，并获取 ID
        int studentId = studentDao.findIdByStudentNo(stuNo);
        if (studentId == -1) {
            showAlert("该学号不存在，请检查输入！");
            return;
        }

        // 3. 构建对象并保存
        AbsenceRecord record = new AbsenceRecord();
        record.setStudentId(studentId);
        record.setDate(date.toString()); // 转换为 yyyy-MM-dd 字符串
        record.setDetail(detail);

        if (absenceDao.add(record)) {
            isSaveClicked = true;
            closeWindow();
        } else {
            showAlert("保存失败，系统错误。");
        }
    }

    @FXML
    protected void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) stuNoField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }

    public boolean isSaveClicked() {
        return isSaveClicked;
    }
}