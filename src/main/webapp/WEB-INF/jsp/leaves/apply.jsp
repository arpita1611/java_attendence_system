<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Apply for Leave - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <div style="max-width: 720px; margin: 0 auto;">
            <div class="glass-card">
                
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
                    <div>
                        <h1 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.2rem;">Submit Leave Application</h1>
                        <span style="font-size: 0.8125rem; color: var(--text-muted);">Apply on behalf of student or faculty member</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/leaves" class="btn-secondary-custom">
                        <i class="fa-solid fa-arrow-left"></i> Back to Leaves
                    </a>
                </div>

                <!-- Type Selector Tabs -->
                <div style="display: flex; gap: 0.5rem; margin-bottom: 1.5rem; background: var(--bg-subtle); padding: 0.35rem; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                    <a href="${pageContext.request.contextPath}/leaves/apply?type=STUDENT" class="${type == 'STUDENT' ? 'btn-primary-custom' : 'btn-secondary-custom'}" style="flex: 1; justify-content: center; ${type != 'STUDENT' ? 'border: none; background: transparent;' : ''}">
                        <i class="fa-solid fa-graduation-cap"></i> Student Leave Form
                    </a>
                    <a href="${pageContext.request.contextPath}/leaves/apply?type=EMPLOYEE" class="${type == 'EMPLOYEE' ? 'btn-primary-custom' : 'btn-secondary-custom'}" style="flex: 1; justify-content: center; ${type != 'EMPLOYEE' ? 'border: none; background: transparent;' : ''}">
                        <i class="fa-solid fa-user-tie"></i> Employee Leave Form
                    </a>
                </div>

                <c:choose>
                    <c:when test="${type == 'STUDENT'}">
                        <!-- Student Leave Form -->
                        <form action="${pageContext.request.contextPath}/leaves/apply/student" method="post">
                            <div style="margin-bottom: 1rem;">
                                <label class="form-label-custom">Select Student *</label>
                                <select name="studentId" class="form-select-custom" required>
                                    <option value="">-- Choose Student --</option>
                                    <c:forEach var="s" items="${students}">
                                        <option value="${s.id}">${s.rollNumber} - ${s.fullName} (${s.department.code}, ${s.semester})</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                                <div>
                                    <label class="form-label-custom">Leave Start Date *</label>
                                    <input type="date" name="startDate" value="${today}" class="form-control-custom" required />
                                </div>
                                <div>
                                    <label class="form-label-custom">Leave End Date *</label>
                                    <input type="date" name="endDate" value="${today}" class="form-control-custom" required />
                                </div>
                            </div>

                            <div style="margin-bottom: 1rem;">
                                <label class="form-label-custom">Leave Category *</label>
                                <select name="leaveType" class="form-select-custom" required>
                                    <c:forEach var="lt" items="${leaveTypes}">
                                        <option value="${lt}">${lt.displayName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div style="margin-bottom: 1.5rem;">
                                <label class="form-label-custom">Reason / Purpose of Leave *</label>
                                <textarea name="reason" class="form-control-custom" rows="3" placeholder="Provide details (e.g. medical illness, family emergency, academic competition)..." required></textarea>
                            </div>

                            <div style="display: flex; justify-content: flex-end; gap: 0.75rem; border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                                <a href="${pageContext.request.contextPath}/leaves" class="btn-secondary-custom">Cancel</a>
                                <button type="submit" class="btn-primary-custom" style="padding: 0.6rem 1.5rem;">
                                    <i class="fa-solid fa-paper-plane"></i> Submit Leave Request
                                </button>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <!-- Employee Leave Form -->
                        <form action="${pageContext.request.contextPath}/leaves/apply/employee" method="post">
                            <div style="margin-bottom: 1rem;">
                                <label class="form-label-custom">Select Staff Member *</label>
                                <select name="employeeId" class="form-select-custom" required>
                                    <option value="">-- Choose Employee --</option>
                                    <c:forEach var="e" items="${employees}">
                                        <option value="${e.id}">${e.employeeCode} - ${e.fullName} (${e.designation}, ${e.department.code})</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                                <div>
                                    <label class="form-label-custom">Leave Start Date *</label>
                                    <input type="date" name="startDate" value="${today}" class="form-control-custom" required />
                                </div>
                                <div>
                                    <label class="form-label-custom">Leave End Date *</label>
                                    <input type="date" name="endDate" value="${today}" class="form-control-custom" required />
                                </div>
                            </div>

                            <div style="margin-bottom: 1rem;">
                                <label class="form-label-custom">Leave Category *</label>
                                <select name="leaveType" class="form-select-custom" required>
                                    <c:forEach var="lt" items="${leaveTypes}">
                                        <option value="${lt}">${lt.displayName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div style="margin-bottom: 1.5rem;">
                                <label class="form-label-custom">Reason / Justification *</label>
                                <textarea name="reason" class="form-control-custom" rows="3" placeholder="State reason (e.g. Casual Leave, Conference attendance, Medical)..." required></textarea>
                            </div>

                            <div style="display: flex; justify-content: flex-end; gap: 0.75rem; border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                                <a href="${pageContext.request.contextPath}/leaves" class="btn-secondary-custom">Cancel</a>
                                <button type="submit" class="btn-primary-custom" style="padding: 0.6rem 1.5rem;">
                                    <i class="fa-solid fa-paper-plane"></i> Submit Leave Request
                                </button>
                            </div>
                        </form>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
