package com.attendance;

import com.attendance.dto.CsvImportResultDto;
import com.attendance.model.Department;
import com.attendance.model.Student;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.service.CsvImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvImportServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CsvImportService csvImportService;

    @Test
    void testImportStudents_Success() {
        String csvContent = "Roll Number,First Name,Last Name,Department Code,Semester,Section,Email,Phone,Gender\n" +
                "CSE-2024-901,Alice,Wonder,CSE,Semester 4,A,alice@univ.edu,+91 9999999999,FEMALE\n" +
                "CSE-2024-902,Bob,Builder,CSE,Semester 4,A,bob@univ.edu,+91 8888888888,MALE";

        MockMultipartFile file = new MockMultipartFile(
                "file", "students.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        Department cse = new Department("Computer Science", "CSE", "CS");
        when(departmentRepository.findByCodeIgnoreCase("CSE")).thenReturn(Optional.of(cse));
        when(studentRepository.findByRollNumberIgnoreCase(any())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        CsvImportResultDto result = csvImportService.importStudents(file);

        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void testImportStudents_DuplicateError() {
        String csvContent = "Roll Number,First Name,Last Name,Department Code,Semester,Section,Email,Phone,Gender\n" +
                "CSE-2024-901,Alice,Wonder,CSE,Semester 4,A,alice@univ.edu,+91 9999999999,FEMALE";

        MockMultipartFile file = new MockMultipartFile(
                "file", "students.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        Student existing = new Student();
        when(studentRepository.findByRollNumberIgnoreCase("CSE-2024-901")).thenReturn(Optional.of(existing));

        CsvImportResultDto result = csvImportService.importStudents(file);

        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertTrue(result.getErrors().get(0).contains("already exists"));
        verify(studentRepository, never()).save(any(Student.class));
    }
}
