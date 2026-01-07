package com.scut.dormitory.controller;

import com.scut.dormitory.dao.AbsenceDao;
import com.scut.dormitory.model.AbsenceRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AbsenceController {

    @FXML private TextField searchField;
    @FXML private TableView<AbsenceRecord> absenceTable;
    @FXML private Label statusLabel;

    private AbsenceDao absenceDao = new AbsenceDao();
    private ObservableList<AbsenceRecord> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        absenceTable.setItems(tableData);
        loadData(null);
    }

    @FXML
    protected void onSearchClick() {
        loadData(searchField.getText());
    }

    @FXML
    protected void onAddClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/scut/dormitory/view/absence-add-dialog.fxml"));
            Scene scene = new Scene(loader.load()); // 这里不需要 BorderPane，直接 load 即可

            Stage stage = new Stage();
            stage.setTitle("登记缺勤");
            stage.setScene(scene);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // 窗口关闭后刷新表格
            AbsenceAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                onSearchClick(); // 刷新列表
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String keyword) {
        tableData.clear();
        List<AbsenceRecord> list = absenceDao.list(keyword);
        tableData.addAll(list);
        statusLabel.setText("共找到 " + list.size() + " 条记录");
    }
}