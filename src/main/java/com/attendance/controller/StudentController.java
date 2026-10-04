package com.attendance.controller;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.model.Student;
import com.attendance.model.StudentAttendance;
import com.attendance.service.AttendanceService;
import com.attendance.service.DepartmentService;
import com.attendance.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final AttendanceService attendanceService;

    public StudentController(StudentService studentService,
                             DepartmentService departmentService,
                             AttendanceService attendanceService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String listStudents(@RequestParam(value = "departmentId", required = false) Long departmentId,
                               @RequestParam(value = "semester", required = false) String semester,
                               @RequestParam(value = "search", required = false) String search,
                               Model model) {
        List<Student> students = studentService.filterStudents(departmentId, semester, search);
        model.addAttribute("students", students);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("search", search);
        return "students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Add New Student");
        return "students/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
            return "redirect:/students";
        }
        model.addAttribute("student", studentOpt.get());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Edit Student Details");
        return "students/form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", (student.getId() == null ? "Add New Student" : "Edit Student Details"));
            return "students/form";
        }

        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving student: " + e.getMessage());
        }

        return "redirect:/students";
    }

    @GetMapping("/view/{id}")
    public String viewStudentProfile(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
            return "redirect:/students";
        }

        Student student = studentOpt.get();
        AttendanceSummaryDto summary = studentService.getStudentAttendanceSummary(id);
        List<StudentAttendance> history = attendanceService.getStudentAttendanceHistory(id);

        model.addAttribute("student", student);
        model.addAttribute("summary", summary);
        model.addAttribute("history", history);

        return "students/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/students";
    }
}
