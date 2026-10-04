package com.attendance.controller;

import com.attendance.dto.CsvImportResultDto;
import com.attendance.service.CsvImportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final CsvImportService csvImportService;

    public ImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @GetMapping("/students")
    public String showStudentImportPage() {
        return "import/students";
    }

    @PostMapping("/students")
    public String uploadStudentsCsv(@RequestParam("file") MultipartFile file,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a valid CSV file to upload.");
            return "redirect:/import/students";
        }

        CsvImportResultDto result = csvImportService.importStudents(file);
        model.addAttribute("result", result);
        return "import/students";
    }

    @GetMapping("/employees")
    public String showEmployeeImportPage() {
        return "import/employees";
    }

    @PostMapping("/employees")
    public String uploadEmployeesCsv(@RequestParam("file") MultipartFile file,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a valid CSV file to upload.");
            return "redirect:/import/employees";
        }

        CsvImportResultDto result = csvImportService.importEmployees(file);
        model.addAttribute("result", result);
        return "import/employees";
    }

    @GetMapping("/template/students.csv")
    public ResponseEntity<InputStreamResource> downloadStudentTemplate() {
        ByteArrayInputStream in = csvImportService.getStudentTemplateCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students_import_template.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/template/employees.csv")
    public ResponseEntity<InputStreamResource> downloadEmployeeTemplate() {
        ByteArrayInputStream in = csvImportService.getEmployeeTemplateCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"employees_import_template.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(in));
    }
}
