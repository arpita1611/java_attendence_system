package com.attendance.controller;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.model.Employee;
import com.attendance.model.EmployeeAttendance;
import com.attendance.service.AttendanceService;
import com.attendance.service.DepartmentService;
import com.attendance.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final AttendanceService attendanceService;

    public EmployeeController(EmployeeService employeeService,
                              DepartmentService departmentService,
                              AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(value = "departmentId", required = false) Long departmentId,
                                @RequestParam(value = "designation", required = false) String designation,
                                @RequestParam(value = "search", required = false) String search,
                                Model model) {
        List<Employee> employees = employeeService.filterEmployees(departmentId, designation, search);
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedDesignation", designation);
        model.addAttribute("search", search);
        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Add New Employee");
        return "employees/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found!");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employeeOpt.get());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Edit Employee Details");
        return "employees/form";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", (employee.getId() == null ? "Add New Employee" : "Edit Employee Details"));
            return "employees/form";
        }

        try {
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving employee: " + e.getMessage());
        }

        return "redirect:/employees";
    }

    @GetMapping("/view/{id}")
    public String viewEmployeeProfile(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found!");
            return "redirect:/employees";
        }

        Employee employee = employeeOpt.get();
        AttendanceSummaryDto summary = employeeService.getEmployeeAttendanceSummary(id);
        List<EmployeeAttendance> history = attendanceService.getEmployeeAttendanceHistory(id);

        model.addAttribute("employee", employee);
        model.addAttribute("summary", summary);
        model.addAttribute("history", history);

        return "employees/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}
