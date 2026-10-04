package com.attendance.controller;

import com.attendance.dto.MonthlyMatrixRowDto;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.EmployeeAttendance;
import com.attendance.model.StudentAttendance;
import com.attendance.service.DepartmentService;
import com.attendance.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final DepartmentService departmentService;

    public ReportController(ReportService reportService, DepartmentService departmentService) {
        this.reportService = reportService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String reportsHub(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("currentMonth", LocalDate.now().getMonthValue());
        return "reports/index";
    }

    @GetMapping("/students")
    public String studentReport(
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "status", required = false) AttendanceStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(14);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<StudentAttendance> records = reportService.filterStudentAttendance(departmentId, semester, status, startDate, endDate);

        model.addAttribute("records", records);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "reports/student-report";
    }

    @GetMapping("/employees")
    public String employeeReport(
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "status", required = false) AttendanceStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(14);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<EmployeeAttendance> records = reportService.filterEmployeeAttendance(departmentId, status, startDate, endDate);

        model.addAttribute("records", records);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "reports/employee-report";
    }

    @GetMapping("/students/matrix")
    public String studentMatrix(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester,
            Model model) {

        LocalDate now = LocalDate.now();
        if (year == null) year = now.getYear();
        if (month == null) month = now.getMonthValue();

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        List<MonthlyMatrixRowDto> matrix = reportService.getStudentMonthlyMatrix(year, month, departmentId, semester);

        model.addAttribute("matrix", matrix);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("monthName", ym.getMonth().name());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedSemester", semester);

        return "reports/student-matrix";
    }

    @GetMapping("/employees/matrix")
    public String employeeMatrix(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            Model model) {

        LocalDate now = LocalDate.now();
        if (year == null) year = now.getYear();
        if (month == null) month = now.getMonthValue();

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        List<MonthlyMatrixRowDto> matrix = reportService.getEmployeeMonthlyMatrix(year, month, departmentId);

        model.addAttribute("matrix", matrix);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("monthName", ym.getMonth().name());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);

        return "reports/employee-matrix";
    }

    @GetMapping("/export/students/csv")
    public ResponseEntity<InputStreamResource> exportStudentsCsv(
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "status", required = false) AttendanceStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        List<StudentAttendance> records = reportService.filterStudentAttendance(departmentId, semester, status, startDate, endDate);
        ByteArrayInputStream in = reportService.generateStudentAttendanceCsv(records);

        String filename = "student_attendance_" + startDate + "_to_" + endDate + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/export/employees/csv")
    public ResponseEntity<InputStreamResource> exportEmployeesCsv(
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "status", required = false) AttendanceStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        List<EmployeeAttendance> records = reportService.filterEmployeeAttendance(departmentId, status, startDate, endDate);
        ByteArrayInputStream in = reportService.generateEmployeeAttendanceCsv(records);

        String filename = "employee_attendance_" + startDate + "_to_" + endDate + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(in));
    }
}
