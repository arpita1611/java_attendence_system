<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Employee Attendance Report - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Staff Attendance & Time Statement</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Historical employee attendance, check-in, and check-out logs</p>
            </div>
            <div style="display: flex; gap: 0.625rem;" class="no-print">
                <button onclick="window.print()" class="btn-secondary-custom">
                    <i class="fa-solid fa-print"></i> Print
                </button>
                <a href="${pageContext.request.contextPath}/reports/export/employees/csv?startDate=${startDate}&endDate=${endDate}&departmentId=${selectedDepartmentId}&status=${selectedStatus}" class="btn-primary-custom">
                    <i class="fa-solid fa-file-arrow-down"></i> Export CSV
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Criteria -->
            <form action="${pageContext.request.contextPath}/reports/employees" method="get" class="no-print" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 0.75rem; margin-bottom: 1.25rem; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
                <div>
                    <label class="form-label-custom">Start Date</label>
                    <input type="date" name="startDate" value="${startDate}" class="form-control-custom" />
                </div>
                <div>
                    <label class="form-label-custom">End Date</label>
                    <input type="date" name="endDate" value="${endDate}" class="form-control-custom" />
                </div>
                <div>
                    <label class="form-label-custom">Department</label>
                    <select name="departmentId" class="form-select-custom">
                        <option value="">All Departments</option>
                        <c:forEach var="dept" items="${departments}">
                            <option value="${dept.id}" ${dept.id == selectedDepartmentId ? 'selected' : ''}>${dept.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="form-label-custom">Status</label>
                    <select name="status" class="form-select-custom">
                        <option value="">All Statuses</option>
                        <c:forEach var="st" items="${statuses}">
                            <option value="${st}" ${st == selectedStatus ? 'selected' : ''}>${st.displayName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <button type="submit" class="btn-secondary-custom" style="width: 100%; justify-content: center; height: 38px;">
                        <i class="fa-solid fa-filter"></i> Apply Filter
                    </button>
                </div>
            </form>

            <!-- Results Summary Banner -->
            <div style="display: flex; gap: 1.5rem; margin-bottom: 1.25rem; font-size: 0.875rem; color: var(--text-primary); background: var(--bg-subtle); padding: 0.65rem 1rem; border-radius: var(--radius-sm); border: 1px solid var(--border-color); flex-wrap: wrap;">
                <div>Total Records: <strong>${records.size()}</strong></div>
                <div style="color: var(--text-muted);">Date Span: <strong style="color: var(--text-primary);">${startDate}</strong> to <strong style="color: var(--text-primary);">${endDate}</strong></div>
            </div>

            <!-- Table of Records -->
            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>EMP Code</th>
                            <th>Employee Name</th>
                            <th>Designation</th>
                            <th>Department</th>
                            <th>Status</th>
                            <th>Check-In</th>
                            <th>Check-Out</th>
                            <th>Remarks</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${records}">
                            <tr>
                                <td><strong style="color: var(--text-primary);">${r.attendanceDate}</strong></td>
                                <td>
                                    <span style="font-family: monospace; font-weight: 600; color: var(--info);">${r.employee.employeeCode}</span>
                                </td>
                                <td><strong style="color: var(--text-primary);">${r.employee.fullName}</strong></td>
                                <td style="color: var(--text-secondary);">${r.employee.designation}</td>
                                <td style="color: var(--text-secondary);">${r.employee.department.name}</td>
                                <td>
                                    <span class="badge-status badge-${r.status.name().toLowerCase()}">
                                        ${r.status.displayName}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty r.checkInTime}">
                                            <span style="font-family: monospace; color: var(--success); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${r.checkInTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty r.checkOutTime}">
                                            <span style="font-family: monospace; color: var(--warning); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${r.checkOutTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="color: var(--text-muted); font-size: 0.8125rem;">
                                    ${not empty r.remarks ? r.remarks : '—'}
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty records}">
                            <tr>
                                <td colspan="9" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    No employee attendance records found matching the specified filters.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
