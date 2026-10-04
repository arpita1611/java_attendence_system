<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Mark Employee Attendance - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Mark Employee Attendance</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Record daily presence, check-in time, and check-out time for staff</p>
            </div>
            <div style="display: flex; gap: 0.625rem;">
                <a href="${pageContext.request.contextPath}/attendance/employees/logs" class="btn-secondary-custom">
                    <i class="fa-solid fa-clock-rotate-left"></i> View Daily Logs
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Criteria Form (GET) -->
            <form action="${pageContext.request.contextPath}/attendance/employees/mark" method="get" style="display: flex; gap: 0.75rem; margin-bottom: 1.5rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
                <div style="flex: 1.5; min-width: 170px;">
                    <label class="form-label-custom">Attendance Date</label>
                    <input type="date" name="date" value="${attendanceDate}" class="form-control-custom" required />
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
                <div>
                    <button type="submit" class="btn-primary-custom" style="padding: 0.55rem 1.15rem;">
                        <i class="fa-solid fa-arrows-rotate"></i> Load Staff Roster
                    </button>
                </div>
            </form>

            <!-- Bulk Submission Form (POST) -->
            <c:choose>
                <c:when test="${not empty form.items}">
                    <!-- Quick Toggle Toolbar -->
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; flex-wrap: wrap; gap: 0.75rem; padding: 0.75rem 1rem; background: var(--bg-subtle); border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                        <div style="font-size: 0.875rem; color: var(--text-primary);">
                            <strong>${form.items.size()}</strong> employees on <strong>${form.attendanceDate}</strong>
                        </div>
                        <div style="display: flex; gap: 0.5rem; align-items: center; flex-wrap: wrap;">
                            <span style="font-size: 0.8125rem; color: var(--text-muted); margin-right: 0.25rem;">Quick Actions:</span>
                            <button type="button" onclick="markAllStatus('PRESENT')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--success);">
                                <i class="fa-solid fa-check-double"></i> All Present
                            </button>
                            <button type="button" onclick="markAllStatus('ON_LEAVE')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--primary);">
                                <i class="fa-solid fa-umbrella-beach"></i> All Leave
                            </button>
                            <button type="button" onclick="setAllTimes('09:00', '17:30')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--info);">
                                <i class="fa-solid fa-business-time"></i> Standard Shift (9-5:30)
                            </button>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/attendance/employees/mark" method="post">
                        <input type="hidden" name="attendanceDate" value="${form.attendanceDate}" />
                        <input type="hidden" name="departmentId" value="${form.departmentId}" />

                        <div style="overflow-x: auto; margin-bottom: 1.5rem;">
                            <table class="custom-table">
                                <thead>
                                    <tr>
                                        <th>EMP Code</th>
                                        <th>Employee Name</th>
                                        <th>Designation</th>
                                        <th style="width: 170px;">Status</th>
                                        <th style="width: 140px;">Check In</th>
                                        <th style="width: 140px;">Check Out</th>
                                        <th>Remarks</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${form.items}" varStatus="status">
                                        <input type="hidden" name="items[${status.index}].employeeId" value="${item.employeeId}" />
                                        <input type="hidden" name="items[${status.index}].employeeCode" value="${item.employeeCode}" />
                                        <input type="hidden" name="items[${status.index}].employeeName" value="${item.employeeName}" />
                                        <input type="hidden" name="items[${status.index}].designation" value="${item.designation}" />

                                        <tr>
                                            <td>
                                                <span style="font-family: monospace; font-weight: 600; color: var(--info);">${item.employeeCode}</span>
                                            </td>
                                            <td><strong style="color: var(--text-primary);">${item.employeeName}</strong></td>
                                            <td style="color: var(--text-secondary); font-size: 0.8125rem;">${item.designation}</td>
                                            <td>
                                                <select name="items[${status.index}].status" class="form-select-custom attendance-status-select" style="font-weight: 500;">
                                                    <c:forEach var="st" items="${statuses}">
                                                        <option value="${st}" ${st == item.status ? 'selected' : ''}>
                                                            ${st.displayName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="time" name="items[${status.index}].checkInTime" value="${item.checkInTime}" class="form-control-custom employee-checkin-time" />
                                            </td>
                                            <td>
                                                <input type="time" name="items[${status.index}].checkOutTime" value="${item.checkOutTime}" class="form-control-custom employee-checkout-time" />
                                            </td>
                                            <td>
                                                <input type="text" name="items[${status.index}].remarks" value="${item.remarks}" placeholder="e.g. half-day..." class="form-control-custom" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div style="display: flex; justify-content: flex-end; gap: 0.75rem; border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                            <button type="submit" class="btn-primary-custom" style="padding: 0.65rem 1.75rem; font-size: 0.9375rem;">
                                <i class="fa-solid fa-floppy-disk"></i> Save Attendance
                            </button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 3rem 1rem; color: var(--text-muted);">
                        <i class="fa-solid fa-user-slash" style="font-size: 2.5rem; margin-bottom: 0.75rem; display: block; color: var(--text-light);"></i>
                        <h3 style="color: var(--text-primary); font-size: 1.1rem; margin-bottom: 0.25rem;">No Employees Found</h3>
                        <p style="font-size: 0.875rem;">Select a valid department above or add employees to the system.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
