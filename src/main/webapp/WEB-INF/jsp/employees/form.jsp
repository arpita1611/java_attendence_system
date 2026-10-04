<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${pageTitle != null ? pageTitle : 'Employee Form - AttendX Pro'}" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <div style="max-width: 760px; margin: 0 auto;">
            <div class="glass-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
                    <div>
                        <h1 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.2rem;">${pageTitle}</h1>
                        <span style="font-size: 0.8125rem; color: var(--text-muted);">Enter employee personal and employment details</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/employees" class="btn-secondary-custom">
                        <i class="fa-solid fa-arrow-left"></i> Back to List
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/employees/save" method="post">
                    <input type="hidden" name="id" value="${employee.id}" />

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">Employee Code / ID *</label>
                            <input type="text" name="employeeCode" value="${employee.employeeCode}" class="form-control-custom" placeholder="e.g. EMP-105" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Department *</label>
                            <select name="department.id" class="form-select-custom" required>
                                <option value="">Select Department</option>
                                <c:forEach var="dept" items="${departments}">
                                    <option value="${dept.id}" ${employee.department != null && employee.department.id == dept.id ? 'selected' : ''}>
                                        ${dept.name} (${dept.code})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">First Name *</label>
                            <input type="text" name="firstName" value="${employee.firstName}" class="form-control-custom" placeholder="e.g. Michael" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Last Name *</label>
                            <input type="text" name="lastName" value="${employee.lastName}" class="form-control-custom" placeholder="e.g. Scott" required />
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">Email Address</label>
                            <input type="email" name="email" value="${employee.email}" class="form-control-custom" placeholder="e.g. michael.s@company.org" />
                        </div>
                        <div>
                            <label class="form-label-custom">Phone Number</label>
                            <input type="text" name="phone" value="${employee.phone}" class="form-control-custom" placeholder="e.g. +91 9820011223" />
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem; margin-bottom: 1.25rem;">
                        <div>
                            <label class="form-label-custom">Designation *</label>
                            <input type="text" name="designation" value="${employee.designation}" class="form-control-custom" placeholder="e.g. Professor & HOD" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Employment Type</label>
                            <select name="employmentType" class="form-select-custom">
                                <option value="FULL_TIME" ${employee.employmentType == 'FULL_TIME' ? 'selected' : ''}>Full Time</option>
                                <option value="PART_TIME" ${employee.employmentType == 'PART_TIME' ? 'selected' : ''}>Part Time</option>
                                <option value="CONTRACT" ${employee.employmentType == 'CONTRACT' ? 'selected' : ''}>Contractual</option>
                            </select>
                        </div>
                        <div>
                            <label class="form-label-custom">Gender</label>
                            <select name="gender" class="form-select-custom">
                                <option value="MALE" ${employee.gender == 'MALE' ? 'selected' : ''}>Male</option>
                                <option value="FEMALE" ${employee.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
                                <option value="OTHER" ${employee.gender == 'OTHER' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>
                    </div>

                    <div style="margin-bottom: 1.75rem;">
                        <label class="form-label-custom">Account Status</label>
                        <div style="display: flex; gap: 1.5rem; margin-top: 0.35rem;">
                            <label style="display: flex; align-items: center; gap: 0.45rem; color: var(--text-primary); cursor: pointer; font-size: 0.875rem;">
                                <input type="radio" name="active" value="true" ${employee.active || employee.id == null ? 'checked' : ''}> Active
                            </label>
                            <label style="display: flex; align-items: center; gap: 0.45rem; color: var(--text-muted); cursor: pointer; font-size: 0.875rem;">
                                <input type="radio" name="active" value="false" ${!employee.active && employee.id != null ? 'checked' : ''}> Inactive
                            </label>
                        </div>
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 0.75rem; border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                        <a href="${pageContext.request.contextPath}/employees" class="btn-secondary-custom">Cancel</a>
                        <button type="submit" class="btn-primary-custom">
                            <i class="fa-solid fa-floppy-disk"></i> Save Employee Record
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
