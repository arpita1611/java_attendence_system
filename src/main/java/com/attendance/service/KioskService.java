package com.attendance.service;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.dto.KioskScanRequestDto;
import com.attendance.dto.KioskScanResponseDto;
import com.attendance.model.*;
import com.attendance.repository.EmployeeAttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.StudentAttendanceRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class KioskService {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;
    private final StudentService studentService;
    private final EmployeeService employeeService;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public KioskService(StudentRepository studentRepository,
                        EmployeeRepository employeeRepository,
                        StudentAttendanceRepository studentAttendanceRepository,
                        EmployeeAttendanceRepository employeeAttendanceRepository,
                        StudentService studentService,
                        EmployeeService employeeService) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
        this.studentService = studentService;
        this.employeeService = employeeService;
    }

    @Transactional
    public KioskScanResponseDto processScan(KioskScanRequestDto request) {
        if (request == null || request.getCode() == null || request.getCode().trim().isEmpty()) {
            return KioskScanResponseDto.error("Please provide a valid Roll Number or Employee Code");
        }

        String rawCode = request.getCode().trim();
        String mode = request.getMode() != null ? request.getMode().toUpperCase() : "STUDENT_CHECKIN";
        LocalDate today = LocalDate.now();
        String timeStr = LocalTime.now().format(TIME_FORMATTER);

        try {
            if ("STUDENT_CHECKIN".equalsIgnoreCase(mode)) {
                Optional<Student> studentOpt = studentRepository.findByRollNumberIgnoreCase(rawCode);
                if (studentOpt.isEmpty()) {
                    return KioskScanResponseDto.notFound(rawCode, "Student not found with Roll Number: " + rawCode);
                }

                Student student = studentOpt.get();
                if (!student.isActive()) {
                    return KioskScanResponseDto.error("Student " + student.getFullName() + " is currently INACTIVE in system.");
                }

                Optional<StudentAttendance> existing = studentAttendanceRepository
                        .findByStudentIdAndAttendanceDate(student.getId(), today);

                StudentAttendance sa = existing.orElseGet(() -> new StudentAttendance(student, today, AttendanceStatus.PRESENT, ""));
                sa.setStatus(AttendanceStatus.PRESENT);
                sa.setRemarks("Kiosk Scan at " + timeStr);
                sa.setRecordedAt(LocalDateTime.now());
                studentAttendanceRepository.save(sa);

                AttendanceSummaryDto summary = studentService.getStudentAttendanceSummary(student.getId());

                return KioskScanResponseDto.success(
                        "STUDENT",
                        student.getRollNumber(),
                        student.getFullName(),
                        student.getDepartment() != null ? student.getDepartment().getName() : "General",
                        student.getSemester() + (student.getSection() != null ? " (" + student.getSection() + ")" : ""),
                        timeStr,
                        summary.getAttendancePercentage(),
                        "Attendance Marked Present for Today!"
                );

            } else if ("EMPLOYEE_CHECKIN".equalsIgnoreCase(mode)) {
                Optional<Employee> empOpt = employeeRepository.findByEmployeeCodeIgnoreCase(rawCode);
                if (empOpt.isEmpty()) {
                    return KioskScanResponseDto.notFound(rawCode, "Employee not found with Code: " + rawCode);
                }

                Employee employee = empOpt.get();
                if (!employee.isActive()) {
                    return KioskScanResponseDto.error("Employee " + employee.getFullName() + " is marked INACTIVE.");
                }

                Optional<EmployeeAttendance> existing = employeeAttendanceRepository
                        .findByEmployeeIdAndAttendanceDate(employee.getId(), today);

                EmployeeAttendance ea = existing.orElseGet(() -> new EmployeeAttendance(employee, today, AttendanceStatus.PRESENT, null, null, ""));
                ea.setStatus(AttendanceStatus.PRESENT);
                if (ea.getCheckInTime() == null) {
                    ea.setCheckInTime(LocalTime.now().withNano(0));
                }
                ea.setRemarks("Kiosk Check-In at " + timeStr);
                ea.setRecordedAt(LocalDateTime.now());
                employeeAttendanceRepository.save(ea);

                AttendanceSummaryDto summary = employeeService.getEmployeeAttendanceSummary(employee.getId());

                return KioskScanResponseDto.success(
                        "EMPLOYEE",
                        employee.getEmployeeCode(),
                        employee.getFullName(),
                        employee.getDepartment() != null ? employee.getDepartment().getName() : "Administration",
                        employee.getDesignation(),
                        timeStr,
                        summary.getAttendancePercentage(),
                        "Check-In Recorded Successfully at " + timeStr + "!"
                );

            } else if ("EMPLOYEE_CHECKOUT".equalsIgnoreCase(mode)) {
                Optional<Employee> empOpt = employeeRepository.findByEmployeeCodeIgnoreCase(rawCode);
                if (empOpt.isEmpty()) {
                    return KioskScanResponseDto.notFound(rawCode, "Employee not found with Code: " + rawCode);
                }

                Employee employee = empOpt.get();
                Optional<EmployeeAttendance> existing = employeeAttendanceRepository
                        .findByEmployeeIdAndAttendanceDate(employee.getId(), today);

                EmployeeAttendance ea = existing.orElseGet(() -> new EmployeeAttendance(employee, today, AttendanceStatus.PRESENT, null, null, ""));
                ea.setStatus(AttendanceStatus.PRESENT);
                ea.setCheckOutTime(LocalTime.now().withNano(0));
                ea.setRemarks("Kiosk Check-Out at " + timeStr);
                employeeAttendanceRepository.save(ea);

                AttendanceSummaryDto summary = employeeService.getEmployeeAttendanceSummary(employee.getId());

                return KioskScanResponseDto.success(
                        "EMPLOYEE",
                        employee.getEmployeeCode(),
                        employee.getFullName(),
                        employee.getDepartment() != null ? employee.getDepartment().getName() : "Administration",
                        employee.getDesignation(),
                        timeStr,
                        summary.getAttendancePercentage(),
                        "Check-Out Recorded Successfully at " + timeStr + "!"
                );
            }

            return KioskScanResponseDto.error("Unknown scan mode: " + mode);

        } catch (Exception e) {
            return KioskScanResponseDto.error("System error processing scan: " + e.getMessage());
        }
    }
}
