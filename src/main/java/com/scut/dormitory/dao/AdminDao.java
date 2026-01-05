package com.scut.dormitory.dao;

import com.scut.dormitory.model.Admin;
import com.scut.dormitory.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {

    /**
     * 登录验证方法
     * @param username 用户输入的账号
     * @param password 用户输入的密码
     * @return 如果登录成功返回 Admin 对象，失败返回 null
     */
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        // try-with-resources 写法，自动关闭连接，防止内存泄漏
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 设置 SQL 参数 (防止 SQL 注入攻击，这是加分点！)
            stmt.setString(1, username);
            stmt.setString(2, password);

            // 执行查询
            ResultSet rs = stmt.executeQuery();

            // 如果查到了结果
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setName(rs.getString("name"));
                return admin; // 登录成功
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 登录失败
    }

    // 简单的测试主函数
    public static void main(String[] args) {
        AdminDao dao = new AdminDao();
        // 模拟测试
        Admin a1 = dao.login("admin", "123456");
        System.out.println("测试1 (正确密码): " + (a1 != null ? "成功 " + a1.getName() : "失败"));

        Admin a2 = dao.login("admin", "wrong_password");
        System.out.println("测试2 (错误密码): " + (a2 != null ? "成功" : "失败 (符合预期)"));
    }
}