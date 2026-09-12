<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.Shelter" %>
<%@ page import="com.disasterrelief.model.Disaster" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shelter Centers - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-house-chimney" style="color: var(--accent-blue);"></i>
                    <span>Shelter Operations & Evacuee Capacity</span>
                </h1>
                <p>Monitor safe zones, track current populations, and manage camp readiness</p>
            </div>
            <div class="header-actions">
                <button class="btn btn-primary" onclick="openModal('shelterModal')">
                    <i class="fa-solid fa-plus"></i>
                    <span>Add Shelter Center</span>
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Shelter operation updated successfully: <%= request.getParameter("msg") %></span>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-building-shield"></i>
                    <span>Registered Shelters & Evacuation Centers</span>
                </div>
                <input type="text" id="shelterSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter shelters..." onkeyup="filterTable('shelterSearch', 'sheltersTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="sheltersTable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Shelter Center</th>
                            <th>Incident Sector</th>
                            <th>Location</th>
                            <th>Evacuees / Capacity</th>
                            <th>Occupancy</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Shelter> shelters = (List<Shelter>) request.getAttribute("shelters");
                            if (shelters != null && !shelters.isEmpty()) {
                                for (Shelter s : shelters) {
                                    double rate = s.getOccupancyRate();
                                    String progColor = "progress-emerald";
                                    if (rate >= 90) progColor = "progress-rose";
                                    else if (rate >= 70) progColor = "progress-amber";
                        %>
                            <tr>
                                <td><strong>#<%= s.getShelterId() %></strong></td>
                                <td>
                                    <strong style="color: #fff;"><%= s.getName() %></strong>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <i class="fa-solid fa-phone" style="margin-right: 0.25rem;"></i><%= s.getContact() %>
                                    </div>
                                </td>
                                <td><%= s.getDisasterName() != null ? s.getDisasterName() : "General Safe Zone" %></td>
                                <td><i class="fa-solid fa-location-dot" style="color: var(--accent-blue); margin-right: 0.35rem;"></i><%= s.getLocation() %></td>
                                <td>
                                    <strong style="color: #fff;"><%= s.getCurrentPopulation() %></strong> / <%= s.getCapacity() %>
                                    <div class="progress-container" style="margin-top: 0.4rem;">
                                        <div class="progress-bar <%= progColor %>" style="width: <%= Math.min(100, (int)rate) %>%;"></div>
                                    </div>
                                </td>
                                <td>
                                    <strong style="color: <%= rate >= 90 ? "var(--badge-critical)" : "var(--accent-emerald)" %>;">
                                        <%= String.format("%.1f%%", rate) %>
                                    </strong>
                                </td>
                                <td>
                                    <span class="badge <%= "OPERATIONAL".equalsIgnoreCase(s.getStatus()) ? "badge-low" : "badge-critical" %>">
                                        <span class="pulse-dot"></span>
                                        <%= s.getStatus() %>
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-outline btn-sm"
                                            onclick="openUpdatePopModal(<%= s.getShelterId() %>, <%= s.getCurrentPopulation() %>, '<%= s.getStatus() %>')">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                        <span>Update Pop</span>
                                    </button>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No shelter centers registered yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- ADD SHELTER MODAL -->
    <div id="shelterModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-house-medical" style="color: var(--accent-blue);"></i>
                    <span>Register Relief Shelter Center</span>
                </h3>
                <button class="modal-close" onclick="closeModal('shelterModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/shelters" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Shelter Center Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Hillside High School Relief Camp" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Associated Disaster Incident</label>
                        <select name="disasterId" class="form-control">
                            <option value="">None / Independent Center</option>
                            <%
                                List<Disaster> disasters = (List<Disaster>) request.getAttribute("disasters");
                                if (disasters != null) {
                                    for (Disaster d : disasters) {
                            %>
                                <option value="<%= d.getDisasterId() %>"><%= d.getName() %> (<%= d.getSeverity() %>)</option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Physical Address / GPS Sector</label>
                        <input type="text" name="location" class="form-control" placeholder="e.g. Ridge Road Mile 14" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Total Bed Capacity</label>
                            <input type="number" name="capacity" class="form-control" value="500" required min="1">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Initial Population</label>
                            <input type="number" name="currentPopulation" class="form-control" value="0" required min="0">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Emergency Contact Phone</label>
                        <input type="text" name="contact" class="form-control" placeholder="+91 98765 00000" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('shelterModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">Register Shelter</button>
                </div>
            </form>
        </div>
    </div>

    <!-- UPDATE POPULATION MODAL -->
    <div id="updatePopModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-users" style="color: var(--accent-emerald);"></i>
                    <span>Update Shelter Population</span>
                </h3>
                <button class="modal-close" onclick="closeModal('updatePopModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/shelters" method="POST">
                <input type="hidden" name="action" value="update_population">
                <input type="hidden" id="modalShelterId" name="shelterId">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Current Evacuee Count</label>
                        <input type="number" id="modalPopInput" name="currentPopulation" class="form-control" required min="0">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Operational Status</label>
                        <select id="modalStatusSelect" name="status" class="form-control">
                            <option value="OPERATIONAL">OPERATIONAL</option>
                            <option value="AT_CAPACITY">AT_CAPACITY</option>
                            <option value="EVACUATING">EVACUATING</option>
                            <option value="CLOSED">CLOSED</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('updatePopModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <script>
        function openUpdatePopModal(shelterId, currentPop, status) {
            document.getElementById('modalShelterId').value = shelterId;
            document.getElementById('modalPopInput').value = currentPop;
            document.getElementById('modalStatusSelect').value = status;
            openModal('updatePopModal');
        }
    </script>
</body>
</html>
