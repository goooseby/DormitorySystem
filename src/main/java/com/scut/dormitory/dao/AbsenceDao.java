package com.scut.dormitory.dao;

import com.scut.dormitory.model.AbsenceRecord;
import com.scut.dormitory.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDao {

    /**
     * 查询缺勤记录 (支持按学生姓名或学号搜索)
     */
    public List<AbsenceRecord> list(String keyword) {
        List<AbsenceRecord> list = new ArrayList<>();

        // SQL 重点：关联 student 表，获取 name 和 student_no
        StringBuilder sql = new StringBuilder(
                "SELECT ar.*, s.name, s.student_no " +
                        "FROM absence_record ar " +
                        "LEFT JOIN student s ON ar.student_id = s.id " +
                        "WHERE 1=1 "
        );

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (s.name LIKE ? OR s.student_no LIKE ?) ");
        }

        sql.append(" ORDER BY ar.date DESC"); // 按日期倒序，最近的在前面

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AbsenceRecord record = new AbsenceRecord();
                    record.setId(rs.getInt("id"));
                    record.setStudentId(rs.getInt("student_id"));
                    record.setDate(rs.getString("date"));
                    record.setDetail(rs.getString("detail"));

                    // 获取 JOIN 出来的信息
                    record.setStudentName(rs.getString("name"));
                    record.setStudentNo(rs.getString("student_no"));

                    list.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean add(AbsenceRecord record) {
        String sql = "INSERT INTO absence_record (student_id, date, detail, admin_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getStudentId());
            pstmt.setString(2, record.getDate());
            pstmt.setString(3, record.getDetail());
            pstmt.setInt(4, 1); // 暂时默认管理员ID为1，简化逻辑
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}