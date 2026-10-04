package com.attendance;

import com.attendance.model.*;
import com.attendance.repository.*;
import com.attendance.service.LeaveService;
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
public class LeaveServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private StudentAttendanceRepository studentAttendanceRepository;

    @Mock
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @InjectMocks
    private LeaveService leaveService;

    private Student student;
    private Employee employee;

    @BeforeEach
    void setUp() {
        Department dept = new Department("CSE", "CSE", "CS");
        dept.setId(1L);

        student = new Student("CSE-2024-001", "Aarav", "Sharma", "aarav@univ.edu", "+91 9876543210", "MALE", "Semester 4", "Section A", dept);
        student.setId(10L);

        employee = new Employee("EMP-1001", "Rajesh", "Kumar", "rajesh@univ.edu", "+91 9820011223", "MALE", "Professor", dept, "FULL_TIME");
        employee.setId(20L);
    }

    @Test
    void testApplyStudentLeave() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);

        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> i.getArgument(0));

        LeaveRequest leave = leaveService.applyStudentLeave(10L, LeaveType.MEDICAL, start, end, "Fever");

        assertNotNull(leave);
        assertEquals("STUDENT", leave.getApplicantType());
        assertEquals(LeaveStatus.PENDING, leave.getStatus());
        assertEquals("Fever", leave.getReason());
        verify(leaveRequestRepository, times(1)).save(any(LeaveRequest.class));
    }

    @Test
    void testApproveStudentLeave_AutoRecordsAttendance() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(2);

        LeaveRequest leave = new LeaveRequest("STUDENT", student, null, LeaveType.MEDICAL, start, end, "Medical");
        leave.setId(100L);

        when(leaveRequestRepository.findById(100L)).thenReturn(Optional.of(leave));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> i.getArgument(0));
        when(studentAttendanceRepository.findByStudentIdAndAttendanceDate(eq(10L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        LeaveRequest approved = leaveService.approveLeave(100L, "Doctor certificate verified");

        assertEquals(LeaveStatus.APPROVED, approved.getStatus());
        assertEquals("Doctor certificate verified", approved.getAdminRemarks());
        // Verify 2 attendance records saved (for 2 days: start, end)
        verify(studentAttendanceRepository, times(2)).save(any(StudentAttendance.class));
    }

    @Test
    void testRejectLeave() {
        LeaveRequest leave = new LeaveRequest("STUDENT", student, null, LeaveType.PERSONAL,
                LocalDate.now(), LocalDate.now(), "Personal");
        leave.setId(101L);

        when(leaveRequestRepository.findById(101L)).thenReturn(Optional.of(leave));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> i.getArgument(0));

        LeaveRequest rejected = leaveService.rejectLeave(101L, "Insufficient reason provided");

        assertEquals(LeaveStatus.REJECTED, rejected.getStatus());
        assertEquals("Insufficient reason provided", rejected.getAdminRemarks());
        verify(studentAttendanceRepository, never()).save(any(StudentAttendance.class));
    }
}
