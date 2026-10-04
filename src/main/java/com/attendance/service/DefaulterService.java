package com.attendance.service;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.dto.DefaulterDto;
import com.attendance.model.Student;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DefaulterService {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public DefaulterService(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    public List<DefaulterDto> getStudentDefaulters(Long departmentId, String semester, double thresholdPercent) {
        List<Student> students = studentService.filterStudents(departmentId, semester, null);
        List<DefaulterDto> defaulters = new ArrayList<>();

        for (Student student : students) {
            AttendanceSummaryDto summary = studentService.getStudentAttendanceSummary(student.getId());
            if (summary.getTotalRecords() > 0 && summary.getAttendancePercentage() < thresholdPercent) {
                DefaulterDto dto = new DefaulterDto(
                        student.getId(),
                        student.getRollNumber(),
                        student.getFullName(),
                        student.getDepartment() != null ? student.getDepartment().getName() : "N/A",
                        student.getSemester(),
                        student.getSection(),
                        student.getEmail(),
                        student.getPhone(),
                        summary.getTotalRecords(),
                        summary.getPresentCount() + summary.getExcusedCount(),
                        summary.getAbsentCount(),
                        summary.getLateCount(),
                        summary.getAttendancePercentage()
                );
                defaulters.add(dto);
            }
        }

        // Sort by percentage ascending (most severe shortage first)
        defaulters.sort(Comparator.comparingDouble(DefaulterDto::getAttendancePercentage));
        return defaulters;
    }

    public ByteArrayInputStream generateDefaultersCsv(List<DefaulterDto> defaulters, double threshold) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            writer.println("Roll Number,Student Name,Department,Semester,Section,Attendance %,Total Classes,Attended Classes,Absent,Consecutive Classes Needed to Reach 75%,Email,Phone");

            for (DefaulterDto d : defaulters) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.1f%%,%d,%d,%d,%d,\"%s\",\"%s\"%n",
                        d.getRollNumber(),
                        d.getStudentName(),
                        d.getDepartmentName(),
                        d.getSemester(),
                        d.getSection() != null ? d.getSection() : "",
                        d.getAttendancePercentage(),
                        d.getTotalSessions(),
                        d.getAttendedSessions(),
                        d.getAbsentSessions(),
                        d.getClassesNeededFor75(),
                        d.getEmail() != null ? d.getEmail() : "",
                        d.getPhone() != null ? d.getPhone() : ""
                );
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
