package com.attendance.repository;

import com.attendance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmployeeCodeIgnoreCase(String employeeCode);

    List<Employee> findByActiveTrueOrderByEmployeeCodeAsc();

    List<Employee> findAllByOrderByEmployeeCodeAsc();

    List<Employee> findByDepartmentIdAndActiveTrueOrderByEmployeeCodeAsc(Long departmentId);

    long countByActiveTrue();

    @Query("SELECT e FROM Employee e WHERE " +
           "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
           "(:designation IS NULL OR LOWER(e.designation) LIKE LOWER(CONCAT('%', :designation, '%'))) AND " +
           "(:search IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "e.active = true ORDER BY e.employeeCode ASC")
    List<Employee> filterEmployees(@Param("departmentId") Long departmentId,
                                   @Param("designation") String designation,
                                   @Param("search") String search);
}
