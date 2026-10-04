<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Employee Monthly Matrix - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">
                    Staff & Faculty Monthly Attendance Matrix (${monthName} ${selectedYear})
                </h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Overview of employee workdays, leaves, and presence percentage</p>
            </div>
            <div style="display: flex; gap: 0.625rem;" class="no-print">
                <button onclick="window.print()" class="btn-secondary-custom">
                    <i class="fa-solid fa-print"></i> Print Matrix
                </button>
            </div>
        </div>

        <div class="glass-card">
            <!-- Month & Filter Selector Form -->
            <form action="${pageContext.request.contextPath}/reports/employees/matrix" method="get" class="no-print" style="display: flex; gap: 0.75rem; margin-bottom: 1.25rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
                <div style="width: 140px;">
                    <label class="form-label-custom">Month</label>
                    <select name="month" class="form-select-custom">
                        <c:forEach var="m" begin="1" end="12">
                            <option value="${m}" ${m == selectedMonth ? 'selected' : ''}>Month ${m}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="width: 110px;">
                    <label class="form-label-custom">Year</label>
                    <input type="number" name="year" value="${selectedYear}" class="form-control-custom" />
                </div>
                <div style="flex: 2; min-width: 180px;">
                    <label class="form-label-custom">Department</label>
                    <select name="departmentId" class="form-select-custom">
                        <option value="">All Departments</option>
                        <c:forEach var="dept" items="${departments}">
                            <option value="${dept.id}" ${dept.id == selectedDepartmentId ? 'selected' : ''}>${dept.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <button type="submit" class="btn-primary-custom" style="padding: 0.55rem 1.15rem;">
                        <i class="fa-solid fa-calendar-days"></i> Generate Matrix
                    </button>
                </div>
            </form>

            <!-- Legend -->
            <div style="display: flex; gap: 1rem; margin-bottom: 1rem; align-items: center; flex-wrap: wrap; font-size: 0.8125rem; background: var(--bg-subtle); padding: 0.5rem 0.875rem; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                <span style="color: var(--text-secondary); font-weight: 600;">Legend:</span>
                <div style="display: flex; align-items: center; gap: 0.35rem;">
                    <span class="matrix-cell matrix-P">P</span> <span style="color: var(--success); font-weight: 500;">Present</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.35rem;">
                    <span class="matrix-cell matrix-A">A</span> <span style="color: var(--danger); font-weight: 500;">Absent</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.35rem;">
                    <span class="matrix-cell matrix-E">L</span> <span style="color: var(--primary); font-weight: 500;">On Leave</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.35rem;">
                    <span class="matrix-cell matrix-H">H</span> <span style="color: #0F766E; font-weight: 500;">Half Day</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.35rem;">
                    <span class="matrix-cell matrix-EMPTY">·</span> <span style="color: var(--text-muted);">Off Day</span>
                </div>
            </div>

            <!-- Matrix Calendar Grid -->
            <div class="matrix-container">
                <table class="matrix-table">
                    <thead>
                        <tr>
                            <th class="sticky-col">Employee Details</th>
                            <c:forEach var="day" begin="1" end="${daysInMonth}">
                                <th>${day}</th>
                            </c:forEach>
                            <th style="color: var(--success); min-width: 36px;">P</th>
                            <th style="color: var(--danger); min-width: 36px;">A</th>
                            <th style="color: var(--primary); min-width: 36px;">Leave</th>
                            <th style="color: var(--text-primary); min-width: 60px;">Rate</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="row" items="${matrix}">
                            <tr>
                                <td class="sticky-col">
                                    <span style="font-family: monospace; font-weight: 600; color: var(--info); font-size: 0.78125rem; display: block;">${row.identifier}</span>
                                    <span style="color: var(--text-primary); font-weight: 600;">${row.name}</span>
                                    <span style="display: block; font-size: 0.7rem; color: var(--text-muted);">${row.departmentName} (${row.subInfo})</span>
                                </td>

                                <c:forEach var="day" begin="1" end="${daysInMonth}">
                                    <c:set var="status" value="${row.dailyStatus[day]}" />
                                    <td>
                                        <c:choose>
                                            <c:when test="${status == 'PRESENT'}">
                                                <span class="matrix-cell matrix-P" title="Day ${day}: Present">P</span>
                                            </c:when>
                                            <c:when test="${status == 'ABSENT'}">
                                                <span class="matrix-cell matrix-A" title="Day ${day}: Absent">A</span>
                                            </c:when>
                                            <c:when test="${status == 'ON_LEAVE' || status == 'EXCUSED'}">
                                                <span class="matrix-cell matrix-E" title="Day ${day}: On Leave">L</span>
                                            </c:when>
                                            <c:when test="${status == 'HALF_DAY'}">
                                                <span class="matrix-cell matrix-H" title="Day ${day}: Half Day">H</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="matrix-cell matrix-EMPTY">·</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>

                                <td style="font-weight: 600; color: var(--success);">${row.presentCount}</td>
                                <td style="font-weight: 600; color: var(--danger);">${row.absentCount}</td>
                                <td style="font-weight: 600; color: var(--primary);">${row.leaveCount}</td>
                                <td>
                                    <strong style="color: ${row.attendanceRate >= 80.0 ? 'var(--success)' : 'var(--danger)'}; font-size: 0.8125rem;">
                                        ${row.attendanceRate}%
                                    </strong>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty matrix}">
                            <tr>
                                <td colspan="${daysInMonth + 5}" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    No employee attendance matrix data found for ${monthName} ${selectedYear}.
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
