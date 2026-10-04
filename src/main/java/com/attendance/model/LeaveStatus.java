package com.attendance.model;

public enum LeaveStatus {
    PENDING("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    LeaveStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
