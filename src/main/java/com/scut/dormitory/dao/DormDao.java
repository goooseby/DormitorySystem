package com.scut.dormitory.dao;

import com.scut.dormitory.model.Building;
import com.scut.dormitory.model.DormRoom;
import com.scut.dormitory.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormDao {

    // 获取所有楼宇
    public List<Building> getAllBuildings() {
        List<Building> list = new ArrayList<>();
        String sql = "SELECT * FROM building";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Building b = new Building();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                list.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 根据楼宇ID，获取该楼里【未住满】的房间
    public List<DormRoom> getAvailableRooms(int buildingId) {
        List<DormRoom> list = new ArrayList<>();
        // 关键逻辑：current_capacity < max_capacity
        String sql = "SELECT * FROM dorm_room WHERE building_id = ? AND current_capacity < max_capacity";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DormRoom room = new DormRoom();
                    room.setId(rs.getInt("id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    list.add(room);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 【新功能】查询某栋楼的所有房间 (用于可视化展示)
     */
    public List<DormRoom> getRoomsByBuildingId(int buildingId) {
        List<DormRoom> list = new ArrayList<>();
        // 这里的 SQL 很重要，必须查出 max_capacity 和 current_capacity
        String sql = "SELECT * FROM dorm_room WHERE building_id = ? ORDER BY room_number ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DormRoom room = new DormRoom();
                    room.setId(rs.getInt("id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    // 设置容量信息，决定后续显示的颜色
                    room.setMaxCapacity(rs.getInt("max_capacity"));
                    room.setCurrentCapacity(rs.getInt("current_capacity"));
                    list.add(room);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 【新功能】查询某个房间里的所有学生 (用于弹窗显示详情)
     */
    public List<String> getStudentNamesByRoomId(int roomId) {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name, student_no FROM student WHERE dorm_room_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // 格式：2024001 - 张三
                    names.add(rs.getString("student_no") + " - " + rs.getString("name"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return names;
    }
}