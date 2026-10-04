package com.attendance.dto;

import java.util.ArrayList;
import java.util.List;

public class DashboardStatsDto {
    private long totalStudents;
    private long totalEmployees;
    private long totalDepartments;

    // Today's Student Stats
    private long studentTotalToday;
    private long studentPresentToday;
    private long studentAbsentToday;
    private long studentLateToday;
    private double studentAttendanceRate;

    // Today's Employee Stats
    private long employeeTotalToday;
    private long employeePresentToday;
    private long employeeAbsentToday;
    private long employeeOnLeaveToday;
    private double employeeAttendanceRate;

    // Chart Data (Last 7 Days)
    private List<String> chartLabels = new ArrayList<>();
    private List<Double> studentTrendRates = new ArrayList<>();
    private List<Double> employeeTrendRates = new ArrayList<>();

    public DashboardStatsDto() {
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public long getStudentTotalToday() {
        return studentTotalToday;
    }

    public void setStudentTotalToday(long studentTotalToday) {
        this.studentTotalToday = studentTotalToday;
    }

    public long getStudentPresentToday() {
        return studentPresentToday;
    }

    public void setStudentPresentToday(long studentPresentToday) {
        this.studentPresentToday = studentPresentToday;
    }

    public long getStudentAbsentToday() {
        return studentAbsentToday;
    }

    public void setStudentAbsentToday(long studentAbsentToday) {
        this.studentAbsentToday = studentAbsentToday;
    }

    public long getStudentLateToday() {
        return studentLateToday;
    }

    public void setStudentLateToday(long studentLateToday) {
        this.studentLateToday = studentLateToday;
    }

    public double getStudentAttendanceRate() {
        return studentAttendanceRate;
    }

    public void setStudentAttendanceRate(double studentAttendanceRate) {
        this.studentAttendanceRate = studentAttendanceRate;
    }

    public long getEmployeeTotalToday() {
        return employeeTotalToday;
    }

    public void setEmployeeTotalToday(long employeeTotalToday) {
        this.employeeTotalToday = employeeTotalToday;
    }

    public long getEmployeePresentToday() {
        return employeePresentToday;
    }

    public void setEmployeePresentToday(long employeePresentToday) {
        this.employeePresentToday = employeePresentToday;
    }

    public long getEmployeeAbsentToday() {
        return employeeAbsentToday;
    }

    public void setEmployeeAbsentToday(long employeeAbsentToday) {
        this.employeeAbsentToday = employeeAbsentToday;
    }

    public long getEmployeeOnLeaveToday() {
        return employeeOnLeaveToday;
    }

    public void setEmployeeOnLeaveToday(long employeeOnLeaveToday) {
        this.employeeOnLeaveToday = employeeOnLeaveToday;
    }

    public double getEmployeeAttendanceRate() {
        return employeeAttendanceRate;
    }

    public void setEmployeeAttendanceRate(double employeeAttendanceRate) {
        this.employeeAttendanceRate = employeeAttendanceRate;
    }

    public List<String> getChartLabels() {
        return chartLabels;
    }

    public void setChartLabels(List<String> chartLabels) {
        this.chartLabels = chartLabels;
    }

    public List<Double> getStudentTrendRates() {
        return studentTrendRates;
    }

    public void setStudentTrendRates(List<Double> studentTrendRates) {
        this.studentTrendRates = studentTrendRates;
    }

    public List<Double> getEmployeeTrendRates() {
        return employeeTrendRates;
    }

    public void setEmployeeTrendRates(List<Double> employeeTrendRates) {
        this.employeeTrendRates = employeeTrendRates;
    }
}
