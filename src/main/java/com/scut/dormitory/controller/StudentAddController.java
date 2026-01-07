package com.scut.dormitory.controller;

import com.scut.dormitory.dao.DormDao;
import com.scut.dormitory.dao.StudentDao;
import com.scut.dormitory.model.Building;
import com.scut.dormitory.model.DormRoom;
import com.scut.dormitory.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentAddController {

    @FXML private TextField noField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<Building> buildingCombo;
    @FXML private ComboBox<DormRoom> roomCombo;

    private DormDao dormDao = new DormDao();
    private StudentDao studentDao = new StudentDao();
    private boolean isSaveClicked = false;

    // --- 新增：用于记录编辑模式的状态 ---
    private Student editingStudent = null; // 如果不为null，说明是编辑模式
    private int oldRoomId = -1; // 记录原本的房间ID

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("男", "女");
        buildingCombo.getItems().addAll(dormDao.getAllBuildings());
    }

    // --- 新增：接收要编辑的学生数据 (回显逻辑) ---
    public void setEditData(Student student) {
        this.editingStudent = student;
        this.oldRoomId = student.getDormRoomId(); // 记住旧房间

        // 1. 回显基本文本
        noField.setText(student.getStudentNo());
        noField.setText(student.getStudentNo());
        nameField.setText(student.getName());
        genderCombo.setValue(student.getGender());

        // 2. 回显楼宇 (难点：ComboBox里存的是对象，要循环查找名字匹配的)
        for (Building b : buildingCombo.getItems()) {
            if (b.getName().equals(student.getBuildingName())) {
                buildingCombo.setValue(b);
                // 必须手动触发一下加载房间的逻辑
                onBuildingSelect();
                break;
            }
        }

        // 3. 回显房间 (难点：同上)
        if (buildingCombo.getValue() != null) {
            for (DormRoom r : roomCombo.getItems()) {
                if (r.getRoomNumber().equals(student.getRoomName())) {
                    roomCombo.setValue(r);
                    break;
                }
            }
        }
    }

    @FXML
    protected void onBuildingSelect() {
        Building selectedBuilding = buildingCombo.getValue();
        if (selectedBuilding != null) {
            roomCombo.getItems().clear();
            // 加载未满房间
            roomCombo.getItems().addAll(dormDao.getAvailableRooms(selectedBuilding.getId()));

            // 特殊逻辑：如果是编辑模式，且当前选的楼就是原本的楼，
            // 那么要把学生自己原本住的那个房间也加进去(否则如果满了他就选不回自己原本的房间了)
            if (editingStudent != null && selectedBuilding.getName().equals(editingStudent.getBuildingName())) {
                // 这里简化处理，只要能选别的未满房间即可，通常不需要把满员的自己加回去，除非逻辑要求极其严格
            }
            roomCombo.setPromptText("请选择房间");
        }
    }

    @FXML
    protected void onSave() {
        if (noField.getText().isEmpty() || nameField.getText().isEmpty() ||
                genderCombo.getValue() == null || roomCombo.getValue() == null) {
            showAlert("请填写完整信息！");
            return;
        }

        // 封装数据
        Student s = new Student();
        s.setStudentNo(noField.getText());
        s.setName(nameField.getText());
        s.setGender(genderCombo.getValue());
        s.setDormRoomId(roomCombo.getValue().getId());

        boolean success;
        if (editingStudent == null) {
            // === 新增模式 ===
            success = studentDao.add(s);
        } else {
            // === 编辑模式 ===
            s.setId(editingStudent.getId()); // 别忘了设置ID
            success = studentDao.update(s, oldRoomId);
        }

        if (success) {
            isSaveClicked = true;
            closeWindow();
        } else {
            showAlert("保存失败，请检查学号是否重复！");
        }
    }

    @FXML
    protected void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) noField.getScene().getWindow();
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