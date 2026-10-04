<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Attendance Defaulters - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header Section -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">
                    Attendance Defaulters
                </h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">
                    Students below the required attendance threshold (&lt; ${threshold}%)
                </p>
            </div>
            <div style="display: flex; gap: 0.625rem;" class="no-print">
                <button onclick="window.print()" class="btn-secondary-custom">
                    <i class="fa-solid fa-print"></i> Print List
                </button>
                <a href="${pageContext.request.contextPath}/reports/defaulters/export/csv?threshold=${threshold}&departmentId=${selectedDepartmentId}&semester=${selectedSemester}" class="btn-primary-custom">
                    <i class="fa-solid fa-file-csv"></i> Export CSV
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Toolbar -->
            <form action="${pageContext.request.contextPath}/reports/defaulters" method="get" class="no-print" style="display: flex; gap: 0.75rem; margin-bottom: 1.25rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
                <div style="width: 190px;">
                    <label class="form-label-custom">Shortage Threshold</label>
                    <select name="threshold" class="form-select-custom">
                        <option value="65.0" ${threshold == 65.0 ? 'selected' : ''}>&lt; 65% (Critical Shortage)</option>
                        <option value="70.0" ${threshold == 70.0 ? 'selected' : ''}>&lt; 70% (High Risk)</option>
                        <option value="75.0" ${threshold == 75.0 ? 'selected' : ''}>&lt; 75% (Mandatory Standard)</option>
                        <option value="80.0" ${threshold == 80.0 ? 'selected' : ''}>&lt; 80% (Warning Zone)</option>
                    </select>
                </div>
                <div style="flex: 2; min-width: 200px;">
                    <label class="form-label-custom">Department</label>
                    <select name="departmentId" class="form-select-custom">
                        <option value="">All Departments</option>
                        <c:forEach var="dept" items="${departments}">
                            <option value="${dept.id}" ${dept.id == selectedDepartmentId ? 'selected' : ''}>${dept.name} (${dept.code})</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="flex: 1.5; min-width: 140px;">
                    <label class="form-label-custom">Semester</label>
                    <input type="text" name="semester" value="${selectedSemester}" placeholder="e.g. Semester 4" class="form-control-custom" />
                </div>
                <div>
                    <button type="submit" class="btn-secondary-custom" style="padding: 0.55rem 1rem;">
                        <i class="fa-solid fa-filter"></i> Run Audit
                    </button>
                </div>
            </form>

            <!-- Summary Alert Box -->
            <div style="display: flex; gap: 1rem; margin-bottom: 1.25rem; background: var(--danger-light); border: 1px solid var(--danger-border); padding: 0.875rem 1.25rem; border-radius: var(--radius-md); align-items: center; flex-wrap: wrap;">
                <div style="width: 36px; height: 36px; border-radius: var(--radius-sm); background: #FFFFFF; color: var(--danger); display: flex; align-items: center; justify-content: center; font-size: 1.1rem; border: 1px solid var(--danger-border); flex-shrink: 0;">
                    <i class="fa-solid fa-bell"></i>
                </div>
                <div>
                    <div style="font-size: 0.9375rem; font-weight: 600; color: var(--danger-text);">
                        <strong>${defaulters.size()}</strong> Student(s) identified below <strong>${threshold}%</strong> attendance criteria
                    </div>
                    <span style="font-size: 0.8125rem; color: var(--text-secondary);">
                        Academic policy mandates at least 75% attendance for examination eligibility.
                    </span>
                </div>
            </div>

            <!-- Defaulters Table -->
            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Roll Number</th>
                            <th>Student Name</th>
                            <th>Department & Class</th>
                            <th>Attendance %</th>
                            <th>Attended / Total</th>
                            <th>Absent Days</th>
                            <th>Recovery Classes</th>
                            <th>Contact</th>
                            <th style="text-align: right;" class="no-print">Notice</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${defaulters}">
                            <tr>
                                <td>
                                    <span style="font-family: monospace; font-weight: 600; color: var(--primary);">${d.rollNumber}</span>
                                </td>
                                <td>
                                    <strong style="color: var(--text-primary);">${d.studentName}</strong>
                                </td>
                                <td>
                                    <div style="font-size: 0.8125rem; color: var(--text-secondary);">${d.departmentName}</div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">${d.semester} • ${d.section}</div>
                                </td>
                                <td>
                                    <span class="badge-status badge-absent" style="font-weight: 700;">
                                        ${d.attendancePercentage}%
                                    </span>
                                </td>
                                <td>
                                    <strong style="color: var(--text-primary);">${d.attendedSessions}</strong> / <span style="color: var(--text-muted);">${d.totalSessions}</span>
                                </td>
                                <td>
                                    <span style="color: var(--danger); font-weight: 600; font-size: 0.8125rem;">${d.absentSessions} days</span>
                                </td>
                                <td>
                                    <span class="badge-status badge-late">
                                        +${d.classesNeededFor75} classes
                                    </span>
                                </td>
                                <td>
                                    <div style="font-size: 0.78125rem; color: var(--text-secondary);"><i class="fa-solid fa-phone" style="color: var(--text-light); font-size: 0.7rem;"></i> ${d.phone}</div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);"><i class="fa-solid fa-envelope" style="color: var(--text-light); font-size: 0.7rem;"></i> ${d.email}</div>
                                </td>
                                <td style="text-align: right;" class="no-print">
                                    <button onclick="openNoticeModal('${d.studentName}', '${d.rollNumber}', '${d.departmentName}', '${d.semester}', '${d.attendancePercentage}', '${d.attendedSessions}', '${d.totalSessions}', '${d.classesNeededFor75}')" class="btn-icon" title="View Official Advisory Letter" style="color: var(--primary);">
                                        <i class="fa-solid fa-file-invoice"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty defaulters}">
                            <tr>
                                <td colspan="9" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    <i class="fa-solid fa-circle-check" style="font-size: 2rem; color: var(--success); display: block; margin-bottom: 0.5rem;"></i>
                                    <h3 style="color: var(--text-primary); font-size: 1.1rem; margin-bottom: 0.25rem;">No Defaulters Found</h3>
                                    <p style="font-size: 0.875rem;">All students meet or exceed the ${threshold}% attendance requirement.</p>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<!-- Warning Notice Modal Template (Printable) -->
<div id="noticeModal" style="display: none; position: fixed; inset: 0; background: rgba(17, 24, 39, 0.6); z-index: 2000; align-items: center; justify-content: center; padding: 1.5rem;">
    <div class="glass-card" style="max-width: 650px; width: 100%; max-height: 90vh; overflow-y: auto; background: #FFFFFF; border: 1px solid var(--border-color); box-shadow: var(--shadow-lg);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.75rem;">
            <div style="display: flex; align-items: center; gap: 0.5rem;">
                <i class="fa-solid fa-envelope-open-text" style="color: var(--warning); font-size: 1.25rem;"></i>
                <h3 style="color: var(--text-primary); font-size: 1.1rem; font-weight: 600; margin: 0;">Attendance Shortage Notice</h3>
            </div>
            <button onclick="closeNoticeModal()" class="btn-icon" style="border: none;"><i class="fa-solid fa-xmark"></i></button>
        </div>

        <div id="noticeLetterBody" style="background: #FFFFFF; color: #111827; padding: 1.5rem; border: 1px solid var(--border-color); border-radius: 6px; font-family: 'Times New Roman', serif; line-height: 1.6;">
            <div style="text-align: center; border-bottom: 2px solid #111827; padding-bottom: 0.75rem; margin-bottom: 1.25rem;">
                <h2 style="font-size: 1.3rem; font-weight: bold; margin-bottom: 0.2rem; text-transform: uppercase;">Office of Academic Affairs</h2>
                <div style="font-size: 0.85rem; color: #4B5563;">ATTENDANCE REGULATION ADVISORY MEMORANDUM</div>
            </div>

            <div style="margin-bottom: 1rem; font-size: 0.9rem;">
                <strong>Date:</strong> <span id="noticeDate"><%= java.time.LocalDate.now() %></span><br>
                <strong>To:</strong> Parent / Guardian of <span id="modalStudentName" style="font-weight: bold; text-decoration: underline;"></span><br>
                <strong>Roll Number:</strong> <span id="modalRollNumber" style="font-family: monospace;"></span><br>
                <strong>Department:</strong> <span id="modalDepartment"></span> (<span id="modalSemester"></span>)
            </div>

            <p style="margin-bottom: 1rem; font-size: 0.9rem;">
                This is an official notice regarding the attendance record of your ward. As per institutional academic regulations, students are required to maintain a minimum of <strong>75% aggregate attendance</strong> to remain eligible for end-semester examinations.
            </p>

            <table style="width: 100%; border-collapse: collapse; margin: 1rem 0; font-size: 0.875rem;">
                <tr style="background: #F8FAFC;">
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem;"><strong>Current Attendance Rate:</strong></td>
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem; color: #DC2626; font-weight: bold;"><span id="modalPercentage"></span>%</td>
                </tr>
                <tr>
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem;"><strong>Attended Sessions:</strong></td>
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem;"><span id="modalAttended"></span> of <span id="modalTotal"></span> classes</td>
                </tr>
                <tr style="background: #F8FAFC;">
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem;"><strong>Recovery Classes Required for 75%:</strong></td>
                    <td style="border: 1px solid #E5E7EB; padding: 0.5rem 0.75rem; font-weight: bold; color: #D97706;">+<span id="modalNeeded"></span> classes</td>
                </tr>
            </table>

            <p style="margin-bottom: 1.5rem; font-size: 0.9rem;">
                Failure to regularize attendance immediately may lead to examination debarment. Please meet the Department Head within 3 working days.
            </p>

            <div style="display: flex; justify-content: space-between; margin-top: 2rem; padding-top: 1rem;">
                <div>
                    ____________________<br>
                    <strong>Faculty Advisor</strong>
                </div>
                <div style="text-align: right;">
                    ____________________<br>
                    <strong>Head of Department</strong>
                </div>
            </div>
        </div>

        <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.25rem;" class="no-print">
            <button onclick="closeNoticeModal()" class="btn-secondary-custom">Close</button>
            <button onclick="printNoticeLetter()" class="btn-primary-custom">
                <i class="fa-solid fa-print"></i> Print Letter
            </button>
        </div>
    </div>
</div>

<script>
function openNoticeModal(name, roll, dept, sem, pct, attended, total, needed) {
    document.getElementById('modalStudentName').textContent = name;
    document.getElementById('modalRollNumber').textContent = roll;
    document.getElementById('modalDepartment').textContent = dept;
    document.getElementById('modalSemester').textContent = sem;
    document.getElementById('modalPercentage').textContent = pct;
    document.getElementById('modalAttended').textContent = attended;
    document.getElementById('modalTotal').textContent = total;
    document.getElementById('modalNeeded').textContent = needed;
    
    document.getElementById('noticeModal').style.display = 'flex';
}

function closeNoticeModal() {
    document.getElementById('noticeModal').style.display = 'none';
}

function printNoticeLetter() {
    const printContents = document.getElementById('noticeLetterBody').innerHTML;
    const printWindow = window.open('', '_blank');
    printWindow.document.write('<html><head><title>Attendance Shortage Notice</title><style>body{font-family:serif;padding:2rem;line-height:1.6;}</style></head><body>' + printContents + '</body></html>');
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
    printWindow.close();
}
</script>

<jsp:include page="../common/footer.jsp" />
