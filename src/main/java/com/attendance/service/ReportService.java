package com.attendance.service;

import com.attendance.dto.MonthlyMatrixRowDto;
import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public ReportService(StudentRepository studentRepository,
                         EmployeeRepository employeeRepository,
                         StudentAttendanceRepository studentAttendanceRepository,
                         EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    public List<StudentAttendance> filterStudentAttendance(Long departmentId, String semester,
                                                          AttendanceStatus status, LocalDate startDate, LocalDate endDate) {
        String cleanSemester = (semester != null && !semester.trim().isEmpty()) ? semester.trim() : null;
        return studentAttendanceRepository.filterAttendance(departmentId, cleanSemester, status, startDate, endDate);
    }

    public List<EmployeeAttendance> filterEmployeeAttendance(Long departmentId, AttendanceStatus status,
                                                            LocalDate startDate, LocalDate endDate) {
        return employeeAttendanceRepository.filterAttendance(departmentId, status, startDate, endDate);
    }

    public List<MonthlyMatrixRowDto> getStudentMonthlyMatrix(int year, int month, Long departmentId, String semester) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        int daysInMonth = yearMonth.lengthOfMonth();

        List<Student> students;
        if (departmentId != null && semester != null && !semester.trim().isEmpty()) {
            students = studentRepository.findByDepartmentIdAndSemesterAndActiveTrueOrderByRollNumberAsc(departmentId, semester);
        } else if (departmentId != null) {
            students = studentRepository.findByDepartmentIdAndActiveTrueOrderByRollNumberAsc(departmentId);
        } else {
            students = studentRepository.findByActiveTrueOrderByRollNumberAsc();
        }

        List<MonthlyMatrixRowDto> matrix = new ArrayList<>();
        for (Student s : students) {
            MonthlyMatrixRowDto row = new MonthlyMatrixRowDto(
                    s.getId(),
                    s.getRollNumber(),
                    s.getFullName(),
                    s.getDepartment() != null ? s.getDepartment().getName() : "N/A",
                    s.getSemester() != null ? s.getSemester() : ""
            );

            List<StudentAttendance> records = studentAttendanceRepository
                    .findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(s.getId(), startDate, endDate);

            for (StudentAttendance rec : records) {
                row.addDayStatus(rec.getAttendanceDate().getDayOfMonth(), rec.getStatus());
            }

            row.calculateRate(daysInMonth);
            matrix.add(row);
        }

        return matrix;
    }

    public List<MonthlyMatrixRowDto> getEmployeeMonthlyMatrix(int year, int month, Long departmentId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        int daysInMonth = yearMonth.lengthOfMonth();

        List<Employee> employees;
        if (departmentId != null) {
            employees = employeeRepository.findByDepartmentIdAndActiveTrueOrderByEmployeeCodeAsc(departmentId);
        } else {
            employees = employeeRepository.findByActiveTrueOrderByEmployeeCodeAsc();
        }

        List<MonthlyMatrixRowDto> matrix = new ArrayList<>();
        for (Employee e : employees) {
            MonthlyMatrixRowDto row = new MonthlyMatrixRowDto(
                    e.getId(),
                    e.getEmployeeCode(),
                    e.getFullName(),
                    e.getDepartment() != null ? e.getDepartment().getName() : "N/A",
                    e.getDesignation() != null ? e.getDesignation() : ""
            );

            List<EmployeeAttendance> records = employeeAttendanceRepository
                    .findByEmployeeIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(e.getId(), startDate, endDate);

            for (EmployeeAttendance rec : records) {
                row.addDayStatus(rec.getAttendanceDate().getDayOfMonth(), rec.getStatus());
            }

            row.calculateRate(daysInMonth);
            matrix.add(row);
        }

        return matrix;
    }

    // CSV Generation Utility
    public ByteArrayInputStream generateStudentAttendanceCsv(List<StudentAttendance> records) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Date,Roll Number,Student Name,Department,Semester,Section,Status,Remarks");
            for (StudentAttendance r : records) {
                Student s = r.getStudent();
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        r.getAttendanceDate(),
                        s.getRollNumber(),
                        s.getFullName(),
                        s.getDepartment() != null ? s.getDepartment().getName() : "",
                        s.getSemester() != null ? s.getSemester() : "",
                        s.getSection() != null ? s.getSection() : "",
                        r.getStatus().getDisplayName(),
                        r.getRemarks() != null ? r.getRemarks().replace("\"", "\"\"") : ""
                );
            }
            writer.flush();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateEmployeeAttendanceCsv(List<EmployeeAttendance> records) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Date,Employee Code,Employee Name,Department,Designation,Status,Check In,Check Out,Remarks");
            for (EmployeeAttendance r : records) {
                Employee e = r.getEmployee();
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        r.getAttendanceDate(),
                        e.getEmployeeCode(),
                        e.getFullName(),
                        e.getDepartment() != null ? e.getDepartment().getName() : "",
                        e.getDesignation() != null ? e.getDesignation() : "",
                        r.getStatus().getDisplayName(),
                        r.getCheckInTime() != null ? r.getCheckInTime().toString() : "",
                        r.getCheckOutTime() != null ? r.getCheckOutTime().toString() : "",
                        r.getRemarks() != null ? r.getRemarks().replace("\"", "\"\"") : ""
                );
            }
            writer.flush();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
