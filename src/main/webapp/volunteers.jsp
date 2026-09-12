<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.disasterrelief.model.Volunteer" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volunteers - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="header-icon-badge" style="background: rgba(16, 185, 129, 0.15); color: #10b981;">
                        <i class="fa-solid fa-hands-holding-child fa-lg"></i>
                    </div>
                    <div>
                        <h1>Disaster Response Volunteer Roster</h1>
                        <p>Track field responder skills, certifications (<code>HashSet</code>), and mission assignments</p>
                    </div>
                </div>
            </div>
            <div class="header-actions">
                <button class="btn btn-primary" onclick="openModal('volunteerModal')">
                    <i class="fa-solid fa-user-plus"></i> Register Volunteer
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Volunteer records updated successfully.</span>
            </div>
        <% } %>

        <%
            List<Volunteer> volunteers = (List<Volunteer>) request.getAttribute("volunteers");
            int totalVol = (volunteers != null) ? volunteers.size() : 0;
            int availVol = 0;
            int assignedVol = 0;
            if (volunteers != null) {
                for (Volunteer v : volunteers) {
                    if ("AVAILABLE".equalsIgnoreCase(v.getAvailabilityStatus())) availVol++;
                    else if ("ASSIGNED".equalsIgnoreCase(v.getAvailabilityStatus())) assignedVol++;
                }
            }
            Set<String> skills = (Set<String>) request.getAttribute("uniqueSkills");
            int skillCount = (skills != null) ? skills.size() : 0;
        %>

        <!-- STATS OVERVIEW CARDS -->
        <div class="stats-grid" style="margin-bottom: 1.5rem;">
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-users"></i> Total Registered</span>
                <div class="stat-value"><%= totalVol %></div>
                <div class="stat-meta">Vetted personnel</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-user-clock" style="color: #10b981;"></i> Ready for Dispatch</span>
                <div class="stat-value" style="color: #10b981;"><%= availVol %></div>
                <div class="stat-meta">Immediate deployment</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-truck-fast" style="color: var(--primary);"></i> In Field Operations</span>
                <div class="stat-value" style="color: #60a5fa;"><%= assignedVol %></div>
                <div class="stat-meta">Active relief zones</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-award" style="color: #f59e0b;"></i> Certified Domains</span>
                <div class="stat-value" style="color: #f59e0b;"><%= skillCount %></div>
                <div class="stat-meta">Unique skill types</div>
            </div>
        </div>

        <!-- UNIQUE SKILLS (Demonstrating HashSet) -->
        <div class="card" style="margin-bottom: 1.5rem; background: linear-gradient(135deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.8)); border-color: rgba(99, 102, 241, 0.25);">
            <div class="card-body" style="padding: 1.1rem 1.4rem; display: flex; align-items: center; gap: 1rem; flex-wrap: wrap;">
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <i class="fa-solid fa-certificate" style="color: var(--accent);"></i>
                    <span style="font-size: 0.85rem; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">
                        Certified Skill Domains (via HashSet&lt;String&gt;):
                    </span>
                </div>
                <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <%
                        if (skills != null && !skills.isEmpty()) {
                            for (String s : skills) {
                    %>
                        <span class="badge badge-active" style="font-size: 0.82rem; padding: 0.35rem 0.75rem; border: 1px solid rgba(99, 102, 241, 0.3);">
                            <i class="fa-solid fa-shield-halved" style="margin-right: 4px; font-size: 0.75rem;"></i> <%= s %>
                        </span>
                    <%
                            }
                        } else {
                    %>
                        <span style="color: var(--text-muted); font-size: 0.85rem;">No skills registered.</span>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- VOLUNTEER TABLE -->
        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-id-badge" style="color: #10b981; margin-right: 0.5rem;"></i>
                    Registered Responders Roster
                </div>
                <input type="text" id="volSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter responders..." onkeyup="filterTable('volSearch', 'volunteersTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="volunteersTable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Volunteer Name</th>
                            <th>Primary Skill</th>
                            <th>Experience</th>
                            <th>Staging Base</th>
                            <th>Contact</th>
                            <th>Duty Status</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (volunteers != null && !volunteers.isEmpty()) {
                                for (Volunteer v : volunteers) {
                                    String stClass = "badge-low";
                                    String iconStatus = "fa-circle-check";
                                    if ("ASSIGNED".equalsIgnoreCase(v.getAvailabilityStatus())) {
                                        stClass = "badge-active";
                                        iconStatus = "fa-person-walking";
                                    } else if ("OFF_DUTY".equalsIgnoreCase(v.getAvailabilityStatus())) {
                                        stClass = "badge-medium";
                                        iconStatus = "fa-moon";
                                    }
                        %>
                            <tr>
                                <td><span style="font-family: monospace; color: var(--text-muted); font-weight: 600;">#<%= v.getVolunteerId() %></span></td>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 0.6rem;">
                                        <div style="width: 32px; height: 32px; border-radius: 50%; background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(16, 185, 129, 0.2)); border: 1px solid rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.8rem; color: #fff;">
                                            <%= v.getName() != null && !v.getName().isEmpty() ? v.getName().substring(0, 1).toUpperCase() : "V" %>
                                        </div>
                                        <strong style="color: #fff;"><%= v.getName() %></strong>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge badge-active" style="display: inline-flex; align-items: center; gap: 4px;">
                                        <i class="fa-solid fa-tag" style="font-size: 0.7rem;"></i> <%= v.getSkill() %>
                                    </span>
                                </td>
                                <td>
                                    <i class="fa-solid fa-briefcase" style="color: var(--text-muted); font-size: 0.8rem; margin-right: 3px;"></i>
                                    <%= v.getExperienceYears() %> yrs
                                </td>
                                <td>
                                    <i class="fa-solid fa-location-dot" style="color: var(--danger); font-size: 0.8rem; margin-right: 3px;"></i>
                                    <%= v.getLocation() != null ? v.getLocation() : "HQ Basecamp" %>
                                </td>
                                <td>
                                    <i class="fa-solid fa-phone" style="color: var(--primary); font-size: 0.8rem; margin-right: 3px;"></i>
                                    <%= v.getContact() %>
                                </td>
                                <td>
                                    <span class="badge <%= stClass %>" style="display: inline-flex; align-items: center; gap: 4px;">
                                        <i class="fa-solid <%= iconStatus %>" style="font-size: 0.7rem;"></i>
                                        <%= v.getAvailabilityStatus() %>
                                    </span>
                                </td>
                                <td style="text-align: right;">
                                    <button class="btn btn-outline btn-sm"
                                            onclick="openVolStatusModal(<%= v.getVolunteerId() %>, '<%= v.getName() %>', '<%= v.getAvailabilityStatus() %>')">
                                        <i class="fa-solid fa-sliders"></i> Update
                                    </button>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No volunteers registered yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- REGISTER VOLUNTEER MODAL -->
    <div id="volunteerModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-user-plus" style="color: #10b981;"></i>
                    <h3 class="modal-title">Register Response Volunteer</h3>
                </div>
                <button class="modal-close" onclick="closeModal('volunteerModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/volunteers" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Full Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Rahul Sharma" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Primary Skill Domain</label>
                            <select name="skill" class="form-control" required>
                                <option value="FIRST_AID">FIRST_AID</option>
                                <option value="RESCUE">RESCUE</option>
                                <option value="DRIVING">DRIVING</option>
                                <option value="LOGISTICS">LOGISTICS</option>
                                <option value="MEDICAL">MEDICAL</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Experience (Years)</label>
                            <input type="number" name="experienceYears" class="form-control" value="2" required min="0">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Contact Phone</label>
                        <input type="text" name="contact" class="form-control" placeholder="+91 98765 00000" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Current Staging Location / Basecamp</label>
                        <input type="text" name="location" class="form-control" placeholder="e.g. Coastal Sector 4, Civic Hall" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('volunteerModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-check"></i> Save Volunteer
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- UPDATE VOLUNTEER STATUS MODAL -->
    <div id="volStatusModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-sliders" style="color: var(--primary);"></i>
                    <h3 class="modal-title">Update Volunteer Duty Status</h3>
                </div>
                <button class="modal-close" onclick="closeModal('volStatusModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/volunteers" method="POST">
                <input type="hidden" name="action" value="update_status">
                <input type="hidden" id="modalVolId" name="volunteerId">
                <div class="modal-body">
                    <p style="margin-bottom: 1.25rem; color: var(--text-secondary); background: rgba(0,0,0,0.25); padding: 0.75rem 1rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        Responder: <strong id="modalVolName" style="color: #fff; font-size: 1rem;"></strong>
                    </p>
                    <div class="form-group">
                        <label class="form-label">Availability Status</label>
                        <select id="modalVolStatusSelect" name="status" class="form-control" required>
                            <option value="AVAILABLE">AVAILABLE (Ready for immediate dispatch)</option>
                            <option value="ASSIGNED">ASSIGNED (On active mission)</option>
                            <option value="OFF_DUTY">OFF_DUTY (Rest / Rotation)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('volStatusModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-check"></i> Update Status
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <script>
        function openVolStatusModal(id, name, status) {
            document.getElementById('modalVolId').value = id;
            document.getElementById('modalVolName').innerText = name;
            document.getElementById('modalVolStatusSelect').value = status;
            openModal('volStatusModal');
        }
    </script>
</body>
</html>
