package com.attendance.dto;

import com.attendance.model.AttendanceStatus;

public class StudentAttendanceItemDto {
    private Long studentId;
    private String rollNumber;
    private String studentName;
    private String semester;
    private String section;
    private AttendanceStatus status = AttendanceStatus.PRESENT;
    private String remarks;

    public StudentAttendanceItemDto() {
    }

    public StudentAttendanceItemDto(Long studentId, String rollNumber, String studentName, String semester, String section, AttendanceStatus status, String remarks) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.semester = semester;
        this.section = section;
        this.status = status != null ? status : AttendanceStatus.PRESENT;
        this.remarks = remarks;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
