<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.Disaster" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disaster Incidents - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-triangle-exclamation" style="color: var(--accent-rose);"></i>
                    <span>Disaster Incident Management</span>
                </h1>
                <p>Register, categorize, and monitor active catastrophic events across sectors</p>
            </div>
            <div class="header-actions">
                <button class="btn btn-danger" onclick="openModal('disasterModal')">
                    <i class="fa-solid fa-plus"></i>
                    <span>Register Incident</span>
                </button>
            </div>
        </div>

        <% if ("created".equals(request.getParameter("msg"))) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>New disaster incident successfully registered and operationalized.</span>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-list-check"></i>
                    <span>Active Disasters Roster</span>
                </div>
                <input type="text" id="disasterSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter incidents..." onkeyup="filterTable('disasterSearch', 'disastersTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="disastersTable">
                    <thead>
                        <tr>
                            <th>Incident ID</th>
                            <th>Disaster Name</th>
                            <th>Type</th>
                            <th>Geographic Location</th>
                            <th>Severity</th>
                            <th>Status</th>
                            <th>Operational Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Disaster> disasters = (List<Disaster>) request.getAttribute("disasters");
                            if (disasters != null && !disasters.isEmpty()) {
                                for (Disaster d : disasters) {
                                    String sevClass = "badge-medium";
                                    if ("CRITICAL".equalsIgnoreCase(d.getSeverity())) sevClass = "badge-critical";
                                    else if ("HIGH".equalsIgnoreCase(d.getSeverity())) sevClass = "badge-high";
                                    else if ("LOW".equalsIgnoreCase(d.getSeverity())) sevClass = "badge-low";
                        %>
                            <tr>
                                <td><strong>#<%= d.getDisasterId() %></strong></td>
                                <td><strong style="color: #fff;"><%= d.getName() %></strong></td>
                                <td><span class="badge badge-active"><%= d.getDisasterType() %></span></td>
                                <td><i class="fa-solid fa-location-dot" style="color: var(--accent-blue); margin-right: 0.35rem;"></i><%= d.getLocation() %></td>
                                <td>
                                    <span class="badge <%= sevClass %>">
                                        <span class="pulse-dot"></span>
                                        <%= d.getSeverity() %>
                                    </span>
                                </td>
                                <td>
                                    <span class="badge <%= "ACTIVE".equalsIgnoreCase(d.getStatus()) ? "badge-critical" : "badge-low" %>">
                                        <%= d.getStatus() %>
                                    </span>
                                </td>
                                <td style="max-width: 380px; font-size: 0.825rem; color: var(--text-secondary); line-height: 1.4;"><%= d.getDescription() %></td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="7" style="text-align: center; color: var(--text-muted); padding: 2rem;">No disaster incidents currently active.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- REGISTER DISASTER MODAL -->
    <div id="disasterModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-triangle-exclamation" style="color: var(--accent-rose);"></i>
                    <span>Register New Disaster Incident</span>
                </h3>
                <button class="modal-close" onclick="closeModal('disasterModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/disasters" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Incident / Disaster Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Cyclone Vardah, Flash Floods Sector B" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Disaster Type</label>
                            <select name="disasterType" class="form-control" required>
                                <option value="FLOOD">Flood</option>
                                <option value="CYCLONE">Cyclone / Storm</option>
                                <option value="EARTHQUAKE">Earthquake</option>
                                <option value="LANDSLIDE">Landslide</option>
                                <option value="TSUNAMI">Tsunami</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Severity Level</label>
                            <select name="severity" class="form-control" required>
                                <option value="CRITICAL">CRITICAL (+25 Priority pts)</option>
                                <option value="HIGH">HIGH (+15 Priority pts)</option>
                                <option value="MEDIUM" selected>MEDIUM (+5 Priority pts)</option>
                                <option value="LOW">LOW (0 pts)</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Affected Location / Coordinates</label>
                        <input type="text" name="location" class="form-control" placeholder="e.g. Coastal Sector 4, Riverbank Zone" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Situation Description</label>
                        <textarea name="description" class="form-control" rows="3" placeholder="Provide damage context, inundation levels, casualty reports..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('disasterModal')">Cancel</button>
                    <button type="submit" class="btn btn-danger">Register Disaster</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
