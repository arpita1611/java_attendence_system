package com.attendance.controller;

import com.attendance.dto.DashboardStatsDto;
import com.attendance.dto.DefaulterDto;
import com.attendance.model.EmployeeAttendance;
import com.attendance.model.StudentAttendance;
import com.attendance.service.AttendanceService;
import com.attendance.service.DashboardService;
import com.attendance.service.DefaulterService;
import com.attendance.service.DepartmentService;
import com.attendance.service.LeaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final DepartmentService departmentService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final DefaulterService defaulterService;

    public DashboardController(DashboardService dashboardService,
                               DepartmentService departmentService,
                               AttendanceService attendanceService,
                               LeaveService leaveService,
                               DefaulterService defaulterService) {
        this.dashboardService = dashboardService;
        this.departmentService = departmentService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.defaulterService = defaulterService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        LocalDate today = LocalDate.now();

        List<StudentAttendance> todayStudentLogs = attendanceService.getStudentAttendanceByDate(today);
        List<EmployeeAttendance> todayEmployeeLogs = attendanceService.getEmployeeAttendanceByDate(today);
        long pendingLeaves = leaveService.getPendingLeavesCount();
        List<DefaulterDto> defaulters = defaulterService.getStudentDefaulters(null, null, 75.0);

        model.addAttribute("stats", stats);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("todayStudentLogs", todayStudentLogs);
        model.addAttribute("todayEmployeeLogs", todayEmployeeLogs);
        model.addAttribute("pendingLeavesCount", pendingLeaves);
        model.addAttribute("defaultersCount", defaulters.size());
        model.addAttribute("defaultersList", defaulters);
        model.addAttribute("today", today);

        return "dashboard";
    }
}
