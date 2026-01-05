package com.scut.dormitory.model;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String name;

    // 无参构造
    public Admin() {}

    // 全参构造
    public Admin(int id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    // Getter 和 Setter 方法 (IDEA 可以按 Alt+Insert 自动生成，或者直接复制这里的)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Admin{" + "name='" + name + '\'' + '}';
    }
}