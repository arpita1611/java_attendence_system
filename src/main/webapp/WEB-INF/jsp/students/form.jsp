<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${pageTitle != null ? pageTitle : 'Student Form - AttendX Pro'}" />
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
                        <span style="font-size: 0.8125rem; color: var(--text-muted);">Enter student academic and personal information</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/students" class="btn-secondary-custom">
                        <i class="fa-solid fa-arrow-left"></i> Back to List
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/students/save" method="post">
                    <input type="hidden" name="id" value="${student.id}" />

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">Roll Number / Registration ID *</label>
                            <input type="text" name="rollNumber" value="${student.rollNumber}" class="form-control-custom" placeholder="e.g. CSE-2024-101" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Department *</label>
                            <select name="department.id" class="form-select-custom" required>
                                <option value="">Select Department</option>
                                <c:forEach var="dept" items="${departments}">
                                    <option value="${dept.id}" ${student.department != null && student.department.id == dept.id ? 'selected' : ''}>
                                        ${dept.name} (${dept.code})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">First Name *</label>
                            <input type="text" name="firstName" value="${student.firstName}" class="form-control-custom" placeholder="e.g. John" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Last Name *</label>
                            <input type="text" name="lastName" value="${student.lastName}" class="form-control-custom" placeholder="e.g. Doe" required />
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div>
                            <label class="form-label-custom">Email Address</label>
                            <input type="email" name="email" value="${student.email}" class="form-control-custom" placeholder="e.g. student@college.edu" />
                        </div>
                        <div>
                            <label class="form-label-custom">Phone Number</label>
                            <input type="text" name="phone" value="${student.phone}" class="form-control-custom" placeholder="e.g. +91 9876543210" />
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem; margin-bottom: 1.25rem;">
                        <div>
                            <label class="form-label-custom">Semester / Grade *</label>
                            <input type="text" name="semester" value="${student.semester}" class="form-control-custom" placeholder="e.g. Semester 4" required />
                        </div>
                        <div>
                            <label class="form-label-custom">Section</label>
                            <input type="text" name="section" value="${student.section}" class="form-control-custom" placeholder="e.g. Section A" />
                        </div>
                        <div>
                            <label class="form-label-custom">Gender</label>
                            <select name="gender" class="form-select-custom">
                                <option value="MALE" ${student.gender == 'MALE' ? 'selected' : ''}>Male</option>
                                <option value="FEMALE" ${student.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
                                <option value="OTHER" ${student.gender == 'OTHER' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>
                    </div>

                    <div style="margin-bottom: 1.75rem;">
                        <label class="form-label-custom">Account Status</label>
                        <div style="display: flex; gap: 1.5rem; margin-top: 0.35rem;">
                            <label style="display: flex; align-items: center; gap: 0.45rem; color: var(--text-primary); cursor: pointer; font-size: 0.875rem;">
                                <input type="radio" name="active" value="true" ${student.active || student.id == null ? 'checked' : ''}> Active
                            </label>
                            <label style="display: flex; align-items: center; gap: 0.45rem; color: var(--text-muted); cursor: pointer; font-size: 0.875rem;">
                                <input type="radio" name="active" value="false" ${!student.active && student.id != null ? 'checked' : ''}> Inactive
                            </label>
                        </div>
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 0.75rem; border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                        <a href="${pageContext.request.contextPath}/students" class="btn-secondary-custom">Cancel</a>
                        <button type="submit" class="btn-primary-custom">
                            <i class="fa-solid fa-floppy-disk"></i> Save Student Record
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
