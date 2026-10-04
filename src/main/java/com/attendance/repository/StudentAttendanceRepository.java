package com.attendance.repository;

import com.attendance.model.AttendanceStatus;
import com.attendance.model.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long> {

    Optional<StudentAttendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    List<StudentAttendance> findByAttendanceDate(LocalDate attendanceDate);

    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.attendanceDate = :date AND sa.student.department.id = :departmentId")
    List<StudentAttendance> findByAttendanceDateAndDepartmentId(@Param("date") LocalDate date, @Param("departmentId") Long departmentId);

    List<StudentAttendance> findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(Long studentId, LocalDate startDate, LocalDate endDate);

    List<StudentAttendance> findByStudentIdOrderByAttendanceDateDesc(Long studentId);

    List<StudentAttendance> findByAttendanceDateBetweenOrderByAttendanceDateDesc(LocalDate startDate, LocalDate endDate);

    long countByAttendanceDateAndStatus(LocalDate date, AttendanceStatus status);

    long countByAttendanceDate(LocalDate date);

    long countByStudentIdAndStatus(Long studentId, AttendanceStatus status);

    long countByStudentId(Long studentId);

    @Query("SELECT sa FROM StudentAttendance sa WHERE " +
           "(:departmentId IS NULL OR sa.student.department.id = :departmentId) AND " +
           "(:semester IS NULL OR sa.student.semester = :semester) AND " +
           "(:status IS NULL OR sa.status = :status) AND " +
           "(:startDate IS NULL OR sa.attendanceDate >= :startDate) AND " +
           "(:endDate IS NULL OR sa.attendanceDate <= :endDate) " +
           "ORDER BY sa.attendanceDate DESC, sa.student.rollNumber ASC")
    List<StudentAttendance> filterAttendance(@Param("departmentId") Long departmentId,
                                            @Param("semester") String semester,
                                            @Param("status") AttendanceStatus status,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
}
