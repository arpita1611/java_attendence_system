package com.attendance.dto;

import com.attendance.model.AttendanceStatus;
import java.util.HashMap;
import java.util.Map;

public class MonthlyMatrixRowDto {
    private Long id;
    private String identifier; // Roll Number or Employee Code
    private String name;
    private String departmentName;
    private String subInfo; // Semester or Designation
    private Map<Integer, AttendanceStatus> dailyStatus = new HashMap<>();
    private int presentCount = 0;
    private int absentCount = 0;
    private int lateCount = 0;
    private int leaveCount = 0;
    private double attendanceRate = 0.0;

    public MonthlyMatrixRowDto() {
    }

    public MonthlyMatrixRowDto(Long id, String identifier, String name, String departmentName, String subInfo) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.departmentName = departmentName;
        this.subInfo = subInfo;
    }

    public void addDayStatus(int day, AttendanceStatus status) {
        dailyStatus.put(day, status);
        if (status == AttendanceStatus.PRESENT) {
            presentCount++;
        } else if (status == AttendanceStatus.ABSENT) {
            absentCount++;
        } else if (status == AttendanceStatus.LATE) {
            lateCount++;
        } else if (status == AttendanceStatus.ON_LEAVE || status == AttendanceStatus.EXCUSED || status == AttendanceStatus.HALF_DAY) {
            leaveCount++;
        }
    }

    public void calculateRate(int totalWorkingDays) {
        if (totalWorkingDays > 0) {
            double effectivePresent = presentCount + lateCount + (leaveCount * 0.5);
            this.attendanceRate = Math.round((effectivePresent / totalWorkingDays) * 100.0 * 10.0) / 10.0;
        } else {
            this.attendanceRate = 0.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(String subInfo) {
        this.subInfo = subInfo;
    }

    public Map<Integer, AttendanceStatus> getDailyStatus() {
        return dailyStatus;
    }

    public void setDailyStatus(Map<Integer, AttendanceStatus> dailyStatus) {
        this.dailyStatus = dailyStatus;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
}
