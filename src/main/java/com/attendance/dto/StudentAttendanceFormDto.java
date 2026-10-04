package com.attendance.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceFormDto {
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate attendanceDate = LocalDate.now();
    
    private Long departmentId;
    private String semester;
    private List<StudentAttendanceItemDto> items = new ArrayList<>();

    public StudentAttendanceFormDto() {
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<StudentAttendanceItemDto> getItems() {
        return items;
    }

    public void setItems(List<StudentAttendanceItemDto> items) {
        this.items = items;
    }
}
