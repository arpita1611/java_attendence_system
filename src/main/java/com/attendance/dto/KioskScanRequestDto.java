package com.attendance.dto;

public class KioskScanRequestDto {
    private String code;
    private String mode = "STUDENT_CHECKIN"; // "STUDENT_CHECKIN", "EMPLOYEE_CHECKIN", "EMPLOYEE_CHECKOUT"
    private String date;

    public KioskScanRequestDto() {
    }

    public KioskScanRequestDto(String code, String mode, String date) {
        this.code = code;
        this.mode = mode;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
