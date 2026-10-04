<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Live Attendance Kiosk - AttendX Pro" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="../common/navbar.jsp" />
    <div class="content-body">
        <jsp:include page="../common/alerts.jsp" />

        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Live Attendance Kiosk</h1>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin: 0;">Rapid RFID / Barcode scanning station for real-time classroom check-ins</p>
            </div>
            <div style="display: flex; gap: 0.625rem; align-items: center;">
                <button type="button" id="soundToggleBtn" onclick="toggleSound()" class="btn-secondary-custom" style="font-size: 0.8125rem;">
                    <i class="fa-solid fa-volume-high" id="soundIcon"></i> Sound ON
                </button>
            </div>
        </div>

        <!-- Kiosk Main Grid -->
        <div style="display: grid; grid-template-columns: 1.2fr 0.8fr; gap: 1.25rem; align-items: start;">
            
            <!-- Left: Minimal Scanner Station -->
            <div class="glass-card" style="padding: 1.75rem;">
                
                <!-- Mode Selector Segmented Buttons -->
                <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 0.5rem; margin-bottom: 1.5rem; background: var(--bg-subtle); padding: 0.35rem; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                    <button type="button" onclick="setMode('STUDENT_CHECKIN')" id="mode-student" class="btn-primary-custom mode-btn" style="justify-content: center; font-size: 0.8125rem; padding: 0.5rem;">
                        <i class="fa-solid fa-graduation-cap"></i> Students
                    </button>
                    <button type="button" onclick="setMode('EMPLOYEE_CHECKIN')" id="mode-emp-in" class="btn-secondary-custom mode-btn" style="justify-content: center; font-size: 0.8125rem; padding: 0.5rem; border: none; background: transparent;">
                        <i class="fa-solid fa-right-to-bracket"></i> Staff In
                    </button>
                    <button type="button" onclick="setMode('EMPLOYEE_CHECKOUT')" id="mode-emp-out" class="btn-secondary-custom mode-btn" style="justify-content: center; font-size: 0.8125rem; padding: 0.5rem; border: none; background: transparent;">
                        <i class="fa-solid fa-right-from-bracket"></i> Staff Out
                    </button>
                </div>

                <!-- Centered Scanner Area -->
                <div class="kiosk-scanner-box">
                    <div class="kiosk-scanner-icon">
                        <i class="fa-solid fa-barcode" id="scannerVisualIcon"></i>
                    </div>

                    <h2 id="modeHeader" style="color: var(--text-primary); font-size: 1.2rem; font-weight: 700; margin-bottom: 0.25rem;">
                        Scan Student ID
                    </h2>
                    <p id="modeSubHeader" style="font-size: 0.8125rem; color: var(--text-muted); max-width: 400px; margin: 0 auto 1.25rem;">
                        Scan barcode or enter roll number (e.g. CSE-2024-001) and press Enter
                    </p>

                    <!-- Input Entry Form -->
                    <form id="kioskForm" onsubmit="handleKioskSubmit(event)" style="max-width: 460px; margin: 0 auto;">
                        <div style="display: flex; gap: 0.5rem;">
                            <input type="text" id="scanInput" placeholder="Scan Barcode / Enter ID..." class="form-control-custom" style="flex: 1; font-size: 1rem; padding: 0.65rem 1rem; font-family: monospace; text-align: center; border-color: var(--primary);" autofocus autocomplete="off" />
                            <button type="submit" class="btn-primary-custom" style="padding: 0 1.25rem; font-size: 0.875rem;">
                                <i class="fa-solid fa-arrow-right"></i> Submit
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Feedback Banner Card -->
                <div id="scanResultCard" style="display: none; padding: 1rem 1.25rem; border-radius: var(--radius-md); background: var(--success-light); border: 1px solid var(--success-border); transition: all 0.2s ease;">
                    <div style="display: flex; align-items: center; gap: 1rem;">
                        <div id="resultAvatar" style="width: 48px; height: 48px; border-radius: var(--radius-md); background: var(--primary-light); border: 1px solid var(--primary-border); display: flex; align-items: center; justify-content: center; font-size: 1.4rem; font-weight: 700; color: var(--primary); flex-shrink: 0;">
                            A
                        </div>
                        <div style="flex: 1;">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h3 id="resultName" style="color: var(--text-primary); font-size: 1.05rem; font-weight: 700; margin: 0;">Aarav Sharma</h3>
                                <span id="resultBadge" class="badge-status badge-present">Present</span>
                            </div>
                            <div id="resultDetails" style="font-size: 0.8125rem; color: var(--text-secondary); margin: 0.15rem 0;">
                                CSE-2024-001 • Computer Science (Semester 4)
                            </div>
                            <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.75rem; color: var(--text-muted); margin-top: 0.25rem;">
                                <span id="resultTime"><i class="fa-regular fa-clock"></i> 09:15 AM</span>
                                <span id="resultRate" style="color: var(--success); font-weight: 600;">Overall Rate: 92%</span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Right: Real-time Live Station Feed -->
            <div class="glass-card" style="padding: 1.5rem;">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <div>
                        <h2 style="font-size: 1rem; font-weight: 600; color: var(--text-primary); margin: 0;">Recent Scans</h2>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">Real-time check-in stream</span>
                    </div>
                    <span id="liveScanCount" class="badge-status" style="background: var(--bg-subtle); color: var(--text-secondary); border: 1px solid var(--border-color);">
                        0 Scans
                    </span>
                </div>

                <div id="liveActivityList" style="display: flex; flex-direction: column; gap: 0.5rem; max-height: 440px; overflow-y: auto;">
                    <div id="emptyStreamPlaceholder" style="text-align: center; padding: 3rem 1rem; color: var(--text-muted);">
                        <i class="fa-solid fa-satellite-dish" style="font-size: 2rem; margin-bottom: 0.5rem; display: block; color: var(--text-light);"></i>
                        Waiting for scans...<br>
                        <span style="font-size: 0.78125rem;">ID / Barcode events will appear here.</span>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>

<script>
let currentMode = 'STUDENT_CHECKIN';
let soundEnabled = true;
let scanSessionCount = 0;

// Web Audio API Synthesizer
const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

function playTone(type) {
    if (!soundEnabled) return;
    if (audioCtx.state === 'suspended') {
        audioCtx.resume();
    }

    const now = audioCtx.currentTime;
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.connect(gain);
    gain.connect(audioCtx.destination);

    if (type === 'success') {
        osc.frequency.setValueAtTime(587.33, now); // D5
        osc.frequency.setValueAtTime(880.00, now + 0.08); // A5
        gain.gain.setValueAtTime(0.15, now);
        gain.gain.exponentialRampToValueAtTime(0.01, now + 0.25);
        osc.start(now);
        osc.stop(now + 0.25);
    } else {
        osc.type = 'sawtooth';
        osc.frequency.setValueAtTime(160, now);
        gain.gain.setValueAtTime(0.2, now);
        gain.gain.exponentialRampToValueAtTime(0.01, now + 0.3);
        osc.start(now);
        osc.stop(now + 0.3);
    }
}

function toggleSound() {
    soundEnabled = !soundEnabled;
    const icon = document.getElementById('soundIcon');
    const btn = document.getElementById('soundToggleBtn');
    if (soundEnabled) {
        icon.className = 'fa-solid fa-volume-high';
        btn.innerHTML = '<i class="fa-solid fa-volume-high" id="soundIcon"></i> Sound ON';
    } else {
        icon.className = 'fa-solid fa-volume-xmark';
        btn.innerHTML = '<i class="fa-solid fa-volume-xmark" id="soundIcon"></i> Sound OFF';
    }
}

function setMode(mode) {
    currentMode = mode;
    
    // Reset buttons
    const studentBtn = document.getElementById('mode-student');
    const empInBtn = document.getElementById('mode-emp-in');
    const empOutBtn = document.getElementById('mode-emp-out');

    studentBtn.className = 'btn-secondary-custom mode-btn';
    studentBtn.style.background = 'transparent';
    studentBtn.style.border = 'none';

    empInBtn.className = 'btn-secondary-custom mode-btn';
    empInBtn.style.background = 'transparent';
    empInBtn.style.border = 'none';

    empOutBtn.className = 'btn-secondary-custom mode-btn';
    empOutBtn.style.background = 'transparent';
    empOutBtn.style.border = 'none';

    const scanInput = document.getElementById('scanInput');
    const modeHeader = document.getElementById('modeHeader');
    const modeSubHeader = document.getElementById('modeSubHeader');
    const scannerVisualIcon = document.getElementById('scannerVisualIcon');

    if (mode === 'STUDENT_CHECKIN') {
        studentBtn.className = 'btn-primary-custom mode-btn';
        studentBtn.style.background = 'var(--primary)';
        studentBtn.style.border = '1px solid transparent';
        modeHeader.textContent = 'Scan Student ID';
        modeSubHeader.textContent = 'Scan barcode or enter roll number (e.g. CSE-2024-001)';
        scanInput.placeholder = 'Enter Roll Number (e.g. CSE-2024-001)...';
        scannerVisualIcon.className = 'fa-solid fa-graduation-cap';
    } else if (mode === 'EMPLOYEE_CHECKIN') {
        empInBtn.className = 'btn-primary-custom mode-btn';
        empInBtn.style.background = 'var(--primary)';
        empInBtn.style.border = '1px solid transparent';
        modeHeader.textContent = 'Staff Check-In';
        modeSubHeader.textContent = 'Scan badge or enter employee code (e.g. EMP-101)';
        scanInput.placeholder = 'Enter Employee Code (e.g. EMP-101)...';
        scannerVisualIcon.className = 'fa-solid fa-right-to-bracket';
    } else if (mode === 'EMPLOYEE_CHECKOUT') {
        empOutBtn.className = 'btn-primary-custom mode-btn';
        empOutBtn.style.background = 'var(--primary)';
        empOutBtn.style.border = '1px solid transparent';
        modeHeader.textContent = 'Staff Check-Out';
        modeSubHeader.textContent = 'Record departure time with employee code (e.g. EMP-101)';
        scanInput.placeholder = 'Enter Employee Code (e.g. EMP-101)...';
        scannerVisualIcon.className = 'fa-solid fa-right-from-bracket';
    }

    scanInput.focus();
}

async function handleKioskSubmit(e) {
    e.preventDefault();
    const scanInput = document.getElementById('scanInput');
    const code = scanInput.value.trim();
    if (!code) return;

    scanInput.value = '';

    try {
        const response = await fetch('${pageContext.request.contextPath}/api/kiosk/scan', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code: code, mode: currentMode })
        });

        const data = await response.json();
        showScanResult(data, code);

    } catch (err) {
        showScanResult({
            success: false,
            status: 'ERROR',
            message: 'Network connection error: ' + err.message
        }, code);
    }

    scanInput.focus();
}

function showScanResult(res, scannedCode) {
    const card = document.getElementById('scanResultCard');
    card.style.display = 'block';

    if (res.success) {
        playTone('success');
        scanSessionCount++;
        document.getElementById('liveScanCount').textContent = scanSessionCount + ' Scans';

        card.style.background = 'var(--success-light)';
        card.style.borderColor = 'var(--success-border)';

        document.getElementById('resultAvatar').textContent = res.name.charAt(0);
        document.getElementById('resultName').textContent = res.name;
        document.getElementById('resultBadge').className = 'badge-status badge-present';
        document.getElementById('resultBadge').textContent = 'SUCCESS';
        document.getElementById('resultDetails').textContent = res.identifier + ' • ' + res.department + ' (' + res.details + ')';
        document.getElementById('resultTime').innerHTML = '<i class="fa-regular fa-clock"></i> ' + res.time;
        document.getElementById('resultRate').textContent = 'Rate: ' + (res.currentPercentage || 0) + '%';

        addToActivityFeed(res);

    } else {
        playTone('error');
        card.style.background = 'var(--danger-light)';
        card.style.borderColor = 'var(--danger-border)';

        document.getElementById('resultAvatar').textContent = '!';
        document.getElementById('resultName').textContent = 'Not Recognized';
        document.getElementById('resultBadge').className = 'badge-status badge-absent';
        document.getElementById('resultBadge').textContent = 'FAILED';
        document.getElementById('resultDetails').textContent = res.message || ('Code: ' + scannedCode);
        document.getElementById('resultTime').textContent = new Date().toLocaleTimeString();
        document.getElementById('resultRate').textContent = 'Action Required';
    }
}

function addToActivityFeed(res) {
    const placeholder = document.getElementById('emptyStreamPlaceholder');
    if (placeholder) placeholder.remove();

    const list = document.getElementById('liveActivityList');
    const item = document.createElement('div');
    item.className = 'glass-card';
    item.style.padding = '0.65rem 0.85rem';
    item.style.display = 'flex';
    item.style.alignItems = 'center';
    item.style.justifyContent = 'space-between';
    item.style.fontSize = '0.8125rem';
    item.style.borderRadius = 'var(--radius-sm)';

    const isStudent = res.entityType === 'STUDENT';
    const iconClass = isStudent ? 'fa-graduation-cap' : 'fa-user-tie';

    item.innerHTML = `
        <div style="display: flex; align-items: center; gap: 0.65rem;">
            <div style="width: 28px; height: 28px; border-radius: 4px; background: var(--bg-subtle); display: flex; align-items: center; justify-content: center; color: var(--primary);">
                <i class="fa-solid ` + iconClass + `"></i>
            </div>
            <div>
                <strong style="color: var(--text-primary); font-size: 0.8125rem;">` + res.name + `</strong>
                <div style="font-size: 0.72rem; color: var(--text-muted);">` + res.identifier + ` • ` + res.department + `</div>
            </div>
        </div>
        <div style="text-align: right;">
            <span class="badge-status badge-present" style="font-size: 0.65rem; padding: 0.15rem 0.4rem;">OK</span>
            <div style="font-size: 0.6875rem; color: var(--text-muted); margin-top: 0.15rem;">` + res.time + `</div>
        </div>
    `;

    list.insertBefore(item, list.firstChild);
}

// Keep autofocus alive on click outside
document.addEventListener('click', (e) => {
    if (!['INPUT', 'TEXTAREA', 'BUTTON', 'SELECT', 'A'].includes(e.target.tagName)) {
        const input = document.getElementById('scanInput');
        if (input) input.focus();
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
