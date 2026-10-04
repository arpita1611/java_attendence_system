<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<header class="top-navbar">
    <div style="display: flex; align-items: center; gap: 0.875rem;">
        <button type="button" id="sidebarToggle" class="mobile-toggle-btn" aria-label="Toggle navigation">
            <i class="fa-solid fa-bars"></i>
        </button>
        <div>
            <h2 style="font-size: 1.05rem; font-weight: 600; color: var(--text-primary); margin: 0; line-height: 1.2;">
                <c:out value="${pageTitle != null ? pageTitle : 'Overview & Dashboard'}" />
            </h2>
        </div>
    </div>

    <div style="display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
        <!-- Live Clock Pill -->
        <div style="display: flex; align-items: center; gap: 0.45rem; background: var(--bg-subtle); padding: 0.35rem 0.75rem; border-radius: var(--radius-full); border: 1px solid var(--border-color); font-size: 0.8125rem; color: var(--text-secondary);">
            <i class="fa-regular fa-clock" style="color: var(--text-muted); font-size: 0.85rem;"></i>
            <span id="liveClock" style="font-weight: 600; font-family: inherit;">00:00:00</span>
        </div>

        <!-- Smart Kiosk Direct Button -->
        <a href="${pageContext.request.contextPath}/attendance/kiosk" class="btn-secondary-custom" style="padding: 0.4rem 0.8rem; font-size: 0.8125rem;">
            <i class="fa-solid fa-barcode"></i>
            <span>Live Kiosk</span>
        </a>

        <!-- Quick Attendance Shortcut -->
        <a href="${pageContext.request.contextPath}/attendance/students/mark" class="btn-primary-custom" style="padding: 0.4rem 0.8rem; font-size: 0.8125rem;">
            <i class="fa-solid fa-plus"></i>
            <span>Take Attendance</span>
        </a>

        <!-- User Profile -->
        <div style="display: flex; align-items: center; gap: 0.6rem; padding-left: 0.75rem; border-left: 1px solid var(--border-color);">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: var(--primary-light); border: 1px solid var(--primary-border); display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 0.8125rem; color: var(--primary);">
                AD
            </div>
            <div style="display: flex; flex-direction: column;">
                <span style="font-size: 0.8125rem; font-weight: 600; color: var(--text-primary); line-height: 1.1;">Admin Console</span>
                <span style="font-size: 0.7rem; color: var(--success); font-weight: 500;">System Online</span>
            </div>
        </div>
    </div>
</header>
