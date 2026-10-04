package com.attendance.service;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository,
                        StudentRepository studentRepository,
                        EmployeeRepository employeeRepository,
                        StudentAttendanceRepository studentAttendanceRepository,
                        EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestRepository.findAllByOrderByAppliedAtDesc();
    }

    public List<LeaveRequest> filterLeaves(String applicantType, LeaveStatus status) {
        if (applicantType != null && !applicantType.isEmpty() && status != null) {
            return leaveRequestRepository.findByApplicantTypeAndStatusOrderByAppliedAtDesc(applicantType, status);
        } else if (applicantType != null && !applicantType.isEmpty()) {
            return leaveRequestRepository.findByApplicantTypeOrderByAppliedAtDesc(applicantType);
        } else if (status != null) {
            return leaveRequestRepository.findByStatusOrderByAppliedAtDesc(status);
        }
        return leaveRequestRepository.findAllByOrderByAppliedAtDesc();
    }

    public Optional<LeaveRequest> getLeaveById(Long id) {
        return leaveRequestRepository.findById(id);
    }

    public long getPendingLeavesCount() {
        return leaveRequestRepository.countByStatus(LeaveStatus.PENDING);
    }

    @Transactional
    public LeaveRequest applyStudentLeave(Long studentId, LeaveType leaveType, LocalDate startDate,
                                          LocalDate endDate, String reason) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        LeaveRequest request = new LeaveRequest("STUDENT", student, null, leaveType, startDate, endDate, reason);
        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest applyEmployeeLeave(Long employeeId, LeaveType leaveType, LocalDate startDate,
                                           LocalDate endDate, String reason) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        LeaveRequest request = new LeaveRequest("EMPLOYEE", null, employee, leaveType, startDate, endDate, reason);
        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest approveLeave(Long leaveId, String adminRemarks) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found with ID: " + leaveId));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setAdminRemarks(adminRemarks);
        leave.setReviewedAt(LocalDateTime.now());

        // Auto-record attendance for the approved leave duration
        LocalDate current = leave.getStartDate();
        while (!current.isAfter(leave.getEndDate())) {
            final LocalDate dateToRecord = current;
            if ("STUDENT".equalsIgnoreCase(leave.getApplicantType()) && leave.getStudent() != null) {
                Student student = leave.getStudent();
                Optional<StudentAttendance> existing = studentAttendanceRepository
                        .findByStudentIdAndAttendanceDate(student.getId(), dateToRecord);

                StudentAttendance sa = existing.orElseGet(() -> new StudentAttendance(student, dateToRecord, AttendanceStatus.EXCUSED, ""));
                sa.setStatus(AttendanceStatus.EXCUSED);
                sa.setRemarks("Approved Leave: " + leave.getLeaveType().getDisplayName() + 
                              (adminRemarks != null && !adminRemarks.isEmpty() ? " (" + adminRemarks + ")" : ""));
                studentAttendanceRepository.save(sa);

            } else if ("EMPLOYEE".equalsIgnoreCase(leave.getApplicantType()) && leave.getEmployee() != null) {
                Employee employee = leave.getEmployee();
                Optional<EmployeeAttendance> existing = employeeAttendanceRepository
                        .findByEmployeeIdAndAttendanceDate(employee.getId(), dateToRecord);

                EmployeeAttendance ea = existing.orElseGet(() -> new EmployeeAttendance(employee, dateToRecord, AttendanceStatus.ON_LEAVE, null, null, ""));
                ea.setStatus(AttendanceStatus.ON_LEAVE);
                ea.setCheckInTime(null);
                ea.setCheckOutTime(null);
                ea.setRemarks("Approved Leave: " + leave.getLeaveType().getDisplayName() + 
                              (adminRemarks != null && !adminRemarks.isEmpty() ? " (" + adminRemarks + ")" : ""));
                employeeAttendanceRepository.save(ea);
            }
            current = current.plusDays(1);
        }

        return leaveRequestRepository.save(leave);
    }

    @Transactional
    public LeaveRequest rejectLeave(Long leaveId, String adminRemarks) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found with ID: " + leaveId));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setAdminRemarks(adminRemarks);
        leave.setReviewedAt(LocalDateTime.now());
        return leaveRequestRepository.save(leave);
    }

    @Transactional
    public void deleteLeave(Long id) {
        leaveRequestRepository.deleteById(id);
    }
}
