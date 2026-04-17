/**
 * Practical 9 - Employee Task Management System
 * Frontend JavaScript  |  Vanilla JS REST Client
 */

const API = {
    employees : '/api/employees',
    tasks     : '/api/tasks',
};

// ── Global State ─────────────────────────────────────────────────────────────
let allEmployees = [];
let allTasks     = [];
let editingEmpId = null;

// ── On Page Load ─────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    setupTabs();
    setDefaultDueDate();
    loadDashboard();
    loadAllEmployees();
    loadAllTasks();
    populateAssignEmployeeDropdown();
});

// ── TAB NAVIGATION ────────────────────────────────────────────────────────────
function setupTabs() {
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            const tabName = tab.dataset.tab;
            document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
            tab.classList.add('active');
            document.getElementById('page-' + tabName).classList.add('active');
            // Refresh data when switching tabs
            if (tabName === 'dashboard')  loadDashboard();
            if (tabName === 'employees')  loadAllEmployees();
            if (tabName === 'tasks')      loadAllTasks();
            if (tabName === 'assign')     populateAssignEmployeeDropdown();
        });
    });
}

// ── HELPERS ───────────────────────────────────────────────────────────────────
function setDefaultDueDate() {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 7);
    const dd = document.getElementById('task-due');
    if (dd) dd.value = tomorrow.toISOString().split('T')[0];
}

async function fetchJSON(url, options = {}) {
    const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Request failed');
    return data;
}

function showToast(msg, type = 'success') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `toast show ${type}`;
    setTimeout(() => t.classList.remove('show'), 3500);
}

function statusBadge(status) {
    const map = {
        PENDING     : 'badge-pending',
        IN_PROGRESS : 'badge-in_progress',
        COMPLETED   : 'badge-completed',
        OVERDUE     : 'badge-overdue',
        CANCELLED   : 'badge-cancelled',
    };
    const label = status.replace('_', ' ');
    return `<span class="badge ${map[status] || ''}">${label}</span>`;
}

function typeBadge(type) {
    const map = {
        EXAM_DUTY          : 'badge-exam_duty',
        PROJECT_GUIDE      : 'badge-project_guide',
        DOCUMENTATION      : 'badge-documentation',
        EVENT_COORDINATION : 'badge-event_coordination',
        REPORT_SUBMISSION  : 'badge-report_submission',
        OTHER              : 'badge-other',
    };
    const label = type.replace(/_/g,' ');
    return `<span class="badge ${map[type] || ''}">${label}</span>`;
}

function priorityBadge(p) {
    return `<span class="badge badge-${p.toLowerCase()}">${p}</span>`;
}

function formatDate(d) {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' });
}

function daysLeft(dueDateArr) {
    const due = new Date(dueDateArr[0], dueDateArr[1]-1, dueDateArr[2]);
    const diff = Math.ceil((due - new Date()) / 86400000);
    if (diff < 0)  return `<span style="color:var(--red)">⚠ ${Math.abs(diff)}d overdue</span>`;
    if (diff === 0) return `<span style="color:var(--amber)">Due today</span>`;
    if (diff <= 3)  return `<span style="color:var(--amber)">In ${diff}d</span>`;
    return `<span style="color:var(--text2)">In ${diff}d</span>`;
}

function formatDateArr(arr) {
    if (!arr) return '—';
    return new Date(arr[0], arr[1]-1, arr[2]).toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' });
}

// ── DASHBOARD ─────────────────────────────────────────────────────────────────
async function loadDashboard() {
    try {
        const stats = await fetchJSON(API.tasks + '/stats');
        document.getElementById('stat-employees').textContent = stats.employees ?? '—';
        document.getElementById('stat-total').textContent     = stats.total ?? '—';
        document.getElementById('stat-pending').textContent   = stats.pending ?? '—';
        document.getElementById('stat-inprogress').textContent= stats.inProgress ?? '—';
        document.getElementById('stat-completed').textContent = stats.completed ?? '—';
        document.getElementById('stat-overdue').textContent   = stats.overdue ?? '—';

        // Recent tasks (up to 8)
        const tasks = await fetchJSON(API.tasks);
        const recent = tasks.slice(-8).reverse();
        const recentEl = document.getElementById('recent-tasks-list');
        if (recent.length === 0) {
            recentEl.innerHTML = '<p style="color:var(--text2);font-size:.85rem">No tasks yet. Assign tasks from the Assign Task tab.</p>';
        } else {
            recentEl.innerHTML = recent.map(t => `
                <div class="task-item-compact">
                    ${statusBadge(t.status)}
                    <span class="task-item-name">${t.title}</span>
                    ${typeBadge(t.taskType)}
                    <span class="task-item-emp">${t.employee?.name ?? '—'}</span>
                    <span>${daysLeft(t.dueDate)}</span>
                </div>
            `).join('');
        }

        // Recent Faculty
        const employees = await fetchJSON(API.employees);
        const recentFac = employees.slice(-5).reverse();
        const facEl = document.getElementById('recent-faculty-list');
        if (recentFac.length === 0) {
            facEl.innerHTML = '<p style="color:var(--text2);font-size:.85rem">No faculty yet.</p>';
        } else {
            facEl.innerHTML = recentFac.map(f => `
                <div class="task-item-compact">
                    <span class="task-item-name"><strong>${f.name}</strong></span>
                    <span class="task-item-emp">${f.designation}</span>
                    <span class="task-item-emp" style="color:var(--accent)">${f.department}</span>
                </div>
            `).join('');
        }

        // Overdue panel
        const overdueEl  = document.getElementById('overdue-tasks-list');
        const overduePanel = document.getElementById('overdue-panel');
        const overdueTasks = tasks.filter(t => t.status === 'OVERDUE');
        if (overdueTasks.length > 0) {
            overduePanel.style.display = 'block';
            overdueEl.innerHTML = overdueTasks.map(t => `
                <div class="task-item-compact">
                    ${statusBadge(t.status)}
                    <span class="task-item-name">${t.title}</span>
                    <span class="task-item-emp">${t.employee?.name ?? '—'}</span>
                    ${priorityBadge(t.priority)}
                </div>
            `).join('');
        } else {
            overduePanel.style.display = 'none';
        }
    } catch (err) {
        console.error('Dashboard error:', err);
    }
}

// ── EMPLOYEES ─────────────────────────────────────────────────────────────────
async function loadAllEmployees() {
    try {
        allEmployees = await fetchJSON(API.employees);
        renderEmployeeTable(allEmployees);
    } catch (err) {
        document.getElementById('employee-tbody').innerHTML =
            `<tr><td colspan="8" class="loading-row" style="color:var(--red)">Failed to load employees.</td></tr>`;
    }
}

function renderEmployeeTable(list) {
    const tbody = document.getElementById('employee-tbody');
    if (list.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="loading-row">No faculty members found.</td></tr>`;
        return;
    }
    tbody.innerHTML = list.map((e, i) => `
        <tr>
            <td style="color:var(--text2)">${i + 1}</td>
            <td><strong>${e.name}</strong></td>
            <td><span style="font-size:.8rem;color:var(--text2)">${e.designation}</span></td>
            <td>${e.department}</td>
            <td style="font-size:.82rem">${e.email}</td>
            <td style="font-size:.82rem;color:var(--text2)">${e.phone ?? '—'}</td>
            <td style="font-size:.8rem;color:var(--text2);max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap" title="${e.expertise ?? ''}">${e.expertise ?? '—'}</td>
            <td>
                <div style="display:flex;gap:6px">
                    <button class="btn btn-outline btn-sm" onclick="openEmployeeModal(${e.id})">✏ Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteEmployee(${e.id}, '${e.name.replace(/'/g,"\\'")}')">🗑</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function searchEmployees() {
    const q = document.getElementById('emp-search').value.toLowerCase();
    const filtered = allEmployees.filter(e =>
        e.name.toLowerCase().includes(q) ||
        e.department.toLowerCase().includes(q) ||
        e.designation.toLowerCase().includes(q)
    );
    renderEmployeeTable(filtered);
}

// ── EMPLOYEE MODAL ────────────────────────────────────────────────────────────
function openEmployeeModal(id = null) {
    editingEmpId = id;
    const modal = document.getElementById('emp-modal');
    const title = document.getElementById('modal-title');
    const btn   = document.getElementById('emp-submit-btn');

    if (id) {
        const emp = allEmployees.find(e => e.id === id);
        if (!emp) return;
        title.textContent = 'Edit Faculty Member';
        btn.textContent   = 'Update Faculty';
        document.getElementById('emp-name').value        = emp.name;
        document.getElementById('emp-email').value       = emp.email;
        document.getElementById('emp-designation').value = emp.designation;
        document.getElementById('emp-department').value  = emp.department;
        document.getElementById('emp-phone').value       = emp.phone ?? '';
        document.getElementById('emp-expertise').value   = emp.expertise ?? '';
    } else {
        title.textContent = 'Add Faculty Member';
        btn.textContent   = 'Save Faculty';
        document.getElementById('emp-form').reset();
    }
    modal.style.display = 'flex';
}

function closeEmployeeModal() {
    document.getElementById('emp-modal').style.display = 'none';
    editingEmpId = null;
}

async function submitEmployee(e) {
    e.preventDefault();
    const payload = {
        name       : document.getElementById('emp-name').value.trim(),
        email      : document.getElementById('emp-email').value.trim(),
        designation: document.getElementById('emp-designation').value,
        department : document.getElementById('emp-department').value,
        phone      : document.getElementById('emp-phone').value.trim(),
        expertise  : document.getElementById('emp-expertise').value.trim(),
    };

    try {
        if (editingEmpId) {
            await fetchJSON(`${API.employees}/${editingEmpId}`, { method: 'PUT', body: JSON.stringify(payload) });
            showToast('Faculty updated successfully!');
        } else {
            await fetchJSON(API.employees, { method: 'POST', body: JSON.stringify(payload) });
            showToast('Faculty member added!');
        }
        closeEmployeeModal();
        await loadAllEmployees();
        loadDashboard();
        setTimeout(() => {
            document.getElementById('tab-employees').click();
        }, 100);
    } catch (err) {
        showToast('Error: ' + err.message, 'error');
    }
}

async function deleteEmployee(id, name) {
    if (!confirm(`Delete faculty member "${name}"?\n\nWarning: This will also delete all their assigned tasks.`)) return;
    try {
        await fetchJSON(`${API.employees}/${id}`, { method: 'DELETE' });
        showToast(`🗑 ${name} deleted.`);
        await loadAllEmployees();
        await loadAllTasks();
        loadDashboard();
    } catch (err) {
        showToast('❌ ' + err.message, 'error');
    }
}

// ── TASKS ──────────────────────────────────────────────────────────────────────
async function loadAllTasks() {
    try {
        allTasks = await fetchJSON(API.tasks);
        renderTaskCards(allTasks);
    } catch (err) {
        document.getElementById('task-cards-grid').innerHTML =
            `<div class="loading-placeholder" style="color:var(--red)">Failed to load tasks.</div>`;
    }
}

function renderTaskCards(list) {
    const grid = document.getElementById('task-cards-grid');
    if (list.length === 0) {
        grid.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">📋</div>
                <p>No tasks found. Use the <strong>Assign Task</strong> tab to add tasks.</p>
            </div>`;
        return;
    }
    grid.innerHTML = list.map(t => `
        <div class="task-card priority-${t.priority.toLowerCase()}" onclick="openTaskModal(${t.id})">
            <div class="task-priority-bar"></div>
            <div style="padding-left:8px">
                <div class="task-card-top">
                    <div class="task-card-title">${t.title}</div>
                    ${statusBadge(t.status)}
                </div>
                <div class="task-card-desc">${t.description || '<em style="color:var(--text2)">No description provided</em>'}</div>
                <div class="task-card-meta">
                    ${typeBadge(t.taskType)}
                    ${priorityBadge(t.priority)}
                </div>
                <div class="task-card-footer">
                    <span>${t.employee?.name ?? '—'}</span>
                    <span>${daysLeft(t.dueDate)}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function filterTasks() {
    const status  = document.getElementById('filter-status').value;
    const type    = document.getElementById('filter-type').value;
    const keyword = document.getElementById('task-search').value.toLowerCase();

    let filtered = allTasks;
    if (status)  filtered = filtered.filter(t => t.status === status);
    if (type)    filtered = filtered.filter(t => t.taskType === type);
    if (keyword) filtered = filtered.filter(t => t.title.toLowerCase().includes(keyword));
    renderTaskCards(filtered);
}

function searchTasks() { filterTasks(); }

// ── TASK DETAIL MODAL ──────────────────────────────────────────────────────────
async function openTaskModal(id) {
    const task = allTasks.find(t => t.id === id) || await fetchJSON(`${API.tasks}/${id}`);
    const modal = document.getElementById('task-modal');
    const body  = document.getElementById('task-modal-body');

    body.innerHTML = `
        <div>
            <div style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:1rem">
                ${statusBadge(task.status)}
                ${typeBadge(task.taskType)}
                ${priorityBadge(task.priority)}
            </div>
            <h4 style="font-size:1.1rem;font-weight:700;margin-bottom:.5rem">${task.title}</h4>
            <p style="color:var(--text2);font-size:.88rem;margin-bottom:1rem">${task.description || 'No description provided.'}</p>
            <div class="detail-row">
                <div class="detail-item">
                    <div class="detail-label">Assigned To</div>
                    <div class="detail-value">${task.employee?.name ?? '—'}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Department</div>
                    <div class="detail-value">${task.employee?.department ?? '—'}</div>
                </div>
            </div>
            <div class="detail-row">
                <div class="detail-item">
                    <div class="detail-label">Assigned Date</div>
                    <div class="detail-value">${formatDateArr(task.assignedDate)}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Due Date</div>
                    <div class="detail-value">${formatDateArr(task.dueDate)}</div>
                </div>
            </div>

            <div class="status-select-row">
                <label style="font-size:.85rem;color:var(--text2);font-weight:500">Update Status:</label>
                <select id="modal-status-select" style="background:var(--bg3);border:1px solid var(--border);color:var(--text);border-radius:8px;padding:6px 10px;font-family:Inter,sans-serif;flex:1;max-width:200px">
                    <option value="PENDING"     ${task.status==='PENDING'     ?'selected':''}>Pending</option>
                    <option value="IN_PROGRESS" ${task.status==='IN_PROGRESS' ?'selected':''}>In Progress</option>
                    <option value="COMPLETED"   ${task.status==='COMPLETED'   ?'selected':''}>Completed</option>
                    <option value="OVERDUE"     ${task.status==='OVERDUE'     ?'selected':''}>Overdue</option>
                    <option value="CANCELLED"   ${task.status==='CANCELLED'   ?'selected':''}>Cancelled</option>
                </select>
                <button class="btn btn-success btn-sm" onclick="updateStatus(${task.id})">Save Status</button>
                <button class="btn btn-danger btn-sm" onclick="deleteTaskFromModal(${task.id})">🗑 Delete</button>
            </div>
        </div>
    `;
    modal.style.display = 'flex';
}

function closeTaskModal() {
    document.getElementById('task-modal').style.display = 'none';
}

async function updateStatus(taskId) {
    const newStatus = document.getElementById('modal-status-select').value;
    try {
        await fetchJSON(`${API.tasks}/${taskId}/status?status=${newStatus}`, { method: 'PATCH' });
        showToast('✅ Task status updated!');
        closeTaskModal();
        await loadAllTasks();
        loadDashboard();
    } catch (err) {
        showToast('❌ ' + err.message, 'error');
    }
}

async function deleteTaskFromModal(taskId) {
    if (!confirm('Delete this task permanently?')) return;
    try {
        await fetchJSON(`${API.tasks}/${taskId}`, { method: 'DELETE' });
        showToast('🗑 Task deleted.');
        closeTaskModal();
        await loadAllTasks();
        loadDashboard();
    } catch (err) {
        showToast('❌ ' + err.message, 'error');
    }
}

// ── ASSIGN TASK ────────────────────────────────────────────────────────────────
async function populateAssignEmployeeDropdown() {
    try {
        const employees = await fetchJSON(API.employees);
        allEmployees = employees;
        const sel = document.getElementById('task-employee');
        const current = sel.value;
        sel.innerHTML = '<option value="">— Select Faculty Member —</option>' +
            employees.map(e => `<option value="${e.id}">${e.name} (${e.designation} · ${e.department})</option>`).join('');
        if (current) sel.value = current;
    } catch (e) { /* silent */ }
}

async function submitAssignTask(ev) {
    ev.preventDefault();
    const employeeId = document.getElementById('task-employee').value;
    const title      = document.getElementById('task-title').value.trim();
    const taskType   = document.getElementById('task-type').value;
    const description= document.getElementById('task-desc').value.trim();
    const priority   = document.getElementById('task-priority').value;
    const status     = document.getElementById('task-status').value;
    const dueDate    = document.getElementById('task-due').value;

    const payload = { title, description, taskType, status, priority, dueDate };

    try {
        await fetchJSON(`${API.tasks}?employeeId=${employeeId}`, {
            method: 'POST',
            body: JSON.stringify(payload),
        });
        showToast('✅ Task assigned successfully!');
        document.getElementById('assign-task-form').reset();
        setDefaultDueDate();
        await loadAllTasks();
        loadDashboard();
        // Switch to tasks tab
        setTimeout(() => {
            document.getElementById('tab-tasks').click();
        }, 800);
    } catch (err) {
        showToast('❌ ' + err.message, 'error');
    }
}

// ── CLOSE MODALS ON OVERLAY CLICK ─────────────────────────────────────────────
document.getElementById('emp-modal').addEventListener('click', function(e) {
    if (e.target === this) closeEmployeeModal();
});
document.getElementById('task-modal').addEventListener('click', function(e) {
    if (e.target === this) closeTaskModal();
});
