<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Employee Daily Logs - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Employee Attendance Daily Logs</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Detailed attendance and check-in/out records for selected date</p>
            </div>
            <div style="display: flex; gap: 0.625rem;">
                <a href="${pageContext.request.contextPath}/attendance/employees/mark?date=${selectedDate}" class="btn-primary-custom">
                    <i class="fa-solid fa-pen-to-square"></i> Edit Attendance
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Date Selector -->
            <form action="${pageContext.request.contextPath}/attendance/employees/logs" method="get" style="display: flex; gap: 0.75rem; margin-bottom: 1.25rem; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end; width: fit-content;">
                <div>
                    <label class="form-label-custom">Select Date</label>
                    <input type="date" name="date" value="${selectedDate}" class="form-control-custom" />
                </div>
                <button type="submit" class="btn-secondary-custom" style="padding: 0.55rem 1rem;">
                    <i class="fa-solid fa-calendar-check"></i> View Records
                </button>
            </form>

            <!-- Logs Table -->
            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
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
                        <c:forEach var="log" items="${logs}">
                            <tr>
                                <td>
                                    <span style="font-family: monospace; font-weight: 600; color: var(--info);">${log.employee.employeeCode}</span>
                                </td>
                                <td><strong style="color: var(--text-primary);">${log.employee.fullName}</strong></td>
                                <td style="color: var(--text-secondary);">${log.employee.designation}</td>
                                <td style="color: var(--text-secondary);">${log.employee.department.name}</td>
                                <td>
                                    <span class="badge-status badge-${log.status.name().toLowerCase()}">
                                        ${log.status.displayName}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty log.checkInTime}">
                                            <span style="font-family: monospace; color: var(--success); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${log.checkInTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty log.checkOutTime}">
                                            <span style="font-family: monospace; color: var(--warning); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${log.checkOutTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="color: var(--text-muted); font-size: 0.8125rem;">
                                    ${not empty log.remarks ? log.remarks : '—'}
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty logs}">
                            <tr>
                                <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    <i class="fa-regular fa-calendar-xmark" style="font-size: 2rem; display: block; margin-bottom: 0.5rem; color: var(--text-light);"></i>
                                    No employee attendance records found for ${selectedDate}.
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
