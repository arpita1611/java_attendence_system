<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Dashboard - AttendX Pro" />
<jsp:include page="common/header.jsp" />
<jsp:include page="common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="common/alerts.jsp" />

        <!-- Page Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Attendance Dashboard</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Overview of student and employee attendance across the institution</p>
            </div>
            <div style="display: flex; gap: 0.625rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/attendance/students/mark" class="btn-secondary-custom">
                    <i class="fa-solid fa-user-check"></i> Mark Attendance
                </a>
                <a href="${pageContext.request.contextPath}/attendance/kiosk" class="btn-primary-custom">
                    <i class="fa-solid fa-barcode"></i> Launch Kiosk
                </a>
            </div>
        </div>

        <!-- 1. Statistics Cards Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(210px, 1fr)); gap: 1rem; margin-bottom: 1.5rem;">
            
            <!-- Total Students Card -->
            <div class="stat-card">
                <div>
                    <span class="stat-label">Total Students</span>
                    <div class="stat-val">${stats.totalStudents}</div>
                    <span style="font-size: 0.8125rem; color: var(--success); font-weight: 500;">
                        <i class="fa-solid fa-arrow-trend-up"></i> ${stats.studentAttendanceRate}% Rate
                    </span>
                </div>
                <div class="stat-icon primary">
                    <i class="fa-solid fa-user-graduate"></i>
                </div>
            </div>

            <!-- Total Employees Card -->
            <div class="stat-card">
                <div>
                    <span class="stat-label">Total Employees</span>
                    <div class="stat-val">${stats.totalEmployees}</div>
                    <span style="font-size: 0.8125rem; color: var(--info); font-weight: 500;">
                        <i class="fa-solid fa-circle-check"></i> ${stats.employeeAttendanceRate}% Rate
                    </span>
                </div>
                <div class="stat-icon cyan">
                    <i class="fa-solid fa-user-tie"></i>
                </div>
            </div>

            <!-- Present Today Card -->
            <div class="stat-card">
                <div>
                    <span class="stat-label">Present Today</span>
                    <div class="stat-val" style="color: var(--success);">${stats.studentPresentToday}</div>
                    <span style="font-size: 0.8125rem; color: var(--text-muted);">Students marked in</span>
                </div>
                <div class="stat-icon emerald">
                    <i class="fa-solid fa-check"></i>
                </div>
            </div>

            <!-- Absent Today Card -->
            <div class="stat-card">
                <div>
                    <span class="stat-label">Absent Today</span>
                    <div class="stat-val" style="color: var(--danger);">${stats.studentAbsentToday}</div>
                    <span style="font-size: 0.8125rem; color: var(--text-muted);">Unexcused misses</span>
                </div>
                <div class="stat-icon rose">
                    <i class="fa-solid fa-xmark"></i>
                </div>
            </div>

            <!-- Low Attendance / Defaulters Card -->
            <a href="${pageContext.request.contextPath}/reports/defaulters" class="stat-card" style="text-decoration: none; border-color: var(--danger-border); background: var(--bg-card);">
                <div>
                    <span class="stat-label" style="color: var(--danger);">Defaulters (<75%)</span>
                    <div class="stat-val" style="color: var(--danger);">${defaultersCount}</div>
                    <span style="font-size: 0.8125rem; color: var(--danger); font-weight: 500;">
                        <i class="fa-solid fa-triangle-exclamation"></i> View Shortage
                    </span>
                </div>
                <div class="stat-icon rose">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                </div>
            </a>

        </div>

        <!-- 2. Attendance Analytics Trend Charts -->
        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1.25rem; margin-bottom: 1.5rem;">
            
            <!-- 7-Day Trend Chart -->
            <div class="glass-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <div>
                        <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.15rem;">Attendance Trend (Last 7 Days)</h2>
                        <span style="font-size: 0.78125rem; color: var(--text-muted);">Daily attendance rate percentages</span>
                    </div>
                </div>
                <div style="height: 270px;">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>

            <!-- Today's Attendance Distribution Doughnut -->
            <div class="glass-card">
                <div style="margin-bottom: 1rem;">
                    <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.15rem;">Today's Attendance Ratio</h2>
                    <span style="font-size: 0.78125rem; color: var(--text-muted);">Present vs Absent vs Late</span>
                </div>
                <div style="height: 200px; display: flex; align-items: center; justify-content: center;">
                    <canvas id="doughnutChart"></canvas>
                </div>
                <div style="display: flex; justify-content: space-around; margin-top: 1rem; border-top: 1px solid var(--border-color); padding-top: 0.75rem; font-size: 0.8125rem; color: var(--text-secondary);">
                    <div><span style="color: var(--success); font-weight: bold;">●</span> Present: <strong style="color: var(--text-primary);">${stats.studentPresentToday}</strong></div>
                    <div><span style="color: var(--danger); font-weight: bold;">●</span> Absent: <strong style="color: var(--text-primary);">${stats.studentAbsentToday}</strong></div>
                    <div><span style="color: var(--warning); font-weight: bold;">●</span> Late: <strong style="color: var(--text-primary);">${stats.studentLateToday}</strong></div>
                </div>
            </div>
        </div>

        <!-- 3. Quick Action Hub & Recent Attendance Stream -->
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.25rem;">
            
            <!-- Quick Actions Card -->
            <div class="glass-card">
                <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 1rem;">
                    Quick Actions
                </h2>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/attendance/kiosk" class="glass-card" style="text-decoration: none; padding: 1rem; text-align: center; border-radius: var(--radius-md);">
                        <i class="fa-solid fa-barcode" style="font-size: 1.5rem; color: var(--primary); margin-bottom: 0.35rem; display: block;"></i>
                        <strong style="color: var(--text-primary); font-size: 0.875rem; display: block;">Smart Kiosk</strong>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">RFID / ID Scanner</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/attendance/students/mark" class="glass-card" style="text-decoration: none; padding: 1rem; text-align: center; border-radius: var(--radius-md);">
                        <i class="fa-solid fa-user-check" style="font-size: 1.5rem; color: var(--success); margin-bottom: 0.35rem; display: block;"></i>
                        <strong style="color: var(--text-primary); font-size: 0.875rem; display: block;">Mark Students</strong>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">Roster Attendance</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/reports/defaulters" class="glass-card" style="text-decoration: none; padding: 1rem; text-align: center; border-radius: var(--radius-md);">
                        <i class="fa-solid fa-triangle-exclamation" style="font-size: 1.5rem; color: var(--danger); margin-bottom: 0.35rem; display: block;"></i>
                        <strong style="color: var(--text-primary); font-size: 0.875rem; display: block;">Defaulters List</strong>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">< 75% Audit</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/leaves" class="glass-card" style="text-decoration: none; padding: 1rem; text-align: center; border-radius: var(--radius-md);">
                        <i class="fa-solid fa-umbrella-beach" style="font-size: 1.5rem; color: var(--warning); margin-bottom: 0.35rem; display: block;"></i>
                        <strong style="color: var(--text-primary); font-size: 0.875rem; display: block;">Leave Requests</strong>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">${pendingLeavesCount} Pending</span>
                    </a>
                </div>
            </div>

            <!-- Today's Live Attendance Feed -->
            <div class="glass-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <div>
                        <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin: 0;">Today's Recent Activity</h2>
                        <span style="font-size: 0.78125rem; color: var(--text-muted);">Date: ${today}</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/attendance/students/logs" class="btn-secondary-custom" style="padding: 0.3rem 0.65rem; font-size: 0.75rem;">View All</a>
                </div>

                <div style="max-height: 250px; overflow-y: auto;">
                    <c:choose>
                        <c:when test="${not empty todayStudentLogs}">
                            <table class="custom-table" style="font-size: 0.8125rem;">
                                <tbody>
                                    <c:forEach var="log" items="${todayStudentLogs}" end="4">
                                        <tr>
                                            <td style="font-family: monospace; font-weight: 600; color: var(--primary);">${log.student.rollNumber}</td>
                                            <td style="font-weight: 500;">${log.student.fullName}</td>
                                            <td>
                                                <span class="badge-status badge-${log.status.name().toLowerCase()}">
                                                    ${log.status.displayName}
                                                </span>
                                            </td>
                                            <td style="color: var(--text-muted); font-size: 0.75rem;">${log.student.department.code}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; padding: 2.5rem 1rem; color: var(--text-muted);">
                                <i class="fa-regular fa-calendar-xmark" style="font-size: 2rem; margin-bottom: 0.5rem; display: block; color: var(--text-light);"></i>
                                No attendance recorded yet today.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>

    </div>
</div>

<!-- Chart.js Render Script -->
<script>
document.addEventListener('DOMContentLoaded', () => {
    // 1. Trend Line Chart
    const trendCtx = document.getElementById('trendChart').getContext('2d');
    const chartLabels = [
        <c:forEach var="label" items="${stats.chartLabels}" varStatus="loop">
            "${label}"${!loop.last ? ',' : ''}
        </c:forEach>
    ];
    const studentData = [
        <c:forEach var="rate" items="${stats.studentTrendRates}" varStatus="loop">
            ${rate}${!loop.last ? ',' : ''}
        </c:forEach>
    ];
    const employeeData = [
        <c:forEach var="rate" items="${stats.employeeTrendRates}" varStatus="loop">
            ${rate}${!loop.last ? ',' : ''}
        </c:forEach>
    ];

    new Chart(trendCtx, {
        type: 'line',
        data: {
            labels: chartLabels,
            datasets: [
                {
                    label: 'Students %',
                    data: studentData,
                    borderColor: '#2563EB',
                    backgroundColor: 'rgba(37, 99, 235, 0.08)',
                    fill: true,
                    tension: 0.3,
                    borderWidth: 2,
                    pointRadius: 3,
                    pointBackgroundColor: '#2563EB'
                },
                {
                    label: 'Employees %',
                    data: employeeData,
                    borderColor: '#0D9488',
                    backgroundColor: 'rgba(13, 148, 136, 0.08)',
                    fill: true,
                    tension: 0.3,
                    borderWidth: 2,
                    pointRadius: 3,
                    pointBackgroundColor: '#0D9488'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                    labels: { color: '#4B5563', font: { family: 'Inter', size: 12 }, boxWidth: 12 }
                }
            },
            scales: {
                y: {
                    min: 0,
                    max: 100,
                    grid: { color: '#F1F5F9' },
                    ticks: { color: '#6B7280', callback: val => val + '%' }
                },
                x: {
                    grid: { color: '#F8FAFC' },
                    ticks: { color: '#6B7280' }
                }
            }
        }
    });

    // 2. Ratio Doughnut Chart
    const doughnutCtx = document.getElementById('doughnutChart').getContext('2d');
    const presentCount = ${stats.studentPresentToday};
    const absentCount = ${stats.studentAbsentToday};
    const lateCount = ${stats.studentLateToday};

    new Chart(doughnutCtx, {
        type: 'doughnut',
        data: {
            labels: ['Present', 'Absent', 'Late'],
            datasets: [{
                data: [presentCount > 0 ? presentCount : 1, absentCount, lateCount],
                backgroundColor: ['#16A34A', '#DC2626', '#D97706'],
                borderWidth: 2,
                borderColor: '#FFFFFF',
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            cutout: '72%'
        }
    });
});
</script>

<jsp:include page="common/footer.jsp" />
