package com.attendance;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.dto.KioskScanRequestDto;
import com.attendance.dto.KioskScanResponseDto;
import com.attendance.model.Department;
import com.attendance.model.Employee;
import com.attendance.model.Student;
import com.attendance.repository.EmployeeAttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.StudentAttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.service.EmployeeService;
import com.attendance.service.KioskService;
import com.attendance.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KioskServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private StudentAttendanceRepository studentAttendanceRepository;

    @Mock
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private KioskService kioskService;

    private Student student;
    private Employee employee;

    @BeforeEach
    void setUp() {
        Department dept = new Department("CSE", "CSE", "CS Dept");
        student = new Student("CSE-2024-001", "Aarav", "Sharma", "aarav@univ.edu", "123", "MALE", "Semester 4", "A", dept);
        student.setId(10L);

        employee = new Employee("EMP-1001", "Rajesh", "Kumar", "rajesh@univ.edu", "456", "MALE", "Professor", dept, "FULL_TIME");
        employee.setId(20L);
    }

    @Test
    void testProcessScan_StudentCheckIn_Success() {
        KioskScanRequestDto req = new KioskScanRequestDto("CSE-2024-001", "STUDENT_CHECKIN", null);

        when(studentRepository.findByRollNumberIgnoreCase("CSE-2024-001")).thenReturn(Optional.of(student));
        when(studentAttendanceRepository.findByStudentIdAndAttendanceDate(eq(10L), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(studentService.getStudentAttendanceSummary(10L))
                .thenReturn(new AttendanceSummaryDto(10, 9, 1, 0, 0, 0, 0));

        KioskScanResponseDto response = kioskService.processScan(req);

        assertTrue(response.isSuccess());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("STUDENT", response.getEntityType());
        assertEquals("CSE-2024-001", response.getIdentifier());
        assertEquals("Aarav Sharma", response.getName());
        assertEquals(90.0, response.getCurrentPercentage());
        verify(studentAttendanceRepository, times(1)).save(any());
    }

    @Test
    void testProcessScan_EmployeeCheckIn_Success() {
        KioskScanRequestDto req = new KioskScanRequestDto("EMP-1001", "EMPLOYEE_CHECKIN", null);

        when(employeeRepository.findByEmployeeCodeIgnoreCase("EMP-1001")).thenReturn(Optional.of(employee));
        when(employeeAttendanceRepository.findByEmployeeIdAndAttendanceDate(eq(20L), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(employeeService.getEmployeeAttendanceSummary(20L))
                .thenReturn(new AttendanceSummaryDto(10, 10, 0, 0, 0, 0, 0));

        KioskScanResponseDto response = kioskService.processScan(req);

        assertTrue(response.isSuccess());
        assertEquals("EMPLOYEE", response.getEntityType());
        assertEquals("Rajesh Kumar", response.getName());
        verify(employeeAttendanceRepository, times(1)).save(any());
    }

    @Test
    void testProcessScan_NotFound() {
        KioskScanRequestDto req = new KioskScanRequestDto("UNKNOWN-CODE", "STUDENT_CHECKIN", null);

        when(studentRepository.findByRollNumberIgnoreCase("UNKNOWN-CODE")).thenReturn(Optional.empty());

        KioskScanResponseDto response = kioskService.processScan(req);

        assertFalse(response.isSuccess());
        assertEquals("NOT_FOUND", response.getStatus());
        verify(studentAttendanceRepository, never()).save(any());
    }
}
