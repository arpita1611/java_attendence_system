<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Employee Profile: ${employee.fullName} - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Employee Profile & Attendance</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Detailed employment records and attendance logs</p>
            </div>
            <div style="display: flex; gap: 0.625rem;">
                <a href="${pageContext.request.contextPath}/employees" class="btn-secondary-custom">
                    <i class="fa-solid fa-arrow-left"></i> Employees List
                </a>
                <a href="${pageContext.request.contextPath}/employees/edit/${employee.id}" class="btn-primary-custom">
                    <i class="fa-solid fa-pen"></i> Edit Profile
                </a>
            </div>
        </div>

        <!-- Employee Info Header Card -->
        <div class="glass-card" style="margin-bottom: 1.25rem; display: flex; align-items: center; gap: 1.5rem; flex-wrap: wrap;">
            <div style="width: 64px; height: 64px; border-radius: var(--radius-md); background: var(--info-light); border: 1px solid var(--info-border); display: flex; align-items: center; justify-content: center; font-size: 1.75rem; color: var(--info); font-weight: 700; flex-shrink: 0;">
                ${employee.firstName.substring(0, 1)}
            </div>

            <div style="flex: 1; min-width: 250px;">
                <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.35rem; flex-wrap: wrap;">
                    <h2 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin: 0;">${employee.fullName}</h2>
                    <span class="badge-status ${employee.active ? 'badge-present' : 'badge-absent'}">
                        ${employee.active ? 'Active' : 'Inactive'}
                    </span>
                </div>
                <div style="display: flex; gap: 1.25rem; flex-wrap: wrap; color: var(--text-secondary); font-size: 0.875rem;">
                    <div><span style="color: var(--text-muted);">EMP Code:</span> <strong style="color: var(--info); font-family: monospace;">${employee.employeeCode}</strong></div>
                    <div><span style="color: var(--text-muted);">Designation:</span> <strong>${employee.designation}</strong></div>
                    <div><span style="color: var(--text-muted);">Dept:</span> <strong>${employee.department.name}</strong></div>
                    <c:if test="${not empty employee.email}">
                        <div><span style="color: var(--text-muted);">Email:</span> ${employee.email}</div>
                    </c:if>
                    <c:if test="${not empty employee.phone}">
                        <div><span style="color: var(--text-muted);">Phone:</span> ${employee.phone}</div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Attendance Stats KPI Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)); gap: 1rem; margin-bottom: 1.25rem;">
            
            <div class="stat-card" style="padding: 1.15rem;">
                <div style="width: 100%;">
                    <span class="stat-label">Attendance Rate</span>
                    <div class="stat-val" style="color: ${summary.attendancePercentage >= 80.0 ? 'var(--success)' : 'var(--danger)'};">
                        ${summary.attendancePercentage}%
                    </div>
                    <div class="progress-glass" style="margin-top: 0.35rem;">
                        <div class="progress-fill" style="width: ${summary.attendancePercentage}%; background: ${summary.attendancePercentage >= 80.0 ? 'var(--success)' : 'var(--danger)'};"></div>
                    </div>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Working Days</span>
                    <div class="stat-val">${summary.totalRecords}</div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">Recorded Days</span>
                </div>
                <div class="stat-icon emerald" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-calendar-check"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Present</span>
                    <div class="stat-val" style="color: var(--success);">${summary.presentCount}</div>
                    <span style="font-size: 0.75rem; color: var(--success);">Full Days</span>
                </div>
                <div class="stat-icon emerald" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-check"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Leaves / Half-Day</span>
                    <div class="stat-val" style="color: var(--primary);">${summary.onLeaveCount + summary.halfDayCount}</div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">${summary.onLeaveCount} Leave, ${summary.halfDayCount} Half-Day</span>
                </div>
                <div class="stat-icon primary" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-umbrella-beach"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Absent</span>
                    <div class="stat-val" style="color: var(--danger);">${summary.absentCount}</div>
                    <span style="font-size: 0.75rem; color: var(--danger);">Unplanned</span>
                </div>
                <div class="stat-icon rose" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-xmark"></i>
                </div>
            </div>

        </div>

        <!-- Attendance History Table -->
        <div class="glass-card">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <div>
                    <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin: 0;">Attendance & Time-Log History</h2>
                    <span style="font-size: 0.78125rem; color: var(--text-muted);">Chronological record of employee attendance and check-in/out times</span>
                </div>
            </div>

            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Check-In</th>
                            <th>Check-Out</th>
                            <th>Remarks / Note</th>
                            <th>Timestamp</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${history}">
                            <tr>
                                <td><strong style="color: var(--text-primary);">${h.attendanceDate}</strong></td>
                                <td>
                                    <span class="badge-status badge-${h.status.name().toLowerCase()}">
                                        ${h.status.displayName}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty h.checkInTime}">
                                            <span style="font-family: monospace; color: var(--success); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${h.checkInTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty h.checkOutTime}">
                                            <span style="font-family: monospace; color: var(--warning); font-weight: 600;"><i class="fa-regular fa-clock"></i> ${h.checkOutTime}</span>
                                        </c:when>
                                        <c:otherwise><span style="color: var(--text-light);">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="color: var(--text-secondary); font-size: 0.8125rem;">
                                    ${not empty h.remarks ? h.remarks : '—'}
                                </td>
                                <td style="color: var(--text-muted); font-size: 0.8125rem;">
                                    ${h.recordedAt}
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty history}">
                            <tr>
                                <td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2.5rem 1rem;">
                                    No attendance records recorded for this employee yet.
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
