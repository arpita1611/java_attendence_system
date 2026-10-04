/**
 * AttendX Pro - Modern Interactive Client Scripts
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Live Clock in Navbar
    const clockEl = document.getElementById('liveClock');
    if (clockEl) {
        const updateClock = () => {
            const now = new Date();
            clockEl.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
        };
        updateClock();
        setInterval(updateClock, 1000);
    }

    // 2. Mobile Sidebar Toggle & Overlay
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('open');
            if (overlay) overlay.classList.toggle('active');
        });
    }

    if (overlay && sidebar) {
        overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('active');
        });
    }

    // 3. Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.custom-alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.4s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 400);
        }, 5000);
    });

    // 4. Client-side quick filter on data tables
    const tableSearchInput = document.getElementById('quickTableSearch');
    if (tableSearchInput) {
        tableSearchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            const rows = document.querySelectorAll('.custom-table tbody tr');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }
});

// Bulk Status Setter for Student and Employee Attendance Forms
function markAllStatus(statusValue) {
    const selects = document.querySelectorAll('.attendance-status-select');
    selects.forEach(select => {
        select.value = statusValue;
        select.style.borderColor = 'var(--primary)';
        setTimeout(() => select.style.borderColor = '', 600);
    });
}

// Bulk Time Setter for Employee Attendance Forms
function setAllTimes(checkInTime, checkOutTime) {
    if (checkInTime) {
        const checkIns = document.querySelectorAll('.employee-checkin-time');
        checkIns.forEach(input => input.value = checkInTime);
    }
    if (checkOutTime) {
        const checkOuts = document.querySelectorAll('.employee-checkout-time');
        checkOuts.forEach(input => input.value = checkOutTime);
    }
}
