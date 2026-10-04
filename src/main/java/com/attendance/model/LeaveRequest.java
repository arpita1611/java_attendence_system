package com.attendance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "applicant_type", nullable = false)
    private String applicantType; // "STUDENT" or "EMPLOYEE"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType = LeaveType.CASUAL;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason", length = 1000)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "admin_remarks", length = 500)
    private String adminRemarks;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public LeaveRequest() {
    }

    public LeaveRequest(String applicantType, Student student, Employee employee, LeaveType leaveType,
                        LocalDate startDate, LocalDate endDate, String reason) {
        this.applicantType = applicantType;
        this.student = student;
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(String applicantType) {
        this.applicantType = applicantType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getApplicantName() {
        if ("STUDENT".equalsIgnoreCase(applicantType) && student != null) {
            return student.getFullName();
        } else if ("EMPLOYEE".equalsIgnoreCase(applicantType) && employee != null) {
            return employee.getFullName();
        }
        return "Unknown";
    }

    public String getApplicantIdentifier() {
        if ("STUDENT".equalsIgnoreCase(applicantType) && student != null) {
            return student.getRollNumber();
        } else if ("EMPLOYEE".equalsIgnoreCase(applicantType) && employee != null) {
            return employee.getEmployeeCode();
        }
        return "-";
    }

    public String getDepartmentName() {
        if ("STUDENT".equalsIgnoreCase(applicantType) && student != null && student.getDepartment() != null) {
            return student.getDepartment().getName();
        } else if ("EMPLOYEE".equalsIgnoreCase(applicantType) && employee != null && employee.getDepartment() != null) {
            return employee.getDepartment().getName();
        }
        return "N/A";
    }
}
