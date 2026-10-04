package com.attendance.service;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.Student;
import com.attendance.repository.StudentAttendanceRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;

    public StudentService(StudentRepository studentRepository,
                          StudentAttendanceRepository studentAttendanceRepository) {
        this.studentRepository = studentRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAllByOrderByRollNumberAsc();
    }

    public List<Student> getAllActiveStudents() {
        return studentRepository.findByActiveTrueOrderByRollNumberAsc();
    }

    public List<Student> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentIdAndActiveTrueOrderByRollNumberAsc(departmentId);
    }

    public List<Student> getStudentsByDepartmentAndSemester(Long departmentId, String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            return getStudentsByDepartment(departmentId);
        }
        return studentRepository.findByDepartmentIdAndSemesterAndActiveTrueOrderByRollNumberAsc(departmentId, semester);
    }

    public List<Student> filterStudents(Long departmentId, String semester, String search) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String cleanSemester = (semester != null && !semester.trim().isEmpty()) ? semester.trim() : null;
        return studentRepository.filterStudents(departmentId, cleanSemester, cleanSearch);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public long countActiveStudents() {
        return studentRepository.countByActiveTrue();
    }

    public List<String> getSemestersByDepartment(Long departmentId) {
        return studentRepository.findDistinctSemestersByDepartmentId(departmentId);
    }

    public AttendanceSummaryDto getStudentAttendanceSummary(Long studentId) {
        long total = studentAttendanceRepository.countByStudentId(studentId);
        long present = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.PRESENT);
        long absent = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.ABSENT);
        long late = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.LATE);
        long excused = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.EXCUSED);
        long halfDay = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.HALF_DAY);
        long onLeave = studentAttendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.ON_LEAVE);

        return new AttendanceSummaryDto(total, present, absent, late, excused, halfDay, onLeave);
    }
}
