package com.scut.dormitory.model;

public class Building {
    private int id;
    private String name;

    // 必须重写 toString，因为下拉框(ComboBox)显示的就是这个方法的返回值
    @Override
    public String toString() {
        return name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}