<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Student Profile: ${student.fullName} - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Student Profile & Analytics</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Detailed performance and attendance breakdown</p>
            </div>
            <div style="display: flex; gap: 0.625rem;">
                <a href="${pageContext.request.contextPath}/students" class="btn-secondary-custom">
                    <i class="fa-solid fa-arrow-left"></i> Students List
                </a>
                <a href="${pageContext.request.contextPath}/students/edit/${student.id}" class="btn-primary-custom">
                    <i class="fa-solid fa-pen"></i> Edit Profile
                </a>
            </div>
        </div>

        <!-- Student Info Header Card -->
        <div class="glass-card" style="margin-bottom: 1.25rem; display: flex; align-items: center; gap: 1.5rem; flex-wrap: wrap;">
            <div style="width: 64px; height: 64px; border-radius: var(--radius-md); background: var(--primary-light); border: 1px solid var(--primary-border); display: flex; align-items: center; justify-content: center; font-size: 1.75rem; color: var(--primary); font-weight: 700; flex-shrink: 0;">
                ${student.firstName.substring(0, 1)}
            </div>

            <div style="flex: 1; min-width: 250px;">
                <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.35rem; flex-wrap: wrap;">
                    <h2 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin: 0;">${student.fullName}</h2>
                    <span class="badge-status ${student.active ? 'badge-present' : 'badge-absent'}">
                        ${student.active ? 'Active' : 'Inactive'}
                    </span>
                </div>
                <div style="display: flex; gap: 1.25rem; flex-wrap: wrap; color: var(--text-secondary); font-size: 0.875rem;">
                    <div><span style="color: var(--text-muted);">Roll:</span> <strong style="color: var(--primary); font-family: monospace;">${student.rollNumber}</strong></div>
                    <div><span style="color: var(--text-muted);">Dept:</span> <strong>${student.department.name} (${student.department.code})</strong></div>
                    <div><span style="color: var(--text-muted);">Class:</span> <strong>${student.semester} - ${student.section}</strong></div>
                    <c:if test="${not empty student.email}">
                        <div><span style="color: var(--text-muted);">Email:</span> ${student.email}</div>
                    </c:if>
                    <c:if test="${not empty student.phone}">
                        <div><span style="color: var(--text-muted);">Phone:</span> ${student.phone}</div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Attendance Stats KPI Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)); gap: 1rem; margin-bottom: 1.25rem;">
            
            <div class="stat-card" style="padding: 1.15rem;">
                <div style="width: 100%;">
                    <span class="stat-label">Attendance Rate</span>
                    <div class="stat-val" style="color: ${summary.attendancePercentage >= 75.0 ? 'var(--success)' : 'var(--danger)'};">
                        ${summary.attendancePercentage}%
                    </div>
                    <div class="progress-glass" style="margin-top: 0.35rem;">
                        <div class="progress-fill" style="width: ${summary.attendancePercentage}%; background: ${summary.attendancePercentage >= 75.0 ? 'var(--success)' : 'var(--danger)'};"></div>
                    </div>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Total Sessions</span>
                    <div class="stat-val">${summary.totalRecords}</div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">Recorded Days</span>
                </div>
                <div class="stat-icon primary" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-calendar-check"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Present</span>
                    <div class="stat-val" style="color: var(--success);">${summary.presentCount}</div>
                    <span style="font-size: 0.75rem; color: var(--success);">Attended</span>
                </div>
                <div class="stat-icon emerald" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-check"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Absent</span>
                    <div class="stat-val" style="color: var(--danger);">${summary.absentCount}</div>
                    <span style="font-size: 0.75rem; color: var(--danger);">Missed</span>
                </div>
                <div class="stat-icon rose" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-xmark"></i>
                </div>
            </div>

            <div class="stat-card" style="padding: 1.15rem;">
                <div>
                    <span class="stat-label">Late / Excused</span>
                    <div class="stat-val" style="color: var(--warning);">${summary.lateCount + summary.excusedCount}</div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">${summary.lateCount} Late, ${summary.excusedCount} Excused</span>
                </div>
                <div class="stat-icon amber" style="width: 40px; height: 40px; font-size: 1.1rem;">
                    <i class="fa-solid fa-clock"></i>
                </div>
            </div>

        </div>

        <!-- Attendance History Table -->
        <div class="glass-card">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <div>
                    <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin: 0;">Attendance History Log</h2>
                    <span style="font-size: 0.78125rem; color: var(--text-muted);">Chronological record of student attendance</span>
                </div>
            </div>

            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Remarks / Reason</th>
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
                                <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 2.5rem 1rem;">
                                    No attendance records recorded for this student yet.
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
