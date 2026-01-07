package com.scut.dormitory.controller;

import com.scut.dormitory.dao.DormDao;
import com.scut.dormitory.model.Building;
import com.scut.dormitory.model.DormRoom;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.TilePane;

import java.util.List;

public class DormController {

    @FXML private ListView<Building> buildingList;
    @FXML private Label titleLabel;
    @FXML private TilePane roomPane;

    private DormDao dormDao = new DormDao();

    @FXML
    public void initialize() {
        // 1. 加载所有楼宇到左侧列表
        buildingList.getItems().addAll(dormDao.getAllBuildings());

        // 2. 监听选择事件：当选中某栋楼时，刷新右侧
        buildingList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadRooms(newValue);
            }
        });

        // 3. 默认选中第一个，避免界面空白
        if (!buildingList.getItems().isEmpty()) {
            buildingList.getSelectionModel().select(0);
        }
    }

    // 核心逻辑：循环生成房间卡片
    private void loadRooms(Building building) {
        titleLabel.setText(building.getName() + " - 房源视图");
        roomPane.getChildren().clear(); // 先清空旧的按钮

        List<DormRoom> rooms = dormDao.getRoomsByBuildingId(building.getId());

        for (DormRoom room : rooms) {
            Button btn = new Button();

            // 按钮文字：房号 + (当前/最大)
            String text = room.getRoomNumber() + "\n(" + room.getCurrentCapacity() + "/" + room.getMaxCapacity() + ")";
            btn.setText(text);
            btn.setPrefSize(100, 80); // 设置卡片大小

            // 颜色判断逻辑
            if (room.getCurrentCapacity() >= room.getMaxCapacity()) {
                // 满员：红色背景
                btn.setStyle("-fx-background-color: #ffcccc; -fx-border-color: #ff0000; -fx-text-fill: #cc0000; -fx-font-weight: bold;");
            } else {
                // 未满：绿色背景
                btn.setStyle("-fx-background-color: #d4edda; -fx-border-color: #28a745; -fx-text-fill: #155724; -fx-font-weight: bold;");
            }

            // 点击按钮，弹出详情
            btn.setOnAction(e -> showRoomDetails(room));

            roomPane.getChildren().add(btn);
        }
    }

    // 点击房间后的弹窗
    private void showRoomDetails(DormRoom room) {
        List<String> students = dormDao.getStudentNamesByRoomId(room.getId());

        StringBuilder content = new StringBuilder();
        if (students.isEmpty()) {
            content.append("当前为空房");
        } else {
            for (String name : students) {
                content.append("👤 ").append(name).append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("房间详情 - " + room.getRoomNumber());
        alert.setHeaderText("当前入住人员 (" + room.getCurrentCapacity() + "/" + room.getMaxCapacity() + ")");
        alert.setContentText(content.toString());
        alert.show();
    }
}