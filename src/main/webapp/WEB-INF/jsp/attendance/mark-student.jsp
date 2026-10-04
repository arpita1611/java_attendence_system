<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Mark Student Attendance - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Mark Student Attendance</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Select date and department to load classroom student roster</p>
            </div>
            <div style="display: flex; gap: 0.625rem;">
                <a href="${pageContext.request.contextPath}/attendance/students/logs" class="btn-secondary-custom">
                    <i class="fa-solid fa-list-check"></i> View Daily Logs
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Criteria Form (GET) -->
            <form action="${pageContext.request.contextPath}/attendance/students/mark" method="get" style="display: flex; gap: 0.75rem; margin-bottom: 1.5rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
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
                <div style="flex: 1.5; min-width: 150px;">
                    <label class="form-label-custom">Semester</label>
                    <input type="text" name="semester" value="${selectedSemester}" placeholder="e.g. Semester 4" class="form-control-custom" />
                </div>
                <div>
                    <button type="submit" class="btn-primary-custom" style="padding: 0.55rem 1.15rem;">
                        <i class="fa-solid fa-arrows-rotate"></i> Load Roster
                    </button>
                </div>
            </form>

            <!-- Bulk Submission Form (POST) -->
            <c:choose>
                <c:when test="${not empty form.items}">
                    <!-- Quick Toggle Toolbar -->
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; flex-wrap: wrap; gap: 0.75rem; padding: 0.75rem 1rem; background: var(--bg-subtle); border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                        <div style="font-size: 0.875rem; color: var(--text-primary);">
                            <strong>${form.items.size()}</strong> students on <strong>${form.attendanceDate}</strong>
                        </div>
                        <div style="display: flex; gap: 0.5rem; align-items: center; flex-wrap: wrap;">
                            <span style="font-size: 0.8125rem; color: var(--text-muted); margin-right: 0.25rem;">Quick Set:</span>
                            <button type="button" onclick="markAllStatus('PRESENT')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--success);">
                                <i class="fa-solid fa-check-double"></i> All Present
                            </button>
                            <button type="button" onclick="markAllStatus('ABSENT')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--danger);">
                                <i class="fa-solid fa-xmark"></i> All Absent
                            </button>
                            <button type="button" onclick="markAllStatus('LATE')" class="btn-secondary-custom" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem; color: var(--warning);">
                                <i class="fa-solid fa-clock"></i> All Late
                            </button>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/attendance/students/mark" method="post">
                        <input type="hidden" name="attendanceDate" value="${form.attendanceDate}" />
                        <input type="hidden" name="departmentId" value="${form.departmentId}" />
                        <input type="hidden" name="semester" value="${form.semester}" />

                        <div style="overflow-x: auto; margin-bottom: 1.5rem;">
                            <table class="custom-table">
                                <thead>
                                    <tr>
                                        <th>Roll Number</th>
                                        <th>Student Name</th>
                                        <th>Semester / Sec</th>
                                        <th style="width: 190px;">Status</th>
                                        <th>Remarks (Optional)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${form.items}" varStatus="status">
                                        <input type="hidden" name="items[${status.index}].studentId" value="${item.studentId}" />
                                        <input type="hidden" name="items[${status.index}].rollNumber" value="${item.rollNumber}" />
                                        <input type="hidden" name="items[${status.index}].studentName" value="${item.studentName}" />
                                        <input type="hidden" name="items[${status.index}].semester" value="${item.semester}" />
                                        <input type="hidden" name="items[${status.index}].section" value="${item.section}" />

                                        <tr>
                                            <td>
                                                <span style="font-family: monospace; font-weight: 600; color: var(--primary);">${item.rollNumber}</span>
                                            </td>
                                            <td><strong style="color: var(--text-primary);">${item.studentName}</strong></td>
                                            <td>
                                                <span style="font-size: 0.8125rem; color: var(--text-secondary); background: var(--bg-subtle); padding: 0.2rem 0.5rem; border-radius: var(--radius-xs); border: 1px solid var(--border-color);">
                                                    ${item.semester} • ${item.section}
                                                </span>
                                            </td>
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
                                                <input type="text" name="items[${status.index}].remarks" value="${item.remarks}" placeholder="e.g. excused, medical..." class="form-control-custom" />
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
                        <i class="fa-solid fa-users-viewfinder" style="font-size: 2.5rem; margin-bottom: 0.75rem; display: block; color: var(--text-light);"></i>
                        <h3 style="color: var(--text-primary); font-size: 1.1rem; margin-bottom: 0.25rem;">No Students Found</h3>
                        <p style="font-size: 0.875rem;">Select a valid department / semester above or add students to the directory.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
