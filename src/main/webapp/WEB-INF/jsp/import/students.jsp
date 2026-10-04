<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Import Students - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <div style="max-width: 800px; margin: 0 auto;">
            <div class="glass-card" style="padding: 1.75rem;">
                
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <h1 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.2rem;">
                            Import Students
                        </h1>
                        <span style="font-size: 0.8125rem; color: var(--text-muted);">
                            Upload a spreadsheet containing student roster details to enroll in batch.
                        </span>
                    </div>
                    <div style="display: flex; gap: 0.625rem;">
                        <a href="${pageContext.request.contextPath}/import/template/students.csv" class="btn-secondary-custom">
                            <i class="fa-solid fa-download"></i> Sample CSV
                        </a>
                        <a href="${pageContext.request.contextPath}/students" class="btn-secondary-custom">
                            <i class="fa-solid fa-arrow-left"></i> Students
                        </a>
                    </div>
                </div>

                <!-- CSV Format Instructions Card -->
                <div style="background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1rem; margin-bottom: 1.5rem;">
                    <h2 style="font-size: 0.875rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.35rem;">
                        <i class="fa-solid fa-circle-info" style="color: var(--primary);"></i> CSV Column Structure
                    </h2>
                    <p style="font-size: 0.8125rem; color: var(--text-secondary); margin-bottom: 0.5rem;">
                        Your CSV file must include the following column headers in the first row:
                    </p>
                    <code style="display: block; background: #FFFFFF; padding: 0.65rem 0.85rem; border-radius: var(--radius-xs); font-size: 0.8125rem; color: var(--primary); overflow-x: auto; white-space: nowrap; border: 1px solid var(--border-color); font-family: monospace;">
                        Roll Number, First Name, Last Name, Department Code, Semester, Section, Email, Phone, Gender
                    </code>
                </div>

                <!-- Upload Drag-Drop Area -->
                <form action="${pageContext.request.contextPath}/import/students" method="post" enctype="multipart/form-data" style="margin-bottom: 1.5rem;">
                    <div style="border: 2px dashed #CBD5E1; background: var(--bg-subtle); border-radius: var(--radius-lg); padding: 2rem 1.5rem; text-align: center; margin-bottom: 1.25rem;">
                        <div style="width: 52px; height: 52px; margin: 0 auto 0.75rem; border-radius: 50%; background: var(--primary-light); border: 1px solid var(--primary-border); display: flex; align-items: center; justify-content: center; font-size: 1.5rem; color: var(--primary);">
                            <i class="fa-solid fa-cloud-arrow-up"></i>
                        </div>
                        <h3 style="color: var(--text-primary); margin-bottom: 0.25rem; font-size: 1.05rem; font-weight: 600;">Select or Drag CSV File</h3>
                        <p style="font-size: 0.8125rem; color: var(--text-muted); margin-bottom: 1rem;">Only .csv format files are supported</p>
                        
                        <input type="file" name="file" accept=".csv" class="form-control-custom" style="max-width: 360px; margin: 0 auto; display: block;" required />
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 0.75rem;">
                        <button type="submit" class="btn-primary-custom" style="padding: 0.65rem 1.75rem; font-size: 0.9375rem;">
                            <i class="fa-solid fa-upload"></i> Process & Import Students
                        </button>
                    </div>
                </form>

                <!-- Import Results Display -->
                <c:if test="${not empty result}">
                    <div style="border-top: 1px solid var(--border-color); padding-top: 1.25rem;">
                        <h3 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.75rem;">
                            Import Execution Report
                        </h3>

                        <div style="display: flex; gap: 0.75rem; margin-bottom: 1rem; flex-wrap: wrap;">
                            <div class="badge-status badge-present" style="padding: 0.45rem 0.85rem; font-size: 0.8125rem;">
                                <i class="fa-solid fa-check"></i> ${result.successCount} Successfully Enrolled
                            </div>
                            <c:if test="${result.failureCount > 0}">
                                <div class="badge-status badge-absent" style="padding: 0.45rem 0.85rem; font-size: 0.8125rem;">
                                    <i class="fa-solid fa-xmark"></i> ${result.failureCount} Failed / Skipped Rows
                                </div>
                            </c:if>
                        </div>

                        <c:if test="${not empty result.errors}">
                            <div style="background: var(--danger-light); border: 1px solid var(--danger-border); border-radius: var(--radius-sm); padding: 0.875rem; margin-bottom: 1rem;">
                                <strong style="color: var(--danger-text); font-size: 0.8125rem; display: block; margin-bottom: 0.35rem;">Errors & Warnings:</strong>
                                <ul style="color: var(--danger-text); font-size: 0.8125rem; padding-left: 1.25rem; line-height: 1.5;">
                                    <c:forEach var="err" items="${result.errors}">
                                        <li>${err}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>

                        <c:if test="${not empty result.successMessages}">
                            <div style="background: var(--success-light); border: 1px solid var(--success-border); border-radius: var(--radius-sm); padding: 0.875rem;">
                                <strong style="color: var(--success-text); font-size: 0.8125rem; display: block; margin-bottom: 0.35rem;">Successfully Processed:</strong>
                                <div style="max-height: 140px; overflow-y: auto; font-size: 0.8125rem; color: var(--success-text); line-height: 1.5;">
                                    <c:forEach var="msg" items="${result.successMessages}">
                                        <div><i class="fa-solid fa-circle-check"></i> ${msg}</div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </c:if>

            </div>
        </div>

    </div>
</div>

<jsp:include page="../common/footer.jsp" />
