package com.attendance.service;

import com.attendance.dto.DashboardStatsDto;
import com.attendance.model.AttendanceStatus;
import com.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public DashboardService(StudentRepository studentRepository,
                            EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository,
                            StudentAttendanceRepository studentAttendanceRepository,
                            EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        // 1. Overall Counts
        long totalStudents = studentRepository.countByActiveTrue();
        long totalEmployees = employeeRepository.countByActiveTrue();
        long totalDepartments = departmentRepository.count();

        stats.setTotalStudents(totalStudents);
        stats.setTotalEmployees(totalEmployees);
        stats.setTotalDepartments(totalDepartments);

        LocalDate today = LocalDate.now();

        // 2. Today's Student Stats
        long sTotal = studentAttendanceRepository.countByAttendanceDate(today);
        long sPresent = studentAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.PRESENT);
        long sAbsent = studentAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.ABSENT);
        long sLate = studentAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.LATE);

        stats.setStudentTotalToday(sTotal);
        stats.setStudentPresentToday(sPresent);
        stats.setStudentAbsentToday(sAbsent);
        stats.setStudentLateToday(sLate);

        if (sTotal > 0) {
            double rate = ((double) (sPresent + sLate) / sTotal) * 100.0;
            stats.setStudentAttendanceRate(Math.round(rate * 10.0) / 10.0);
        } else {
            stats.setStudentAttendanceRate(0.0);
        }

        // 3. Today's Employee Stats
        long eTotal = employeeAttendanceRepository.countByAttendanceDate(today);
        long ePresent = employeeAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.PRESENT);
        long eAbsent = employeeAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.ABSENT);
        long eLeave = employeeAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.ON_LEAVE)
                + employeeAttendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.EXCUSED);

        stats.setEmployeeTotalToday(eTotal);
        stats.setEmployeePresentToday(ePresent);
        stats.setEmployeeAbsentToday(eAbsent);
        stats.setEmployeeOnLeaveToday(eLeave);

        if (eTotal > 0) {
            double rate = ((double) ePresent / eTotal) * 100.0;
            stats.setEmployeeAttendanceRate(Math.round(rate * 10.0) / 10.0);
        } else {
            stats.setEmployeeAttendanceRate(0.0);
        }

        // 4. Last 7 Days Trend for Chart.js
        List<String> labels = new ArrayList<>();
        List<Double> studentTrends = new ArrayList<>();
        List<Double> employeeTrends = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            labels.add(day.format(formatter));

            long sDayTotal = studentAttendanceRepository.countByAttendanceDate(day);
            long sDayPresent = studentAttendanceRepository.countByAttendanceDateAndStatus(day, AttendanceStatus.PRESENT)
                    + studentAttendanceRepository.countByAttendanceDateAndStatus(day, AttendanceStatus.LATE);
            double sRate = (sDayTotal > 0) ? Math.round(((double) sDayPresent / sDayTotal) * 100.0 * 10.0) / 10.0 : 0.0;
            studentTrends.add(sRate);

            long eDayTotal = employeeAttendanceRepository.countByAttendanceDate(day);
            long eDayPresent = employeeAttendanceRepository.countByAttendanceDateAndStatus(day, AttendanceStatus.PRESENT);
            double eRate = (eDayTotal > 0) ? Math.round(((double) eDayPresent / eDayTotal) * 100.0 * 10.0) / 10.0 : 0.0;
            employeeTrends.add(eRate);
        }

        stats.setChartLabels(labels);
        stats.setStudentTrendRates(studentTrends);
        stats.setEmployeeTrendRates(employeeTrends);

        return stats;
    }
}
