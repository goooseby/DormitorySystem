package com.scut.dormitory.model;

public class Student {
    private int id;
    private String studentNo; // 学号
    private String name;
    private String gender;
    private int dormRoomId;
    private String state; // 入住/迁出

    // --- 额外增加的显示字段 (数据库没有，但查询时会为了显示方便Join出来) ---
    private String roomName;     // 房间号 (如 305)
    private String buildingName; // 楼宇名 (如 C1栋)

    // 无参构造
    public Student() {}

    // Getter Setter (JavaFX 表格非常依赖 Getter，请务必保证命名规范)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getDormRoomId() { return dormRoomId; }
    public void setDormRoomId(int dormRoomId) { this.dormRoomId = dormRoomId; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    // 额外的 Getter/Setter
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    // 方便表格显示 "C1栋 - 305"
    public String getFullRoomName() {
        if (buildingName == null || roomName == null) return "未分配";
        return buildingName + " - " + roomName;
    }
}