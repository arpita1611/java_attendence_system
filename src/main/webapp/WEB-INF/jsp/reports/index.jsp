<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Reports Hub - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="margin-bottom: 1.5rem;">
            <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Reports & Analytics Hub</h1>
            <p style="color: var(--text-muted); font-size: 0.875rem; margin: 0;">Generate customized attendance statements, monthly calendar matrices, shortage advisories, and exportable CSV sheets.</p>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 1.25rem;">
            
            <!-- Attendance Shortage & Defaulters Card -->
            <div class="glass-card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="width: 44px; height: 44px; border-radius: var(--radius-md); background: var(--danger-light); border: 1px solid var(--danger-border); display: flex; align-items: center; justify-content: center; font-size: 1.25rem; color: var(--danger); margin-bottom: 1rem;">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                    </div>
                    <h2 style="font-size: 1.1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">Attendance Defaulters (&lt; 75%)</h2>
                    <p style="color: var(--text-secondary); font-size: 0.8125rem; line-height: 1.5; margin-bottom: 1.25rem;">
                        Audit student attendance shortages, calculate recovery classes needed for examination eligibility, and print official warning letters.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="${pageContext.request.contextPath}/reports/defaulters" class="btn-primary-custom" style="flex: 1; justify-content: center;">
                        <i class="fa-solid fa-triangle-exclamation"></i> Open Audit
                    </a>
                    <a href="${pageContext.request.contextPath}/reports/defaulters/export/csv" class="btn-secondary-custom" title="Export CSV">
                        <i class="fa-solid fa-file-csv"></i> CSV
                    </a>
                </div>
            </div>

            <!-- Student Tabular Report Card -->
            <div class="glass-card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="width: 44px; height: 44px; border-radius: var(--radius-md); background: var(--primary-light); border: 1px solid var(--primary-border); display: flex; align-items: center; justify-content: center; font-size: 1.25rem; color: var(--primary); margin-bottom: 1rem;">
                        <i class="fa-solid fa-graduation-cap"></i>
                    </div>
                    <h2 style="font-size: 1.1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">Student Attendance Statement</h2>
                    <p style="color: var(--text-secondary); font-size: 0.8125rem; line-height: 1.5; margin-bottom: 1.25rem;">
                        Filter student attendance records across customized date ranges, department units, semester levels, and presence status.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="${pageContext.request.contextPath}/reports/students" class="btn-primary-custom" style="flex: 1; justify-content: center;">
                        <i class="fa-solid fa-table"></i> Open Report
                    </a>
                    <a href="${pageContext.request.contextPath}/reports/export/students/csv" class="btn-secondary-custom" title="Export CSV">
                        <i class="fa-solid fa-file-csv"></i> CSV
                    </a>
                </div>
            </div>

            <!-- Employee Tabular Report Card -->
            <div class="glass-card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="width: 44px; height: 44px; border-radius: var(--radius-md); background: var(--info-light); border: 1px solid var(--info-border); display: flex; align-items: center; justify-content: center; font-size: 1.25rem; color: var(--info); margin-bottom: 1rem;">
                        <i class="fa-solid fa-user-tie"></i>
                    </div>
                    <h2 style="font-size: 1.1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">Employee Attendance Report</h2>
                    <p style="color: var(--text-secondary); font-size: 0.8125rem; line-height: 1.5; margin-bottom: 1.25rem;">
                        Detailed logs of employee check-in times, check-out times, leave records, and department-level summaries.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="${pageContext.request.contextPath}/reports/employees" class="btn-primary-custom" style="flex: 1; justify-content: center;">
                        <i class="fa-solid fa-table"></i> Open Report
                    </a>
                    <a href="${pageContext.request.contextPath}/reports/export/employees/csv" class="btn-secondary-custom" title="Export CSV">
                        <i class="fa-solid fa-file-csv"></i> CSV
                    </a>
                </div>
            </div>

            <!-- Student Monthly Matrix Card -->
            <div class="glass-card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="width: 44px; height: 44px; border-radius: var(--radius-md); background: var(--success-light); border: 1px solid var(--success-border); display: flex; align-items: center; justify-content: center; font-size: 1.25rem; color: var(--success); margin-bottom: 1rem;">
                        <i class="fa-solid fa-table-cells"></i>
                    </div>
                    <h2 style="font-size: 1.1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">Student Monthly Matrix</h2>
                    <p style="color: var(--text-secondary); font-size: 0.8125rem; line-height: 1.5; margin-bottom: 1.25rem;">
                        1..31 monthly calendar grid displaying color-coded daily presence per student with calculated monthly attendance % rates.
                    </p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/reports/students/matrix" class="btn-primary-custom" style="width: 100%; justify-content: center;">
                        <i class="fa-solid fa-calendar-days"></i> Open Monthly Matrix
                    </a>
                </div>
            </div>

            <!-- Employee Monthly Matrix Card -->
            <div class="glass-card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="width: 44px; height: 44px; border-radius: var(--radius-md); background: var(--purple-light); border: 1px solid var(--purple-border); display: flex; align-items: center; justify-content: center; font-size: 1.25rem; color: var(--purple); margin-bottom: 1rem;">
                        <i class="fa-solid fa-calendar-week"></i>
                    </div>
                    <h2 style="font-size: 1.1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">Employee Monthly Matrix</h2>
                    <p style="color: var(--text-secondary); font-size: 0.8125rem; line-height: 1.5; margin-bottom: 1.25rem;">
                        Monthly work-schedule sheet showing daily attendance, half-days, leaves, and total monthly percentage for staff.
                    </p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/reports/employees/matrix" class="btn-primary-custom" style="width: 100%; justify-content: center;">
                        <i class="fa-solid fa-calendar-days"></i> Open Monthly Matrix
                    </a>
                </div>
            </div>

        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
