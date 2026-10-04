package com.attendance.service;

import com.attendance.dto.*;
import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public AttendanceService(StudentRepository studentRepository,
                             EmployeeRepository employeeRepository,
                             StudentAttendanceRepository studentAttendanceRepository,
                             EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    // ==========================================
    // Student Attendance Logic
    // ==========================================

    public StudentAttendanceFormDto getStudentAttendanceForm(LocalDate date, Long departmentId, String semester) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<Student> students;
        if (departmentId != null && semester != null && !semester.trim().isEmpty()) {
            students = studentRepository.findByDepartmentIdAndSemesterAndActiveTrueOrderByRollNumberAsc(departmentId, semester);
        } else if (departmentId != null) {
            students = studentRepository.findByDepartmentIdAndActiveTrueOrderByRollNumberAsc(departmentId);
        } else {
            students = studentRepository.findByActiveTrueOrderByRollNumberAsc();
        }

        StudentAttendanceFormDto formDto = new StudentAttendanceFormDto();
        formDto.setAttendanceDate(date);
        formDto.setDepartmentId(departmentId);
        formDto.setSemester(semester);

        List<StudentAttendanceItemDto> items = new ArrayList<>();
        for (Student student : students) {
            Optional<StudentAttendance> existingOpt = studentAttendanceRepository.findByStudentIdAndAttendanceDate(student.getId(), date);
            
            AttendanceStatus status = AttendanceStatus.PRESENT;
            String remarks = "";

            if (existingOpt.isPresent()) {
                StudentAttendance existing = existingOpt.get();
                status = existing.getStatus();
                remarks = existing.getRemarks() != null ? existing.getRemarks() : "";
            }

            StudentAttendanceItemDto item = new StudentAttendanceItemDto(
                    student.getId(),
                    student.getRollNumber(),
                    student.getFullName(),
                    student.getSemester(),
                    student.getSection(),
                    status,
                    remarks
            );
            items.add(item);
        }

        formDto.setItems(items);
        return formDto;
    }

    public void saveStudentAttendance(StudentAttendanceFormDto formDto) {
        LocalDate date = formDto.getAttendanceDate() != null ? formDto.getAttendanceDate() : LocalDate.now();

        if (formDto.getItems() == null || formDto.getItems().isEmpty()) {
            return;
        }

        for (StudentAttendanceItemDto item : formDto.getItems()) {
            if (item.getStudentId() == null) continue;

            Optional<Student> studentOpt = studentRepository.findById(item.getStudentId());
            if (studentOpt.isEmpty()) continue;

            Student student = studentOpt.get();
            Optional<StudentAttendance> existingOpt = studentAttendanceRepository.findByStudentIdAndAttendanceDate(student.getId(), date);

            StudentAttendance record;
            if (existingOpt.isPresent()) {
                record = existingOpt.get();
            } else {
                record = new StudentAttendance();
                record.setStudent(student);
                record.setAttendanceDate(date);
            }

            record.setStatus(item.getStatus() != null ? item.getStatus() : AttendanceStatus.PRESENT);
            record.setRemarks(item.getRemarks());
            record.setRecordedAt(LocalDateTime.now());

            studentAttendanceRepository.save(record);
        }
    }

    public List<StudentAttendance> getStudentAttendanceByDate(LocalDate date) {
        if (date == null) date = LocalDate.now();
        return studentAttendanceRepository.findByAttendanceDate(date);
    }

    public List<StudentAttendance> getStudentAttendanceHistory(Long studentId) {
        return studentAttendanceRepository.findByStudentIdOrderByAttendanceDateDesc(studentId);
    }

    // ==========================================
    // Employee Attendance Logic
    // ==========================================

    public EmployeeAttendanceFormDto getEmployeeAttendanceForm(LocalDate date, Long departmentId) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<Employee> employees;
        if (departmentId != null) {
            employees = employeeRepository.findByDepartmentIdAndActiveTrueOrderByEmployeeCodeAsc(departmentId);
        } else {
            employees = employeeRepository.findByActiveTrueOrderByEmployeeCodeAsc();
        }

        EmployeeAttendanceFormDto formDto = new EmployeeAttendanceFormDto();
        formDto.setAttendanceDate(date);
        formDto.setDepartmentId(departmentId);

        List<EmployeeAttendanceItemDto> items = new ArrayList<>();
        for (Employee employee : employees) {
            Optional<EmployeeAttendance> existingOpt = employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), date);

            AttendanceStatus status = AttendanceStatus.PRESENT;
            LocalTime checkIn = LocalTime.of(9, 0);
            LocalTime checkOut = LocalTime.of(17, 30);
            String remarks = "";

            if (existingOpt.isPresent()) {
                EmployeeAttendance existing = existingOpt.get();
                status = existing.getStatus();
                checkIn = existing.getCheckInTime();
                checkOut = existing.getCheckOutTime();
                remarks = existing.getRemarks() != null ? existing.getRemarks() : "";
            }

            EmployeeAttendanceItemDto item = new EmployeeAttendanceItemDto(
                    employee.getId(),
                    employee.getEmployeeCode(),
                    employee.getFullName(),
                    employee.getDesignation(),
                    status,
                    checkIn,
                    checkOut,
                    remarks
            );
            items.add(item);
        }

        formDto.setItems(items);
        return formDto;
    }

    public void saveEmployeeAttendance(EmployeeAttendanceFormDto formDto) {
        LocalDate date = formDto.getAttendanceDate() != null ? formDto.getAttendanceDate() : LocalDate.now();

        if (formDto.getItems() == null || formDto.getItems().isEmpty()) {
            return;
        }

        for (EmployeeAttendanceItemDto item : formDto.getItems()) {
            if (item.getEmployeeId() == null) continue;

            Optional<Employee> employeeOpt = employeeRepository.findById(item.getEmployeeId());
            if (employeeOpt.isEmpty()) continue;

            Employee employee = employeeOpt.get();
            Optional<EmployeeAttendance> existingOpt = employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), date);

            EmployeeAttendance record;
            if (existingOpt.isPresent()) {
                record = existingOpt.get();
            } else {
                record = new EmployeeAttendance();
                record.setEmployee(employee);
                record.setAttendanceDate(date);
            }

            record.setStatus(item.getStatus() != null ? item.getStatus() : AttendanceStatus.PRESENT);
            record.setCheckInTime(item.getCheckInTime());
            record.setCheckOutTime(item.getCheckOutTime());
            record.setRemarks(item.getRemarks());
            record.setRecordedAt(LocalDateTime.now());

            employeeAttendanceRepository.save(record);
        }
    }

    public List<EmployeeAttendance> getEmployeeAttendanceByDate(LocalDate date) {
        if (date == null) date = LocalDate.now();
        return employeeAttendanceRepository.findByAttendanceDate(date);
    }

    public List<EmployeeAttendance> getEmployeeAttendanceHistory(Long employeeId) {
        return employeeAttendanceRepository.findByEmployeeIdOrderByAttendanceDateDesc(employeeId);
    }

    public boolean quickCheckInOut(String employeeCode, String actionType) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeCode(employeeCode);
        if (employeeOpt.isEmpty()) {
            return false;
        }

        Employee employee = employeeOpt.get();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        Optional<EmployeeAttendance> existingOpt = employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);

        EmployeeAttendance record;
        if (existingOpt.isPresent()) {
            record = existingOpt.get();
        } else {
            record = new EmployeeAttendance();
            record.setEmployee(employee);
            record.setAttendanceDate(today);
            record.setStatus(AttendanceStatus.PRESENT);
        }

        if ("CHECK_IN".equalsIgnoreCase(actionType)) {
            record.setCheckInTime(now);
            record.setStatus(AttendanceStatus.PRESENT);
        } else if ("CHECK_OUT".equalsIgnoreCase(actionType)) {
            record.setCheckOutTime(now);
        }

        record.setRecordedAt(LocalDateTime.now());
        employeeAttendanceRepository.save(record);
        return true;
    }
}
