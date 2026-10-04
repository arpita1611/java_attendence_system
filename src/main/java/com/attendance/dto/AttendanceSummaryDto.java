package com.attendance.dto;

public class AttendanceSummaryDto {
    private long totalRecords;
    private long presentCount;
    private long absentCount;
    private long lateCount;
    private long excusedCount;
    private long halfDayCount;
    private long onLeaveCount;
    private double attendancePercentage;

    public AttendanceSummaryDto() {
    }

    public AttendanceSummaryDto(long totalRecords, long presentCount, long absentCount, long lateCount,
                                long excusedCount, long halfDayCount, long onLeaveCount) {
        this.totalRecords = totalRecords;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.lateCount = lateCount;
        this.excusedCount = excusedCount;
        this.halfDayCount = halfDayCount;
        this.onLeaveCount = onLeaveCount;
        
        // Present + Late + Excused + 0.5 * HalfDay counts towards attendance rate
        if (totalRecords > 0) {
            double effectivePresent = presentCount + lateCount + excusedCount + (halfDayCount * 0.5);
            this.attendancePercentage = Math.round((effectivePresent / totalRecords) * 100.0 * 10.0) / 10.0;
        } else {
            this.attendancePercentage = 0.0;
        }
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(long presentCount) {
        this.presentCount = presentCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(long absentCount) {
        this.absentCount = absentCount;
    }

    public long getLateCount() {
        return lateCount;
    }

    public void setLateCount(long lateCount) {
        this.lateCount = lateCount;
    }

    public long getExcusedCount() {
        return excusedCount;
    }

    public void setExcusedCount(long excusedCount) {
        this.excusedCount = excusedCount;
    }

    public long getHalfDayCount() {
        return halfDayCount;
    }

    public void setHalfDayCount(long halfDayCount) {
        this.halfDayCount = halfDayCount;
    }

    public long getOnLeaveCount() {
        return onLeaveCount;
    }

    public void setOnLeaveCount(long onLeaveCount) {
        this.onLeaveCount = onLeaveCount;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}
