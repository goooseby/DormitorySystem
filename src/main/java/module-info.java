module com.scut.dormitory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires atlantafx.base;

    // 1. 允许 JavaFX 访问主程序包 (MainApp 在这里)
    opens com.scut.dormitory to javafx.fxml;

    // 允许 JavaFX 访问控制器包 (LoginController 在这里)
    opens com.scut.dormitory.controller to javafx.fxml;
    opens com.scut.dormitory.model to javafx.base;

    exports com.scut.dormitory;

    // 如果以后需要在其他地方用 Utils，保持这个导出
    exports com.scut.dormitory.utils;
}