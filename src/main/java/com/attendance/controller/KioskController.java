package com.attendance.controller;

import com.attendance.dto.KioskScanRequestDto;
import com.attendance.dto.KioskScanResponseDto;
import com.attendance.service.DashboardService;
import com.attendance.service.KioskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class KioskController {

    private final KioskService kioskService;
    private final DashboardService dashboardService;

    public KioskController(KioskService kioskService, DashboardService dashboardService) {
        this.kioskService = kioskService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/attendance/kiosk")
    public String showKiosk(Model model) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("stats", dashboardService.getDashboardStats());
        return "attendance/kiosk";
    }

    @PostMapping("/api/kiosk/scan")
    @ResponseBody
    public KioskScanResponseDto handleScan(@RequestBody KioskScanRequestDto request) {
        return kioskService.processScan(request);
    }
}
