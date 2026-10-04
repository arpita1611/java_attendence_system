package com.attendance;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.dto.DefaulterDto;
import com.attendance.model.Department;
import com.attendance.model.Student;
import com.attendance.repository.StudentRepository;
import com.attendance.service.DefaulterService;
import com.attendance.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaulterServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private DefaulterService defaulterService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        Department dept = new Department("CSE", "CSE", "CS Dept");
        student1 = new Student("CSE-2024-001", "Aarav", "Sharma", "a@u.edu", "123", "MALE", "Semester 4", "A", dept);
        student1.setId(1L);

        student2 = new Student("CSE-2024-002", "Kabir", "Mehta", "k@u.edu", "456", "MALE", "Semester 4", "A", dept);
        student2.setId(2L);
    }

    @Test
    void testGetStudentDefaulters_FiltersBelowThreshold() {
        when(studentService.filterStudents(null, null, null)).thenReturn(List.of(student1, student2));

        // Student 1: 10 total, 9 present (90% - above 75%)
        AttendanceSummaryDto summary1 = new AttendanceSummaryDto(10, 9, 1, 0, 0, 0, 0);
        when(studentService.getStudentAttendanceSummary(1L)).thenReturn(summary1);

        // Student 2: 10 total, 5 present (50% - below 75%)
        AttendanceSummaryDto summary2 = new AttendanceSummaryDto(10, 5, 5, 0, 0, 0, 0);
        when(studentService.getStudentAttendanceSummary(2L)).thenReturn(summary2);

        List<DefaulterDto> defaulters = defaulterService.getStudentDefaulters(null, null, 75.0);

        assertEquals(1, defaulters.size());
        DefaulterDto def = defaulters.get(0);
        assertEquals("CSE-2024-002", def.getRollNumber());
        assertEquals(50.0, def.getAttendancePercentage());
        // To reach 75% from 5/10: (5 + x)/(10 + x) = 0.75 => 5 + x = 7.5 + 0.75x => 0.25x = 2.5 => x = 10 classes needed
        assertEquals(10, def.getClassesNeededFor75());
    }
}
