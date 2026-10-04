<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Departments - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Departments</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Manage academic and administrative departments</p>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 340px; gap: 1.25rem; align-items: start;">
            
            <!-- Left: Department List Table -->
            <div class="glass-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <div>
                        <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin: 0;">All Departments</h2>
                        <span style="font-size: 0.78125rem; color: var(--text-muted);">Registered academic units</span>
                    </div>
                    <input type="text" id="quickTableSearch" placeholder="Search departments..." class="form-control-custom" style="width: 180px; padding: 0.35rem 0.65rem; font-size: 0.8125rem;">
                </div>

                <div style="overflow-x: auto;">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;">Code</th>
                                <th>Department Name</th>
                                <th>Description</th>
                                <th style="text-align: right; width: 60px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="dept" items="${departments}">
                                <tr>
                                    <td>
                                        <span style="font-family: monospace; font-weight: 600; color: var(--primary); background: var(--primary-light); padding: 0.2rem 0.5rem; border-radius: var(--radius-xs); border: 1px solid var(--primary-border); font-size: 0.8125rem;">
                                            ${dept.code}
                                        </span>
                                    </td>
                                    <td><strong style="color: var(--text-primary);">${dept.name}</strong></td>
                                    <td style="color: var(--text-muted); font-size: 0.8125rem;">${not empty dept.description ? dept.description : '—'}</td>
                                    <td style="text-align: right;">
                                        <a href="${pageContext.request.contextPath}/departments/delete/${dept.id}" 
                                           onclick="return confirm('Are you sure you want to delete department ${dept.name}?');"
                                           class="btn-icon" title="Delete Department" style="color: var(--danger);">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty departments}">
                                <tr>
                                    <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 2.5rem 1rem;">
                                        No departments found. Add one using the form.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Right: Add Department Card -->
            <div class="glass-card">
                <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 1rem;">
                    <i class="fa-solid fa-circle-plus" style="color: var(--primary);"></i> Add Department
                </h2>

                <form action="${pageContext.request.contextPath}/departments/save" method="post">
                    <div style="margin-bottom: 0.875rem;">
                        <label class="form-label-custom">Department Name *</label>
                        <input type="text" name="name" class="form-control-custom" placeholder="e.g. Electrical Engineering" required>
                    </div>

                    <div style="margin-bottom: 0.875rem;">
                        <label class="form-label-custom">Department Code *</label>
                        <input type="text" name="code" class="form-control-custom" placeholder="e.g. EE" required>
                    </div>

                    <div style="margin-bottom: 1.25rem;">
                        <label class="form-label-custom">Description</label>
                        <textarea name="description" class="form-control-custom" rows="3" placeholder="Brief info about department..."></textarea>
                    </div>

                    <button type="submit" class="btn-primary-custom" style="width: 100%; justify-content: center;">
                        <i class="fa-solid fa-floppy-disk"></i> Save Department
                    </button>
                </form>
            </div>

        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
