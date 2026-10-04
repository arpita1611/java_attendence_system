package com.attendance.service;

import com.attendance.dto.AttendanceSummaryDto;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.Employee;
import com.attendance.repository.EmployeeAttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByOrderByEmployeeCodeAsc();
    }

    public List<Employee> getAllActiveEmployees() {
        return employeeRepository.findByActiveTrueOrderByEmployeeCodeAsc();
    }

    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentIdAndActiveTrueOrderByEmployeeCodeAsc(departmentId);
    }

    public List<Employee> filterEmployees(Long departmentId, String designation, String search) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String cleanDesignation = (designation != null && !designation.trim().isEmpty()) ? designation.trim() : null;
        return employeeRepository.filterEmployees(departmentId, cleanDesignation, cleanSearch);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByCode(String code) {
        return employeeRepository.findByEmployeeCode(code);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public long countActiveEmployees() {
        return employeeRepository.countByActiveTrue();
    }

    public AttendanceSummaryDto getEmployeeAttendanceSummary(Long employeeId) {
        long total = employeeAttendanceRepository.countByEmployeeId(employeeId);
        long present = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.PRESENT);
        long absent = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.ABSENT);
        long late = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.LATE);
        long excused = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.EXCUSED);
        long halfDay = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.HALF_DAY);
        long onLeave = employeeAttendanceRepository.countByEmployeeIdAndStatus(employeeId, AttendanceStatus.ON_LEAVE);

        return new AttendanceSummaryDto(total, present, absent, late, excused, halfDay, onLeave);
    }
}
