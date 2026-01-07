package com.scut.dormitory.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // 数据库配置 (注意数据库名是 dormitory_system)
    private static final String URL = "jdbc:mysql://localhost:3306/dormitory_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root"; // 默认账号
    private static final String PASSWORD = "Admin123!"; //

    // 加载驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("错误：未找到 MySQL 驱动，请检查 pom.xml 是否引入了 mysql-connector-j");
        }
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}