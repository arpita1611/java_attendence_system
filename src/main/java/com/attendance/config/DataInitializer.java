package com.attendance.config;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public DataInitializer(DepartmentRepository departmentRepository,
                           StudentRepository studentRepository,
                           EmployeeRepository employeeRepository,
                           StudentAttendanceRepository studentAttendanceRepository,
                           EmployeeAttendanceRepository employeeAttendanceRepository,
                           LeaveRequestRepository leaveRequestRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println("Initializing sample data for Attendance System...");

        // 1. Create Departments
        Department cse = new Department("Computer Science & Engineering", "CSE", "Department of Computer Science and Engineering");
        Department mech = new Department("Mechanical Engineering", "MECH", "Department of Mechanical Engineering");
        Department bba = new Department("Business Administration", "BBA", "School of Business and Management Studies");
        Department hr = new Department("Human Resources & Admin", "HR", "Corporate Human Resources and Operations Management");

        cse = departmentRepository.save(cse);
        mech = departmentRepository.save(mech);
        bba = departmentRepository.save(bba);
        hr = departmentRepository.save(hr);

        // 2. Create Students
        List<Student> students = new ArrayList<>();
        students.add(new Student("CSE-2024-001", "Aarav", "Sharma", "aarav.sharma@univ.edu", "+91 9876543210", "MALE", "Semester 4", "Section A", cse));
        students.add(new Student("CSE-2024-002", "Diya", "Patel", "diya.patel@univ.edu", "+91 9876543211", "FEMALE", "Semester 4", "Section A", cse));
        students.add(new Student("CSE-2024-003", "Rohan", "Verma", "rohan.verma@univ.edu", "+91 9876543212", "MALE", "Semester 4", "Section B", cse));
        students.add(new Student("CSE-2024-004", "Ananya", "Iyer", "ananya.iyer@univ.edu", "+91 9876543213", "FEMALE", "Semester 4", "Section B", cse));
        students.add(new Student("CSE-2024-005", "Kabir", "Mehta", "kabir.mehta@univ.edu", "+91 9876543214", "MALE", "Semester 6", "Section A", cse));
        students.add(new Student("CSE-2024-006", "Sanya", "Gupta", "sanya.gupta@univ.edu", "+91 9876543215", "FEMALE", "Semester 6", "Section A", cse));

        students.add(new Student("MECH-2024-001", "Vikram", "Singh", "vikram.singh@univ.edu", "+91 9876543220", "MALE", "Semester 4", "Section A", mech));
        students.add(new Student("MECH-2024-002", "Neha", "Reddy", "neha.reddy@univ.edu", "+91 9876543221", "FEMALE", "Semester 4", "Section A", mech));
        students.add(new Student("MECH-2024-003", "Aditya", "Joshi", "aditya.joshi@univ.edu", "+91 9876543222", "MALE", "Semester 6", "Section A", mech));
        students.add(new Student("MECH-2024-004", "Pooja", "Chopra", "pooja.chopra@univ.edu", "+91 9876543223", "FEMALE", "Semester 6", "Section A", mech));

        students.add(new Student("BBA-2024-001", "Aryan", "Malhotra", "aryan.m@univ.edu", "+91 9876543230", "MALE", "Semester 2", "Section A", bba));
        students.add(new Student("BBA-2024-002", "Rhea", "Kapoor", "rhea.k@univ.edu", "+91 9876543231", "FEMALE", "Semester 2", "Section A", bba));
        students.add(new Student("BBA-2024-003", "Kunal", "Deshmukh", "kunal.d@univ.edu", "+91 9876543232", "MALE", "Semester 4", "Section A", bba));
        students.add(new Student("BBA-2024-004", "Tara", "Bose", "tara.b@univ.edu", "+91 9876543233", "FEMALE", "Semester 4", "Section A", bba));
        students.add(new Student("BBA-2024-005", "Dev", "Nair", "dev.n@univ.edu", "+91 9876543234", "MALE", "Semester 4", "Section B", bba));
        students.add(new Student("BBA-2024-006", "Ishita", "Sengupta", "ishita.s@univ.edu", "+91 9876543235", "FEMALE", "Semester 4", "Section B", bba));

        students = studentRepository.saveAll(students);

        // 3. Create Employees
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("EMP-1001", "Dr. Rajesh", "Kumar", "rajesh.kumar@institution.org", "+91 9820011223", "MALE", "Professor & HOD", cse, "FULL_TIME"));
        employees.add(new Employee("EMP-1002", "Dr. Meenakshi", "Sundaram", "meenakshi.s@institution.org", "+91 9820011224", "FEMALE", "Associate Professor", cse, "FULL_TIME"));
        employees.add(new Employee("EMP-1003", "Amitabh", "Saxena", "amitabh.s@institution.org", "+91 9820011225", "MALE", "Assistant Professor", cse, "FULL_TIME"));
        employees.add(new Employee("EMP-1004", "Priyanka", "Nair", "priyanka.n@institution.org", "+91 9820011226", "FEMALE", "Lab Coordinator", cse, "FULL_TIME"));

        employees.add(new Employee("EMP-2001", "Dr. Harish", "Rawat", "harish.rawat@institution.org", "+91 9820022331", "MALE", "Professor & HOD", mech, "FULL_TIME"));
        employees.add(new Employee("EMP-2002", "Suresh", "Menon", "suresh.menon@institution.org", "+91 9820022332", "MALE", "Senior Lecturer", mech, "FULL_TIME"));

        employees.add(new Employee("EMP-3001", "Dr. Shalini", "Mishra", "shalini.m@institution.org", "+91 9820033441", "FEMALE", "Dean & Professor", bba, "FULL_TIME"));
        employees.add(new Employee("EMP-3002", "Gaurav", "Tandon", "gaurav.t@institution.org", "+91 9820033442", "MALE", "Assistant Professor", bba, "FULL_TIME"));

        employees.add(new Employee("EMP-4001", "Simran", "Bhatia", "simran.b@institution.org", "+91 9820044551", "FEMALE", "Head of Human Resources", hr, "FULL_TIME"));
        employees.add(new Employee("EMP-4002", "Nitin", "Kulkarni", "nitin.k@institution.org", "+91 9820044552", "MALE", "Operations Executive", hr, "FULL_TIME"));

        employees = employeeRepository.saveAll(employees);

        // 4. Seed Historical Attendance for past 14 days
        Random rand = new Random(42);
        LocalDate today = LocalDate.now();

        List<StudentAttendance> studentAttendances = new ArrayList<>();
        List<EmployeeAttendance> employeeAttendances = new ArrayList<>();

        for (int dayOffset = 14; dayOffset >= 0; dayOffset--) {
            LocalDate date = today.minusDays(dayOffset);

            // Skip Sundays
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            // Seed Student Attendance
            for (Student s : students) {
                // Give Kabir Mehta and Pooja Chopra lower attendance to trigger Defaulter engine
                boolean isLowAttendanceStudent = s.getRollNumber().equals("CSE-2024-005") || s.getRollNumber().equals("MECH-2024-004");
                int r = rand.nextInt(100);
                AttendanceStatus status;
                String remarks = null;

                if (isLowAttendanceStudent) {
                    if (r < 55) {
                        status = AttendanceStatus.ABSENT;
                        remarks = "Uninformed Absence";
                    } else if (r < 80) {
                        status = AttendanceStatus.PRESENT;
                    } else {
                        status = AttendanceStatus.LATE;
                        remarks = "Late arrival";
                    }
                } else {
                    if (r < 82) {
                        status = AttendanceStatus.PRESENT;
                    } else if (r < 90) {
                        status = AttendanceStatus.LATE;
                        remarks = "Traffic delay (15 min)";
                    } else if (r < 96) {
                        status = AttendanceStatus.ABSENT;
                        remarks = "Uninformed absence";
                    } else {
                        status = AttendanceStatus.EXCUSED;
                        remarks = "Medical leave letter submitted";
                    }
                }

                StudentAttendance sa = new StudentAttendance(s, date, status, remarks);
                sa.setRecordedAt(date.atTime(9, rand.nextInt(30)));
                studentAttendances.add(sa);
            }

            // Seed Employee Attendance
            for (Employee e : employees) {
                int r = rand.nextInt(100);
                AttendanceStatus status;
                LocalTime checkIn = LocalTime.of(8, 45).plusMinutes(rand.nextInt(25));
                LocalTime checkOut = LocalTime.of(17, 30).plusMinutes(rand.nextInt(30));
                String remarks = null;

                if (r < 88) {
                    status = AttendanceStatus.PRESENT;
                } else if (r < 94) {
                    status = AttendanceStatus.ON_LEAVE;
                    checkIn = null;
                    checkOut = null;
                    remarks = "Casual Leave approved";
                } else if (r < 97) {
                    status = AttendanceStatus.HALF_DAY;
                    checkOut = LocalTime.of(13, 30);
                    remarks = "Personal errand in afternoon";
                } else {
                    status = AttendanceStatus.ABSENT;
                    checkIn = null;
                    checkOut = null;
                    remarks = "Sick leave";
                }

                EmployeeAttendance ea = new EmployeeAttendance(e, date, status, checkIn, checkOut, remarks);
                ea.setRecordedAt(date.atTime(8, 50));
                employeeAttendances.add(ea);
            }
        }

        studentAttendanceRepository.saveAll(studentAttendances);
        employeeAttendanceRepository.saveAll(employeeAttendances);

        // 5. Seed Sample Leave Requests
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest("STUDENT", students.get(1), null, LeaveType.MEDICAL,
                today.plusDays(1), today.plusDays(3), "Viral fever recovery with medical prescription"));
        leaveRequests.add(new LeaveRequest("STUDENT", students.get(3), null, LeaveType.DUTY_LEAVE,
                today.plusDays(2), today.plusDays(4), "State Inter-Collegiate Debate Championship participation"));
        
        LeaveRequest empLeave1 = new LeaveRequest("EMPLOYEE", null, employees.get(2), LeaveType.CASUAL,
                today.minusDays(2), today.minusDays(1), "Family wedding attendance in hometown");
        empLeave1.setStatus(LeaveStatus.APPROVED);
        empLeave1.setAdminRemarks("Approved by HOD");
        empLeave1.setReviewedAt(LocalDateTime.now().minusDays(3));
        leaveRequests.add(empLeave1);

        LeaveRequest empLeave2 = new LeaveRequest("EMPLOYEE", null, employees.get(4), LeaveType.ON_DUTY,
                today.plusDays(1), today.plusDays(2), "AICTE National Curriculum Board Review Meeting");
        leaveRequests.add(empLeave2);

        leaveRequestRepository.saveAll(leaveRequests);

        System.out.println("Sample data initialization with leaves and attendance completed successfully!");
    }
}
