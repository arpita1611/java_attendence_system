package com.attendance.controller;

import com.attendance.dto.DefaulterDto;
import com.attendance.service.DefaulterService;
import com.attendance.service.DepartmentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping("/reports/defaulters")
public class DefaulterController {

    private final DefaulterService defaulterService;
    private final DepartmentService departmentService;

    public DefaulterController(DefaulterService defaulterService, DepartmentService departmentService) {
        this.defaulterService = defaulterService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String showDefaulters(
            @RequestParam(value = "threshold", defaultValue = "75.0") Double threshold,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester,
            Model model) {

        if (threshold == null || threshold <= 0) {
            threshold = 75.0;
        }

        List<DefaulterDto> defaulters = defaulterService.getStudentDefaulters(departmentId, semester, threshold);

        model.addAttribute("defaulters", defaulters);
        model.addAttribute("threshold", threshold);
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "reports/defaulters";
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportDefaultersCsv(
            @RequestParam(value = "threshold", defaultValue = "75.0") Double threshold,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "semester", required = false) String semester) {

        if (threshold == null || threshold <= 0) {
            threshold = 75.0;
        }

        List<DefaulterDto> defaulters = defaulterService.getStudentDefaulters(departmentId, semester, threshold);
        ByteArrayInputStream in = defaulterService.generateDefaultersCsv(defaulters, threshold);

        String filename = "defaulters_below_" + threshold.intValue() + "_percent.csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(in));
    }
}
