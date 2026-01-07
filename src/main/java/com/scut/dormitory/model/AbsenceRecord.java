package com.scut.dormitory.model;

public class AbsenceRecord {
    private int id;
    private int studentId;
    private String date;   // 为了方便显示，直接用 String 存日期 (yyyy-MM-dd)
    private String detail; // 缺勤原因
    private int adminId;

    // --- 额外字段 (用于界面显示) ---
    private String studentName;
    private String studentNo;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    // 额外字段的 Getter/Setter
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
}