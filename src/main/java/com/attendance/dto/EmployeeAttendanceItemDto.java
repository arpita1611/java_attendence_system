package com.attendance.dto;

import com.attendance.model.AttendanceStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

public class EmployeeAttendanceItemDto {
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String designation;
    private AttendanceStatus status = AttendanceStatus.PRESENT;
    
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkInTime = LocalTime.of(9, 0);
    
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkOutTime = LocalTime.of(17, 30);
    
    private String remarks;

    public EmployeeAttendanceItemDto() {
    }

    public EmployeeAttendanceItemDto(Long employeeId, String employeeCode, String employeeName, String designation,
                                   AttendanceStatus status, LocalTime checkInTime, LocalTime checkOutTime, String remarks) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.designation = designation;
        this.status = status != null ? status : AttendanceStatus.PRESENT;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.remarks = remarks;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
