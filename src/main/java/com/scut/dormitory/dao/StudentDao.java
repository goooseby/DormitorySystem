package com.scut.dormitory.dao;

import com.scut.dormitory.model.Student;
import com.scut.dormitory.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    /**
     * 查询所有学生列表 (带搜索功能)
     * @param keyword 搜索关键字 (学号或姓名)，如果为空则查所有
     */
    public List<Student> list(String keyword) {
        List<Student> list = new ArrayList<>();

        // 核心 SQL：三表联查 (Student + DormRoom + Building)
        // 这样我们才能知道学生住在 "C1栋 305"
        StringBuilder sql = new StringBuilder(
                "SELECT s.*, dr.room_number, b.name as building_name " +
                        "FROM student s " +
                        "LEFT JOIN dorm_room dr ON s.dorm_room_id = dr.id " +
                        "LEFT JOIN building b ON dr.building_id = b.id " +
                        "WHERE 1=1 "
        );

        // 如果有搜索关键字，拼接 SQL
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (s.name LIKE ? OR s.student_no LIKE ?) ");
        }

        sql.append(" ORDER BY s.student_no ASC"); // 按学号排序

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%"); // 模糊查询
                pstmt.setString(2, "%" + keyword + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student stu = new Student();
                    stu.setId(rs.getInt("id"));
                    stu.setStudentNo(rs.getString("student_no"));
                    stu.setName(rs.getString("name"));
                    stu.setGender(rs.getString("gender"));
                    stu.setState(rs.getString("state"));
                    stu.setDormRoomId(rs.getInt("dorm_room_id"));

                    // 获取 JOIN 出来的房间和楼宇信息
                    stu.setRoomName(rs.getString("room_number"));
                    stu.setBuildingName(rs.getString("building_name"));

                    list.add(stu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 新增学生 (事务操作：插入学生 + 房间人数+1)
     */
    public boolean add(Student student) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 插入学生
            String sql = "INSERT INTO student (student_no, name, gender, dorm_room_id, state) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, student.getStudentNo());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getGender());
                pstmt.setInt(4, student.getDormRoomId());
                pstmt.setString(5, "入住");
                pstmt.executeUpdate();
            }

            // 2. 更新房间人数 (+1)
            String updateRoom = "UPDATE dorm_room SET current_capacity = current_capacity + 1 WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateRoom)) {
                pstmt.setInt(1, student.getDormRoomId());
                pstmt.executeUpdate();
            }

            conn.commit(); // 提交
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 更新学生信息 (包含换房逻辑)
     * @param student 新的学生信息
     * @param oldRoomId 学生原本住的房间ID (用于判断是否换房)
     */
    public boolean update(Student student, int oldRoomId) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 判断是否换了房间
            if (student.getDormRoomId() != oldRoomId) {
                // A. 旧房间人数 -1
                String sqlDec = "UPDATE dorm_room SET current_capacity = current_capacity - 1 WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDec)) {
                    ps.setInt(1, oldRoomId);
                    ps.executeUpdate();
                }
                // B. 新房间人数 +1
                String sqlInc = "UPDATE dorm_room SET current_capacity = current_capacity + 1 WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlInc)) {
                    ps.setInt(1, student.getDormRoomId());
                    ps.executeUpdate();
                }
            }

            // 2. 更新学生基本信息
            String sqlUpdate = "UPDATE student SET student_no=?, name=?, gender=?, dorm_room_id=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setString(1, student.getStudentNo());
                ps.setString(2, student.getName());
                ps.setString(3, student.getGender());
                ps.setInt(4, student.getDormRoomId());
                ps.setInt(5, student.getId()); // WHERE id = ?
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 根据学号查找学生ID
     * @return 如果找到返回ID，找不到返回 -1
     */
    public int findIdByStudentNo(String studentNo) {
        String sql = "SELECT id FROM student WHERE student_no = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // -1 代表没找到
    }

    /**
     * 删除学生，并更新对应房间的入住人数
     */
    public boolean delete(int studentId, int dormRoomId) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务 (保证两步操作同时成功或同时失败)

            // 1. 删除学生
            String deleteSql = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, studentId);
                int affected = pstmt.executeUpdate();
                if (affected == 0) throw new SQLException("删除失败，未找到学生");
            }

            // 2. 更新房间人数 (减1)
            if (dormRoomId > 0) { // 如果该学生分配了房间
                String updateRoomSql = "UPDATE dorm_room SET current_capacity = current_capacity - 1 WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateRoomSql)) {
                    pstmt.setInt(1, dormRoomId);
                    pstmt.executeUpdate();
                }
            }

            conn.commit(); // 提交事务
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // 报错了就回滚，防止数据不一致
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // 恢复默认
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}