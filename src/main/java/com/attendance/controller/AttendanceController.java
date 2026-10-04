package com.attendance.controller;

import com.attendance.dto.EmployeeAttendanceFormDto;
import com.attendance.dto.StudentAttendanceFormDto;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.EmployeeAttendance;
import com.attendance.model.StudentAttendance;
import com.attendance.service.AttendanceService;
import com.attendance.service.DepartmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final DepartmentService departmentService;

    public AttendanceController(AttendanceService attendanceService,
                                DepartmentService departmentService) {
        this.attendanceService = attendanceService;
        this.departmentService = departmentService;
    }

    // ==========================================
    // Student Attendance Endpoints
    // ==========================================

    @GetMapping("/students/mark")
    public String showStudentAttendanceForm(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        StudentAttendanceFormDto form = attendanceService.getStudentAttendanceForm(date, departmentId, semester);

        model.addAttribute("form", form);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("attendanceDate", date);

        return "attendance/mark-student";
    }

    @PostMapping("/students/mark")
    public String saveStudentAttendance(@ModelAttribute("form") StudentAttendanceFormDto form,
                                        RedirectAttributes redirectAttributes) {
        try {
            attendanceService.saveStudentAttendance(form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Student attendance for " + form.getAttendanceDate() + " marked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving attendance: " + e.getMessage());
        }

        String redirectUrl = "redirect:/attendance/students/mark?date=" + form.getAttendanceDate();
        if (form.getDepartmentId() != null) {
            redirectUrl += "&departmentId=" + form.getDepartmentId();
        }
        if (form.getSemester() != null && !form.getSemester().isEmpty()) {
            redirectUrl += "&semester=" + form.getSemester();
        }
        return redirectUrl;
    }

    @GetMapping("/students/logs")
    public String showStudentLogs(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<StudentAttendance> logs = attendanceService.getStudentAttendanceByDate(date);
        model.addAttribute("logs", logs);
        model.addAttribute("selectedDate", date);
        return "attendance/student-logs";
    }

    // ==========================================
    // Employee Attendance Endpoints
    // ==========================================

    @GetMapping("/employees/mark")
    public String showEmployeeAttendanceForm(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        EmployeeAttendanceFormDto form = attendanceService.getEmployeeAttendanceForm(date, departmentId);

        model.addAttribute("form", form);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("attendanceDate", date);

        return "attendance/mark-employee";
    }

    @PostMapping("/employees/mark")
    public String saveEmployeeAttendance(@ModelAttribute("form") EmployeeAttendanceFormDto form,
                                         RedirectAttributes redirectAttributes) {
        try {
            attendanceService.saveEmployeeAttendance(form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employee attendance for " + form.getAttendanceDate() + " marked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving attendance: " + e.getMessage());
        }

        String redirectUrl = "redirect:/attendance/employees/mark?date=" + form.getAttendanceDate();
        if (form.getDepartmentId() != null) {
            redirectUrl += "&departmentId=" + form.getDepartmentId();
        }
        return redirectUrl;
    }

    @GetMapping("/employees/logs")
    public String showEmployeeLogs(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<EmployeeAttendance> logs = attendanceService.getEmployeeAttendanceByDate(date);
        model.addAttribute("logs", logs);
        model.addAttribute("selectedDate", date);
        return "attendance/employee-logs";
    }

    @PostMapping("/employees/quick-checkin")
    public String quickCheckIn(@RequestParam("employeeCode") String employeeCode,
                               @RequestParam("actionType") String actionType,
                               RedirectAttributes redirectAttributes) {
        boolean success = attendanceService.quickCheckInOut(employeeCode, actionType);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Quick " + (actionType.equalsIgnoreCase("CHECK_IN") ? "Check-In" : "Check-Out") +
                            " recorded for " + employeeCode + "!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee Code '" + employeeCode + "' not found!");
        }
        return "redirect:/dashboard";
    }
}
