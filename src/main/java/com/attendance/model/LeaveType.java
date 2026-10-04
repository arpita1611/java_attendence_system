package com.attendance.model;

public enum LeaveType {
    CASUAL("Casual Leave"),
    MEDICAL("Medical / Sick Leave"),
    DUTY_LEAVE("Academic / Duty Leave"),
    ON_DUTY("On Duty Official"),
    PERSONAL("Personal Leave"),
    OTHER("Other Leave");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
