package com.attendance.model;

public enum AttendanceStatus {
    PRESENT("Present", "success", "P"),
    ABSENT("Absent", "danger", "A"),
    LATE("Late", "warning", "L"),
    EXCUSED("Excused", "info", "E"),
    HALF_DAY("Half Day", "primary", "HD"),
    ON_LEAVE("On Leave", "secondary", "OL");

    private final String displayName;
    private final String badgeClass;
    private final String shortCode;

    AttendanceStatus(String displayName, String badgeClass, String shortCode) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
        this.shortCode = shortCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getShortCode() {
        return shortCode;
    }
}
