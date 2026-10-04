<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Employees - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header & Action Row -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Employees</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Directory of faculty and administrative staff members.</p>
            </div>
            <div style="display: flex; gap: 0.625rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/attendance/employees/mark" class="btn-secondary-custom">
                    <i class="fa-solid fa-fingerprint"></i> Mark Attendance
                </a>
                <a href="${pageContext.request.contextPath}/employees/new" class="btn-primary-custom">
                    <i class="fa-solid fa-plus"></i> Add Employee
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Toolbar -->
            <form action="${pageContext.request.contextPath}/employees" method="get" style="display: flex; gap: 0.75rem; margin-bottom: 1.25rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: center;">
                <div style="flex: 2; min-width: 200px;">
                    <input type="text" name="search" value="${search}" placeholder="Search name or employee code..." class="form-control-custom">
                </div>
                <div style="flex: 1.5; min-width: 180px;">
                    <select name="departmentId" class="form-select-custom">
                        <option value="">All Departments</option>
                        <c:forEach var="dept" items="${departments}">
                            <option value="${dept.id}" ${dept.id == selectedDepartmentId ? 'selected' : ''}>${dept.name} (${dept.code})</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="flex: 1; min-width: 140px;">
                    <input type="text" name="designation" value="${selectedDesignation}" placeholder="e.g. Professor" class="form-control-custom">
                </div>
                <button type="submit" class="btn-secondary-custom" style="padding: 0.55rem 1rem;">
                    <i class="fa-solid fa-filter"></i> Filter
                </button>
                <c:if test="${not empty search || not empty selectedDepartmentId || not empty selectedDesignation}">
                    <a href="${pageContext.request.contextPath}/employees" class="btn-icon" title="Reset Filters">
                        <i class="fa-solid fa-rotate-left"></i>
                    </a>
                </c:if>
            </form>

            <!-- Employees Table -->
            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>EMP Code</th>
                            <th>Employee Name</th>
                            <th>Designation</th>
                            <th>Department</th>
                            <th>Contact</th>
                            <th>Type</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${employees}">
                            <tr>
                                <td>
                                    <span style="font-family: monospace; font-weight: 600; color: var(--info); font-size: 0.875rem;">${e.employeeCode}</span>
                                </td>
                                <td>
                                    <div style="font-weight: 600; color: var(--text-primary);">${e.fullName}</div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">${e.email}</div>
                                </td>
                                <td><span style="color: var(--text-primary); font-weight: 500;">${e.designation}</span></td>
                                <td style="color: var(--text-secondary);">${e.department != null ? e.department.name : '—'}</td>
                                <td style="font-size: 0.8125rem; color: var(--text-muted);">${not empty e.phone ? e.phone : '—'}</td>
                                <td>
                                    <span style="font-size: 0.75rem; color: var(--text-secondary); background: var(--bg-subtle); padding: 0.2rem 0.5rem; border-radius: var(--radius-xs); border: 1px solid var(--border-color);">
                                        ${e.employmentType}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge-status ${e.active ? 'badge-present' : 'badge-absent'}">
                                        ${e.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td style="text-align: right;">
                                    <div style="display: inline-flex; gap: 0.35rem;">
                                        <a href="${pageContext.request.contextPath}/employees/view/${e.id}" class="btn-icon" title="View Profile & Logs">
                                            <i class="fa-solid fa-chart-line" style="color: var(--primary);"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/employees/edit/${e.id}" class="btn-icon" title="Edit Employee">
                                            <i class="fa-solid fa-pen" style="color: var(--text-secondary);"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/employees/delete/${e.id}" onclick="return confirm('Delete employee ${e.fullName}?');" class="btn-icon" title="Delete Employee">
                                            <i class="fa-solid fa-trash" style="color: var(--danger);"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty employees}">
                            <tr>
                                <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    <i class="fa-solid fa-user-slash" style="font-size: 2rem; display: block; margin-bottom: 0.5rem; color: var(--text-light);"></i>
                                    No employees found.
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
