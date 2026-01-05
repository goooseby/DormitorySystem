package com.scut.dormitory.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // 1. 数据库配置 (如果你的端口不是3306，请修改)
    // serverTimezone=GMT%2B8 是为了解决时区报错问题
    private static final String URL = "jdbc:mysql://localhost:3306/dormitory_system?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf8";
    private static final String USER = "root";     // 你的数据库用户名
    private static final String PASSWORD = "Admin123!"; // ⚠️ 改成你自己的数据库密码！

    // 2. 加载驱动 (静态代码块，只执行一次)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(" 数据库驱动加载失败！请检查 pom.xml");
        }
    }

    // 3. 获取连接的方法
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 4. 测试用的 main 方法 (这一步是为了测试连接是否成功)
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println(" 恭喜！数据库连接成功！");
        } catch (SQLException e) {
            System.out.println(" 连接失败！请检查数据库名、用户名或密码。");
            e.printStackTrace();
        }
    }
}