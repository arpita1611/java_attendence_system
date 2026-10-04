package com.attendance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee code is required")
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Email(message = "Please provide a valid email")
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 10)
    private String gender;

    @NotBlank(message = "Designation is required")
    @Column(nullable = false, length = 100)
    private String designation; // e.g. "Senior Professor", "HR Manager", "Software Engineer"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "joining_date")
    private LocalDate joiningDate = LocalDate.now();

    @Column(name = "employment_type", length = 30)
    private String employmentType = "FULL_TIME"; // FULL_TIME, PART_TIME, CONTRACT

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeAttendance> attendanceRecords = new ArrayList<>();

    public Employee() {
    }

    public Employee(String employeeCode, String firstName, String lastName, String email, String phone,
                    String gender, String designation, Department department, String employmentType) {
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.designation = designation;
        this.department = department;
        this.employmentType = employmentType;
        this.joiningDate = LocalDate.now();
        this.active = true;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EmployeeAttendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<EmployeeAttendance> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }
}
