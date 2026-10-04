package com.attendance.controller;

import com.attendance.model.LeaveRequest;
import com.attendance.model.LeaveStatus;
import com.attendance.model.LeaveType;
import com.attendance.service.EmployeeService;
import com.attendance.service.LeaveService;
import com.attendance.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;
    private final StudentService studentService;
    private final EmployeeService employeeService;

    public LeaveController(LeaveService leaveService,
                           StudentService studentService,
                           EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.studentService = studentService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listLeaves(
            @RequestParam(value = "applicantType", required = false) String applicantType,
            @RequestParam(value = "status", required = false) LeaveStatus status,
            Model model) {

        List<LeaveRequest> leaves = leaveService.filterLeaves(applicantType, status);
        model.addAttribute("leaves", leaves);
        model.addAttribute("selectedApplicantType", applicantType);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", LeaveStatus.values());
        model.addAttribute("pendingCount", leaveService.getPendingLeavesCount());

        return "leaves/list";
    }

    @GetMapping("/apply")
    public String showApplyForm(
            @RequestParam(value = "type", defaultValue = "STUDENT") String type,
            Model model) {

        model.addAttribute("type", type);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("leaveTypes", LeaveType.values());
        model.addAttribute("today", LocalDate.now());

        return "leaves/apply";
    }

    @PostMapping("/apply/student")
    public String submitStudentLeave(
            @RequestParam("studentId") Long studentId,
            @RequestParam("leaveType") LeaveType leaveType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("reason") String reason,
            RedirectAttributes redirectAttributes) {

        try {
            leaveService.applyStudentLeave(studentId, leaveType, startDate, endDate, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Student leave application submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting leave: " + e.getMessage());
            return "redirect:/leaves/apply?type=STUDENT";
        }
        return "redirect:/leaves";
    }

    @PostMapping("/apply/employee")
    public String submitEmployeeLeave(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("leaveType") LeaveType leaveType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("reason") String reason,
            RedirectAttributes redirectAttributes) {

        try {
            leaveService.applyEmployeeLeave(employeeId, leaveType, startDate, endDate, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Employee leave application submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting leave: " + e.getMessage());
            return "redirect:/leaves/apply?type=EMPLOYEE";
        }
        return "redirect:/leaves";
    }

    @PostMapping("/approve")
    public String approveLeave(
            @RequestParam("id") Long id,
            @RequestParam(value = "adminRemarks", required = false) String adminRemarks,
            RedirectAttributes redirectAttributes) {

        try {
            leaveService.approveLeave(id, adminRemarks);
            redirectAttributes.addFlashAttribute("successMessage", "Leave request approved! Attendance records updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving leave: " + e.getMessage());
        }
        return "redirect:/leaves";
    }

    @PostMapping("/reject")
    public String rejectLeave(
            @RequestParam("id") Long id,
            @RequestParam(value = "adminRemarks", required = false) String adminRemarks,
            RedirectAttributes redirectAttributes) {

        try {
            leaveService.rejectLeave(id, adminRemarks);
            redirectAttributes.addFlashAttribute("successMessage", "Leave request rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting leave: " + e.getMessage());
        }
        return "redirect:/leaves";
    }

    @GetMapping("/delete/{id}")
    public String deleteLeave(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            leaveService.deleteLeave(id);
            redirectAttributes.addFlashAttribute("successMessage", "Leave application deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting leave: " + e.getMessage());
        }
        return "redirect:/leaves";
    }
}
