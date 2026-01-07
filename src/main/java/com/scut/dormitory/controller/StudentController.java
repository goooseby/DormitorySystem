package com.scut.dormitory.controller;

import com.scut.dormitory.dao.StudentDao;
import com.scut.dormitory.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*; // 引入 Alert, ButtonType 等
import javafx.stage.Stage;

import java.util.List;

public class StudentController {

    @FXML private TextField searchField;
    @FXML private TableView<Student> studentTable;
    @FXML private Label statusLabel;

    private StudentDao studentDao = new StudentDao();
    private ObservableList<Student> tableData = FXCollections.observableArrayList();

    // 初始化方法，界面加载时会自动调用
    @FXML
    public void initialize() {
        // 绑定数据源
        studentTable.setItems(tableData);
        // 默认加载所有数据
        loadData(null);
    }

    // 点击搜索按钮
    @FXML
    protected void onSearchClick() {
        String keyword = searchField.getText();
        loadData(keyword);
    }

    // ================== 新增的代码开始 ==================

    // 1. 点击“删除选中”按钮
    @FXML
    protected void onDeleteClick() {
        // 获取当前表格选中的那一行数据
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        // 如果没选中，弹窗提示
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "提示", "请先在表格中选中一名学生！");
            return;
        }

        // 弹出确认删除的对话框
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认删除");
        confirm.setHeaderText(null);
        confirm.setContentText("确定要删除学生 [" + selectedStudent.getName() + "] 吗？\n此操作不可恢复！");

        // 等待用户点击“确定”
        if (confirm.showAndWait().get() == ButtonType.OK) {
            // 调用 DAO 去数据库删除 (注意：这里需要你的 StudentDao 里已经写好了 delete 方法)
            boolean success = studentDao.delete(selectedStudent.getId(), selectedStudent.getDormRoomId());

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "成功", "删除成功！");
                // 删除成功后，刷新一下表格
                onSearchClick();
            } else {
                showAlert(Alert.AlertType.ERROR, "失败", "删除失败，请检查数据库连接。");
            }
        }
    }

    @FXML
    protected void onAddClick() {
        try {
            // 1. 加载 FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/student-add-dialog.fxml"));
            Scene scene = new Scene(loader.load());

            // 2. 创建新窗口 (Stage)
            Stage stage = new Stage();
            stage.setTitle("新增学生");
            stage.setScene(scene);

            // 设置模态窗口 (必须关掉这个窗口才能点主界面)
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // 3. 显示窗口并等待关闭
            stage.showAndWait();

            // 4. 窗口关闭后，检查是否保存成功，如果成功则刷新表格
            StudentAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                showAlert(Alert.AlertType.INFORMATION, "成功", "学生添加成功！");
                onSearchClick(); // 刷新表格
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 在 onDeleteClick 旁边添加这个方法
    @FXML
    protected void onEditClick() {
        // 1. 获取选中行
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "提示", "请先选中一位学生！");
            return;
        }

        try {
            // 2. 加载弹窗 FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/student-add-dialog.fxml"));
            // 注意：这里先不要 load()，或者 load 之后马上获取 controller
            Scene scene = new Scene(loader.load());

            // 3. 获取控制器，并传入数据
            StudentAddController controller = loader.getController();
            controller.setEditData(selectedStudent); // <--- 关键：把数据传过去回显

            // 4. 显示窗口
            Stage stage = new Stage();
            stage.setTitle("修改学生信息");
            stage.setScene(scene);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // 5. 刷新表格
            if (controller.isSaveClicked()) {
                showAlert(Alert.AlertType.INFORMATION, "成功", "修改成功！");
                onSearchClick(); // 重新查库刷新
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. 通用弹窗工具方法 (方便上面调用)
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // ================== 新增的代码结束 ==================

    // 加载数据到表格
    private void loadData(String keyword) {
        tableData.clear(); // 清空旧数据
        List<Student> list = studentDao.list(keyword); // 查数据库
        tableData.addAll(list); // 添加新数据
        statusLabel.setText("共找到 " + list.size() + " 条记录");
    }


}