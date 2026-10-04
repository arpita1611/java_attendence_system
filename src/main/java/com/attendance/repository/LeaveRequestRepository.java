package com.attendance.repository;

import com.attendance.model.LeaveRequest;
import com.attendance.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByOrderByAppliedAtDesc();

    List<LeaveRequest> findByStatusOrderByAppliedAtDesc(LeaveStatus status);

    List<LeaveRequest> findByApplicantTypeOrderByAppliedAtDesc(String applicantType);

    List<LeaveRequest> findByApplicantTypeAndStatusOrderByAppliedAtDesc(String applicantType, LeaveStatus status);

    List<LeaveRequest> findByStudentIdOrderByAppliedAtDesc(Long studentId);

    List<LeaveRequest> findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);

    long countByStatus(LeaveStatus status);
}
