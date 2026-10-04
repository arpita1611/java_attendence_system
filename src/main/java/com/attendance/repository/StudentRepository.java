package com.attendance.repository;

import com.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByRollNumber(String rollNumber);

    Optional<Student> findByRollNumberIgnoreCase(String rollNumber);
    
    List<Student> findByActiveTrueOrderByRollNumberAsc();

    List<Student> findAllByOrderByRollNumberAsc();
    
    List<Student> findByDepartmentIdAndActiveTrueOrderByRollNumberAsc(Long departmentId);
    
    List<Student> findByDepartmentIdAndSemesterAndActiveTrueOrderByRollNumberAsc(Long departmentId, String semester);
    
    long countByActiveTrue();

    @Query("SELECT DISTINCT s.semester FROM Student s WHERE s.department.id = :departmentId AND s.active = true ORDER BY s.semester ASC")
    List<String> findDistinctSemestersByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT s FROM Student s WHERE " +
           "(:departmentId IS NULL OR s.department.id = :departmentId) AND " +
           "(:semester IS NULL OR s.semester = :semester) AND " +
           "(:search IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "s.active = true ORDER BY s.rollNumber ASC")
    List<Student> filterStudents(@Param("departmentId") Long departmentId,
                                @Param("semester") String semester,
                                @Param("search") String search);
}
