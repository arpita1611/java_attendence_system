package com.attendance.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAttendanceFormDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate attendanceDate = LocalDate.now();

    private Long departmentId;
    private List<EmployeeAttendanceItemDto> items = new ArrayList<>();

    public EmployeeAttendanceFormDto() {
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

    public List<EmployeeAttendanceItemDto> getItems() {
        return items;
    }

    public void setItems(List<EmployeeAttendanceItemDto> items) {
        this.items = items;
    }
}
