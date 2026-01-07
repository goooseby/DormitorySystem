package com.scut.dormitory.model;

public class DormRoom {
    private int id;
    private String roomNumber;
    private int maxCapacity;     // 新增：最大床位
    private int currentCapacity; // 新增：当前已住人数

    // 必须重写 toString，因为某些下拉框可能用到
    @Override
    public String toString() {
        return roomNumber;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(int currentCapacity) { this.currentCapacity = currentCapacity; }
}