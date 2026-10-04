package com.attendance.service;

import com.attendance.dto.CsvImportResultDto;
import com.attendance.model.Department;
import com.attendance.model.Employee;
import com.attendance.model.Student;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class CsvImportService {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public CsvImportService(StudentRepository studentRepository,
                            EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public CsvImportResultDto importStudents(MultipartFile file) {
        CsvImportResultDto result = new CsvImportResultDto();

        if (file.isEmpty()) {
            result.addError("Uploaded file is empty.");
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header line
                if (lineNumber == 1 && (line.toLowerCase().contains("roll") || line.toLowerCase().contains("first name"))) {
                    continue;
                }

                String[] tokens = parseCsvLine(line);
                if (tokens.length < 4) {
                    result.addError("Row " + lineNumber + ": Insufficient columns (expected Roll Number, First Name, Last Name, Department Code, Semester, Section, Email, Phone, Gender)");
                    continue;
                }

                String roll = tokens[0].trim();
                String firstName = tokens[1].trim();
                String lastName = tokens[2].trim();
                String deptCode = tokens[3].trim();
                String semester = tokens.length > 4 ? tokens[4].trim() : "Semester 1";
                String section = tokens.length > 5 ? tokens[5].trim() : "A";
                String email = tokens.length > 6 ? tokens[6].trim() : "";
                String phone = tokens.length > 7 ? tokens[7].trim() : "";
                String gender = tokens.length > 8 ? tokens[8].trim().toUpperCase() : "MALE";

                if (roll.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    result.addError("Row " + lineNumber + ": Roll Number, First Name, and Last Name are required.");
                    continue;
                }

                if (studentRepository.findByRollNumberIgnoreCase(roll).isPresent()) {
                    result.addError("Row " + lineNumber + ": Student with Roll Number '" + roll + "' already exists.");
                    continue;
                }

                Department dept = getOrCreateDepartment(deptCode);
                Student student = new Student(roll, firstName, lastName, email, phone, gender, semester, section, dept);
                studentRepository.save(student);

                result.addSuccess("Imported " + firstName + " " + lastName + " (" + roll + ")");
            }

        } catch (Exception e) {
            result.addError("File reading error: " + e.getMessage());
        }

        return result;
    }

    @Transactional
    public CsvImportResultDto importEmployees(MultipartFile file) {
        CsvImportResultDto result = new CsvImportResultDto();

        if (file.isEmpty()) {
            result.addError("Uploaded file is empty.");
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header line
                if (lineNumber == 1 && (line.toLowerCase().contains("code") || line.toLowerCase().contains("designation"))) {
                    continue;
                }

                String[] tokens = parseCsvLine(line);
                if (tokens.length < 4) {
                    result.addError("Row " + lineNumber + ": Insufficient columns (expected Employee Code, First Name, Last Name, Department Code, Designation, Employment Type, Email, Phone, Gender)");
                    continue;
                }

                String empCode = tokens[0].trim();
                String firstName = tokens[1].trim();
                String lastName = tokens[2].trim();
                String deptCode = tokens[3].trim();
                String designation = tokens.length > 4 ? tokens[4].trim() : "Faculty";
                String empType = tokens.length > 5 ? tokens[5].trim().toUpperCase() : "FULL_TIME";
                String email = tokens.length > 6 ? tokens[6].trim() : "";
                String phone = tokens.length > 7 ? tokens[7].trim() : "";
                String gender = tokens.length > 8 ? tokens[8].trim().toUpperCase() : "MALE";

                if (empCode.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    result.addError("Row " + lineNumber + ": Employee Code, First Name, and Last Name are required.");
                    continue;
                }

                if (employeeRepository.findByEmployeeCodeIgnoreCase(empCode).isPresent()) {
                    result.addError("Row " + lineNumber + ": Employee with Code '" + empCode + "' already exists.");
                    continue;
                }

                Department dept = getOrCreateDepartment(deptCode);
                Employee employee = new Employee(empCode, firstName, lastName, email, phone, gender, designation, dept, empType);
                employeeRepository.save(employee);

                result.addSuccess("Imported " + firstName + " " + lastName + " (" + empCode + ")");
            }

        } catch (Exception e) {
            result.addError("File reading error: " + e.getMessage());
        }

        return result;
    }

    private Department getOrCreateDepartment(String code) {
        if (code == null || code.trim().isEmpty()) {
            code = "GEN";
        }
        String cleanCode = code.trim().toUpperCase();
        Optional<Department> opt = departmentRepository.findByCodeIgnoreCase(cleanCode);
        if (opt.isPresent()) {
            return opt.get();
        }

        Department newDept = new Department("Department of " + cleanCode, cleanCode, "Auto-created on bulk import");
        return departmentRepository.save(newDept);
    }

    private String[] parseCsvLine(String line) {
        // Simple robust CSV tokenizer handling optional quotes
        String[] rawTokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < rawTokens.length; i++) {
            rawTokens[i] = rawTokens[i].replace("\"", "").trim();
        }
        return rawTokens;
    }

    public ByteArrayInputStream getStudentTemplateCsv() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            writer.println("Roll Number,First Name,Last Name,Department Code,Semester,Section,Email,Phone,Gender");
            writer.println("CSE-2024-101,John,Doe,CSE,Semester 4,Section A,john.doe@univ.edu,+91 9876543201,MALE");
            writer.println("CSE-2024-102,Jane,Smith,CSE,Semester 4,Section A,jane.smith@univ.edu,+91 9876543202,FEMALE");
            writer.println("MECH-2024-101,Alex,Taylor,MECH,Semester 2,Section B,alex.t@univ.edu,+91 9876543203,MALE");
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream getEmployeeTemplateCsv() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            writer.println("Employee Code,First Name,Last Name,Department Code,Designation,Employment Type,Email,Phone,Gender");
            writer.println("EMP-5001,Robert,Langdon,CSE,Professor,FULL_TIME,robert.l@univ.edu,+91 9820055111,MALE");
            writer.println("EMP-5002,Sophie,Neveu,MECH,Assistant Professor,FULL_TIME,sophie.n@univ.edu,+91 9820055112,FEMALE");
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
