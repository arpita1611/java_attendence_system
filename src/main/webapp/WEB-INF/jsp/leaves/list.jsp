<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Leave Management - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header Row -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Leave Management</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Review, approve, or reject student and employee leave applications</p>
            </div>
            <div style="display: flex; gap: 0.625rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/leaves/apply?type=STUDENT" class="btn-secondary-custom">
                    <i class="fa-solid fa-graduation-cap"></i> Student Leave
                </a>
                <a href="${pageContext.request.contextPath}/leaves/apply?type=EMPLOYEE" class="btn-primary-custom">
                    <i class="fa-solid fa-user-tie"></i> Employee Leave
                </a>
            </div>
        </div>

        <div class="glass-card">
            <!-- Filter Criteria -->
            <form action="${pageContext.request.contextPath}/leaves" method="get" style="display: flex; gap: 0.75rem; margin-bottom: 1.25rem; flex-wrap: wrap; background: var(--bg-subtle); padding: 0.875rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); align-items: flex-end;">
                <div style="flex: 1.5; min-width: 170px;">
                    <label class="form-label-custom">Applicant Type</label>
                    <select name="applicantType" class="form-select-custom">
                        <option value="">All (Students & Staff)</option>
                        <option value="STUDENT" ${selectedApplicantType == 'STUDENT' ? 'selected' : ''}>Students Only</option>
                        <option value="EMPLOYEE" ${selectedApplicantType == 'EMPLOYEE' ? 'selected' : ''}>Employees Only</option>
                    </select>
                </div>
                <div style="flex: 1.5; min-width: 170px;">
                    <label class="form-label-custom">Status</label>
                    <select name="status" class="form-select-custom">
                        <option value="">All Statuses</option>
                        <c:forEach var="st" items="${statuses}">
                            <option value="${st}" ${selectedStatus == st ? 'selected' : ''}>${st.displayName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <button type="submit" class="btn-secondary-custom" style="padding: 0.55rem 1rem;">
                        <i class="fa-solid fa-filter"></i> Filter
                    </button>
                </div>
                <c:if test="${not empty selectedApplicantType || not empty selectedStatus}">
                    <a href="${pageContext.request.contextPath}/leaves" class="btn-icon" title="Reset Filters">
                        <i class="fa-solid fa-rotate-left"></i>
                    </a>
                </c:if>
            </form>

            <!-- Table of Leaves -->
            <div style="overflow-x: auto;">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Applicant</th>
                            <th>ID / Roll</th>
                            <th>Category</th>
                            <th>Dates</th>
                            <th>Duration</th>
                            <th>Reason</th>
                            <th>Status</th>
                            <th style="text-align: right;">Decision</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="l" items="${leaves}">
                            <tr>
                                <td>
                                    <strong style="color: var(--text-primary); font-size: 0.875rem;">${l.applicantName}</strong>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <span style="background: var(--bg-subtle); padding: 0.1rem 0.35rem; border-radius: 3px; font-weight: 500;">
                                            ${l.applicantType}
                                        </span>
                                        ${l.departmentName}
                                    </div>
                                </td>
                                <td>
                                    <span style="font-family: monospace; font-weight: 600; color: ${l.applicantType == 'STUDENT' ? 'var(--primary)' : 'var(--info)'};">
                                        ${l.applicantIdentifier}
                                    </span>
                                </td>
                                <td>
                                    <span style="font-size: 0.8125rem; color: var(--text-secondary);">
                                        ${l.leaveType.displayName}
                                    </span>
                                </td>
                                <td>
                                    <div style="font-size: 0.8125rem; color: var(--text-primary); font-weight: 500;">
                                        ${l.startDate} to ${l.endDate}
                                    </div>
                                    <div style="font-size: 0.7rem; color: var(--text-muted);">Applied: ${l.appliedAt.toLocalDate()}</div>
                                </td>
                                <td>
                                    <span style="font-size: 0.75rem; color: var(--text-secondary); background: var(--bg-subtle); padding: 0.2rem 0.45rem; border-radius: var(--radius-xs); border: 1px solid var(--border-color);">
                                        ${l.endDate.dayOfYear - l.startDate.dayOfYear + 1} Day(s)
                                    </span>
                                </td>
                                <td style="max-width: 220px;">
                                    <span style="font-size: 0.8125rem; color: var(--text-secondary); display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;" title="${l.reason}">
                                        ${not empty l.reason ? l.reason : '—'}
                                    </span>
                                    <c:if test="${not empty l.adminRemarks}">
                                        <div style="font-size: 0.72rem; color: var(--warning); margin-top: 0.15rem;">
                                            <em>Admin: ${l.adminRemarks}</em>
                                        </div>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${l.status == 'APPROVED'}">
                                            <span class="badge-status badge-present"><i class="fa-solid fa-check"></i> Approved</span>
                                        </c:when>
                                        <c:when test="${l.status == 'REJECTED'}">
                                            <span class="badge-status badge-absent"><i class="fa-solid fa-xmark"></i> Rejected</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-late"><i class="fa-solid fa-hourglass-half"></i> Pending</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="text-align: right;">
                                    <c:choose>
                                        <c:when test="${l.status == 'PENDING'}">
                                            <div style="display: inline-flex; gap: 0.35rem;">
                                                <form action="${pageContext.request.contextPath}/leaves/approve" method="post" style="display: inline;">
                                                    <input type="hidden" name="id" value="${l.id}" />
                                                    <input type="hidden" name="adminRemarks" value="Approved by Admin" />
                                                    <button type="submit" class="btn-icon" title="Approve Leave" style="background: var(--success-light); color: var(--success); border-color: var(--success-border);">
                                                        <i class="fa-solid fa-check"></i>
                                                    </button>
                                                </form>
                                                <form action="${pageContext.request.contextPath}/leaves/reject" method="post" style="display: inline;">
                                                    <input type="hidden" name="id" value="${l.id}" />
                                                    <input type="hidden" name="adminRemarks" value="Rejected" />
                                                    <button type="submit" class="btn-icon" title="Reject Leave" style="background: var(--danger-light); color: var(--danger); border-color: var(--danger-border);">
                                                        <i class="fa-solid fa-xmark"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/leaves/delete/${l.id}" onclick="return confirm('Delete this leave record?');" class="btn-icon" title="Delete Log" style="color: var(--text-muted);">
                                                <i class="fa-solid fa-trash"></i>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty leaves}">
                            <tr>
                                <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 3rem 1rem;">
                                    <i class="fa-solid fa-umbrella-beach" style="font-size: 2rem; display: block; margin-bottom: 0.5rem; color: var(--text-light);"></i>
                                    No leave requests found.
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
