<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="sidebar">
    <div class="brand-box">
        <div class="brand-icon">
            <i class="fa-solid fa-clipboard-user"></i>
        </div>
        <div>
            <div class="brand-title">AttendX Pro</div>
            <div class="brand-subtitle">Attendance System</div>
        </div>
    </div>

    <ul class="sidebar-nav">
        <li class="nav-section-title">Core Operations</li>
        
        <li>
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-link-item ${pageContext.request.requestURI.endsWith('dashboard.jsp') || pageContext.request.requestURI.equals('/') ? 'active' : ''}">
                <i class="fa-solid fa-chart-pie"></i>
                <span>Dashboard</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/attendance/kiosk" class="nav-link-item ${pageContext.request.requestURI.contains('/kiosk') ? 'active' : ''}">
                <i class="fa-solid fa-barcode"></i>
                <span>Live Kiosk</span>
                <span class="badge-status badge-present" style="font-size: 0.625rem; padding: 0.15rem 0.4rem; margin-left: auto;">LIVE</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/departments" class="nav-link-item ${pageContext.request.requestURI.contains('/departments') ? 'active' : ''}">
                <i class="fa-solid fa-building"></i>
                <span>Departments</span>
            </a>
        </li>

        <li class="nav-section-title">Directory</li>

        <li>
            <a href="${pageContext.request.contextPath}/students" class="nav-link-item ${pageContext.request.requestURI.contains('/students') && !pageContext.request.requestURI.contains('/attendance') && !pageContext.request.requestURI.contains('/import') && !pageContext.request.requestURI.contains('/matrix') ? 'active' : ''}">
                <i class="fa-solid fa-graduation-cap"></i>
                <span>Students</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/employees" class="nav-link-item ${pageContext.request.requestURI.contains('/employees') && !pageContext.request.requestURI.contains('/attendance') && !pageContext.request.requestURI.contains('/import') && !pageContext.request.requestURI.contains('/matrix') ? 'active' : ''}">
                <i class="fa-solid fa-user-tie"></i>
                <span>Employees</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/import/students" class="nav-link-item ${pageContext.request.requestURI.contains('/import/students') ? 'active' : ''}">
                <i class="fa-solid fa-file-import"></i>
                <span>Import Students</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/import/employees" class="nav-link-item ${pageContext.request.requestURI.contains('/import/employees') ? 'active' : ''}">
                <i class="fa-solid fa-file-csv"></i>
                <span>Import Staff</span>
            </a>
        </li>

        <li class="nav-section-title">Attendance</li>

        <li>
            <a href="${pageContext.request.contextPath}/attendance/students/mark" class="nav-link-item ${pageContext.request.requestURI.contains('/attendance/students/mark') ? 'active' : ''}">
                <i class="fa-solid fa-user-check"></i>
                <span>Mark Students</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/attendance/employees/mark" class="nav-link-item ${pageContext.request.requestURI.contains('/attendance/employees/mark') ? 'active' : ''}">
                <i class="fa-solid fa-fingerprint"></i>
                <span>Mark Employees</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/attendance/students/logs" class="nav-link-item ${pageContext.request.requestURI.contains('/attendance/students/logs') ? 'active' : ''}">
                <i class="fa-solid fa-list-check"></i>
                <span>Student Logs</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/attendance/employees/logs" class="nav-link-item ${pageContext.request.requestURI.contains('/attendance/employees/logs') ? 'active' : ''}">
                <i class="fa-solid fa-clock-rotate-left"></i>
                <span>Employee Logs</span>
            </a>
        </li>

        <li class="nav-section-title">Leaves</li>

        <li>
            <a href="${pageContext.request.contextPath}/leaves" class="nav-link-item ${pageContext.request.requestURI.contains('/leaves') ? 'active' : ''}">
                <i class="fa-solid fa-umbrella-beach"></i>
                <span>Leave Management</span>
            </a>
        </li>

        <li class="nav-section-title">Reports & Analytics</li>

        <li>
            <a href="${pageContext.request.contextPath}/reports/defaulters" class="nav-link-item ${pageContext.request.requestURI.contains('/defaulters') ? 'active' : ''}">
                <i class="fa-solid fa-triangle-exclamation"></i>
                <span>Defaulters (<75%)</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/reports" class="nav-link-item ${pageContext.request.requestURI.contains('/reports') && !pageContext.request.requestURI.contains('/matrix') && !pageContext.request.requestURI.contains('/defaulters') ? 'active' : ''}">
                <i class="fa-solid fa-file-lines"></i>
                <span>Reports Hub</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/reports/students/matrix" class="nav-link-item ${pageContext.request.requestURI.contains('/reports/students/matrix') ? 'active' : ''}">
                <i class="fa-solid fa-table-cells"></i>
                <span>Student Matrix</span>
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/reports/employees/matrix" class="nav-link-item ${pageContext.request.requestURI.contains('/reports/employees/matrix') ? 'active' : ''}">
                <i class="fa-solid fa-calendar-days"></i>
                <span>Employee Matrix</span>
            </a>
        </li>
    </ul>

    <div style="padding-top: 0.75rem; margin-top: 0.75rem; border-top: 1px solid var(--border-color);">
        <a href="${pageContext.request.contextPath}/h2-console" target="_blank" class="nav-link-item" style="font-size: 0.8125rem;">
            <i class="fa-solid fa-database"></i>
            <span>H2 Database Console</span>
        </a>
    </div>
</aside>
