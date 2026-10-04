package com.attendance.repository;

import com.attendance.model.AttendanceStatus;
import com.attendance.model.EmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {

    Optional<EmployeeAttendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    List<EmployeeAttendance> findByAttendanceDate(LocalDate attendanceDate);

    @Query("SELECT ea FROM EmployeeAttendance ea WHERE ea.attendanceDate = :date AND ea.employee.department.id = :departmentId")
    List<EmployeeAttendance> findByAttendanceDateAndDepartmentId(@Param("date") LocalDate date, @Param("departmentId") Long departmentId);

    List<EmployeeAttendance> findByEmployeeIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<EmployeeAttendance> findByEmployeeIdOrderByAttendanceDateDesc(Long employeeId);

    List<EmployeeAttendance> findByAttendanceDateBetweenOrderByAttendanceDateDesc(LocalDate startDate, LocalDate endDate);

    long countByAttendanceDateAndStatus(LocalDate date, AttendanceStatus status);

    long countByAttendanceDate(LocalDate date);

    long countByEmployeeIdAndStatus(Long employeeId, AttendanceStatus status);

    long countByEmployeeId(Long employeeId);

    @Query("SELECT ea FROM EmployeeAttendance ea WHERE " +
           "(:departmentId IS NULL OR ea.employee.department.id = :departmentId) AND " +
           "(:status IS NULL OR ea.status = :status) AND " +
           "(:startDate IS NULL OR ea.attendanceDate >= :startDate) AND " +
           "(:endDate IS NULL OR ea.attendanceDate <= :endDate) " +
           "ORDER BY ea.attendanceDate DESC, ea.employee.employeeCode ASC")
    List<EmployeeAttendance> filterAttendance(@Param("departmentId") Long departmentId,
                                              @Param("status") AttendanceStatus status,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}
