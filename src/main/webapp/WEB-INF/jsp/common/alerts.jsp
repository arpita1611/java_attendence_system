<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty successMessage}">
    <div class="custom-alert custom-alert-success">
        <i class="fa-solid fa-circle-check" style="font-size: 1rem;"></i>
        <div>
            <strong>Success:</strong> <c:out value="${successMessage}" />
        </div>
    </div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div class="custom-alert custom-alert-error">
        <i class="fa-solid fa-circle-exclamation" style="font-size: 1rem;"></i>
        <div>
            <strong>Error:</strong> <c:out value="${errorMessage}" />
        </div>
    </div>
</c:if>
