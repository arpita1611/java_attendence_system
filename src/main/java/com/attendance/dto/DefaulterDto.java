package com.attendance.dto;

public class DefaulterDto {
    private Long studentId;
    private String rollNumber;
    private String studentName;
    private String departmentName;
    private String semester;
    private String section;
    private String email;
    private String phone;
    private long totalSessions;
    private long attendedSessions;
    private long absentSessions;
    private long lateSessions;
    private double attendancePercentage;
    private int classesNeededFor75;

    public DefaulterDto() {
    }

    public DefaulterDto(Long studentId, String rollNumber, String studentName, String departmentName,
                        String semester, String section, String email, String phone,
                        long totalSessions, long attendedSessions, long absentSessions, long lateSessions,
                        double attendancePercentage) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.departmentName = departmentName;
        this.semester = semester;
        this.section = section;
        this.email = email;
        this.phone = phone;
        this.totalSessions = totalSessions;
        this.attendedSessions = attendedSessions;
        this.absentSessions = absentSessions;
        this.lateSessions = lateSessions;
        this.attendancePercentage = attendancePercentage;
        this.classesNeededFor75 = calculateClassesNeeded(0.75);
    }

    public int calculateClassesNeeded(double targetRatio) {
        if (totalSessions == 0) return 0;
        if ((double) attendedSessions / totalSessions >= targetRatio) {
            return 0;
        }
        double needed = (targetRatio * totalSessions - attendedSessions) / (1.0 - targetRatio);
        return (int) Math.ceil(Math.max(0, needed));
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(long totalSessions) {
        this.totalSessions = totalSessions;
    }

    public long getAttendedSessions() {
        return attendedSessions;
    }

    public void setAttendedSessions(long attendedSessions) {
        this.attendedSessions = attendedSessions;
    }

    public long getAbsentSessions() {
        return absentSessions;
    }

    public void setAbsentSessions(long absentSessions) {
        this.absentSessions = absentSessions;
    }

    public long getLateSessions() {
        return lateSessions;
    }

    public void setLateSessions(long lateSessions) {
        this.lateSessions = lateSessions;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getClassesNeededFor75() {
        return classesNeededFor75;
    }

    public void setClassesNeededFor75(int classesNeededFor75) {
        this.classesNeededFor75 = classesNeededFor75;
    }
}
