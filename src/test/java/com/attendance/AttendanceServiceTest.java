package com.attendance;

import com.attendance.dto.EmployeeAttendanceFormDto;
import com.attendance.dto.EmployeeAttendanceItemDto;
import com.attendance.dto.StudentAttendanceFormDto;
import com.attendance.dto.StudentAttendanceItemDto;
import com.attendance.model.*;
import com.attendance.repository.*;
import com.attendance.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private StudentAttendanceRepository studentAttendanceRepository;

    @Mock
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private Department department;
    private Student student;
    private Employee employee;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science", "CSE", "CS Dept");
        department.setId(1L);

        student = new Student("CSE-2024-001", "Aarav", "Sharma", "aarav@univ.edu", "+91 9876543210", "MALE", "Semester 4", "Section A", department);
        student.setId(10L);

        employee = new Employee("EMP-1001", "Rajesh", "Kumar", "rajesh@univ.edu", "+91 9820011223", "MALE", "Professor", department, "FULL_TIME");
        employee.setId(20L);
    }

    @Test
    void testSaveStudentAttendance() {
        StudentAttendanceFormDto form = new StudentAttendanceFormDto();
        form.setAttendanceDate(LocalDate.now());

        List<StudentAttendanceItemDto> items = new ArrayList<>();
        StudentAttendanceItemDto item = new StudentAttendanceItemDto();
        item.setStudentId(10L);
        item.setStatus(AttendanceStatus.PRESENT);
        item.setRemarks("On time");
        items.add(item);
        form.setItems(items);

        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(studentAttendanceRepository.findByStudentIdAndAttendanceDate(eq(10L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        attendanceService.saveStudentAttendance(form);

        verify(studentAttendanceRepository, times(1)).save(any(StudentAttendance.class));
    }

    @Test
    void testSaveEmployeeAttendance() {
        EmployeeAttendanceFormDto form = new EmployeeAttendanceFormDto();
        form.setAttendanceDate(LocalDate.now());

        List<EmployeeAttendanceItemDto> items = new ArrayList<>();
        EmployeeAttendanceItemDto item = new EmployeeAttendanceItemDto();
        item.setEmployeeId(20L);
        item.setStatus(AttendanceStatus.PRESENT);
        item.setCheckInTime(LocalTime.of(9, 0));
        item.setCheckOutTime(LocalTime.of(17, 30));
        items.add(item);
        form.setItems(items);

        when(employeeRepository.findById(20L)).thenReturn(Optional.of(employee));
        when(employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(eq(20L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        attendanceService.saveEmployeeAttendance(form);

        verify(employeeAttendanceRepository, times(1)).save(any(EmployeeAttendance.class));
    }

    @Test
    void testQuickCheckIn_Success() {
        when(employeeRepository.findByEmployeeCode("EMP-1001")).thenReturn(Optional.of(employee));
        when(employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(eq(20L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        boolean result = attendanceService.quickCheckInOut("EMP-1001", "CHECK_IN");

        assertTrue(result);
        verify(employeeAttendanceRepository, times(1)).save(any(EmployeeAttendance.class));
    }

    @Test
    void testQuickCheckIn_NotFound() {
        when(employeeRepository.findByEmployeeCode("INVALID")).thenReturn(Optional.empty());

        boolean result = attendanceService.quickCheckInOut("INVALID", "CHECK_IN");

        assertFalse(result);
        verify(employeeAttendanceRepository, never()).save(any(EmployeeAttendance.class));
    }
}
