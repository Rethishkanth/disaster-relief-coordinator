<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resource Allocations - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-bullseye" style="color: var(--accent-blue);"></i>
                    <span>Resource Allocations & Dispatch Management</span>
                </h1>
                <p>Thread-safe inventory matching, driver assignment, and transport coordination</p>
            </div>
            <div class="header-actions">
                <form action="<%= request.getContextPath() %>/allocations" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="auto_allocate">
                    <button type="submit" class="btn btn-primary" title="Algorithmic match from PriorityQueue">
                        <i class="fa-solid fa-bolt"></i>
                        <span>Auto-Allocate Top Request</span>
                    </button>
                </form>
                <button class="btn btn-danger" onclick="openModal('allocateModal')">
                    <i class="fa-solid fa-plus"></i>
                    <span>Manual Allocation</span>
                </button>
            </div>
        </div>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i>
                <span><%= request.getAttribute("errorMessage") %></span>
            </div>
        <% } %>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Allocation operation successful: <%= request.getParameter("msg") %></span>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-list-check"></i>
                    <span>Completed & Active Allocations Roster</span>
                </div>
                <input type="text" id="allocSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter allocations..." onkeyup="filterTable('allocSearch', 'allocationsTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="allocationsTable">
                    <thead>
                        <tr>
                            <th>Alloc ID</th>
                            <th>Destination Shelter</th>
                            <th>Allocated Resource</th>
                            <th>Quantity</th>
                            <th>Assigned Volunteer</th>
                            <th>Dispatch Vehicle</th>
                            <th>Status</th>
                            <th>Tracking Notes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Allocation> allocations = (List<Allocation>) request.getAttribute("allocations");
                            if (allocations != null && !allocations.isEmpty()) {
                                for (Allocation a : allocations) {
                                    String stClass = "badge-medium";
                                    if ("DELIVERED".equalsIgnoreCase(a.getStatus())) stClass = "badge-low";
                                    else if ("IN_TRANSIT".equalsIgnoreCase(a.getStatus()) || "DISPATCHED".equalsIgnoreCase(a.getStatus())) stClass = "badge-active";
                        %>
                            <tr>
                                <td><strong>#<%= a.getAllocationId() %></strong></td>
                                <td>
                                    <strong style="color: #fff;"><%= a.getShelterName() %></strong>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <i class="fa-solid fa-location-dot" style="margin-right: 0.25rem;"></i><%= a.getShelterLocation() %>
                                    </div>
                                </td>
                                <td>
                                    <strong><%= a.getResourceName() %></strong>
                                    <span style="font-size: 0.8rem; color: var(--text-secondary);">(<%= a.getResourceType() %>)</span>
                                </td>
                                <td>
                                    <strong style="color: #fff;"><%= a.getAllocatedQuantity() %></strong> <%= a.getUnit() %>
                                </td>
                                <td>
                                    <%= a.getVolunteerName() != null ? "<i class='fa-solid fa-user-shield' style='color:var(--accent-emerald); margin-right:0.3rem;'></i>" + a.getVolunteerName() : "<span style='color:var(--text-muted)'>Unassigned</span>" %>
                                </td>
                                <td>
                                    <%= a.getVehicleNumber() != null ? "<i class='fa-solid fa-truck' style='color:var(--accent-blue); margin-right:0.3rem;'></i>" + a.getVehicleNumber() + " (" + a.getVehicleType() + ")" : "<span style='color:var(--text-muted)'>None</span>" %>
                                </td>
                                <td>
                                    <span class="badge <%= stClass %>">
                                        <span class="pulse-dot"></span>
                                        <%= a.getStatus() %>
                                    </span>
                                </td>
                                <td style="max-width: 300px; font-size: 0.825rem; color: var(--text-secondary); line-height: 1.4;">
                                    <%= a.getTrackingNotes() != null ? a.getTrackingNotes() : "&mdash;" %>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No allocations registered yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- MANUAL ALLOCATE MODAL -->
    <div id="allocateModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-boxes-packing" style="color: var(--accent-blue);"></i>
                    <span>Manual Resource Allocation</span>
                </h3>
                <button class="modal-close" onclick="closeModal('allocateModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/allocations" method="POST">
                <input type="hidden" name="action" value="allocate">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Select Pending Resource Request</label>
                        <select name="requestId" class="form-control" required>
                            <%
                                List<ResourceRequest> reqList = (List<ResourceRequest>) request.getAttribute("pendingRequests");
                                String prefill = request.getParameter("prefillReq");
                                if (reqList != null && !reqList.isEmpty()) {
                                    for (ResourceRequest r : reqList) {
                                        boolean selected = prefill != null && prefill.equals(String.valueOf(r.getRequestId()));
                            %>
                                <option value="<%= r.getRequestId() %>" <%= selected ? "selected" : "" %>>
                                    #<%= r.getRequestId() %> - <%= r.getShelterName() %>: <%= r.getQuantity() %> <%= r.getUnit() %> <%= r.getResourceType() %> [Score: <%= r.getPriorityScore() %>]
                                </option>
                            <%
                                    }
                                } else {
                            %>
                                <option value="">No pending requests available</option>
                            <% } %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Select Warehouse Resource to Deduct</label>
                        <select name="resourceId" class="form-control" required>
                            <%
                                List<Resource> resList = (List<Resource>) request.getAttribute("resources");
                                if (resList != null) {
                                    for (Resource res : resList) {
                                        if (res.getQuantity() > 0) {
                            %>
                                <option value="<%= res.getResourceId() %>">
                                    #<%= res.getResourceId() %> - <%= res.getResourceName() %> (Avail: <%= res.getQuantity() %> <%= res.getUnit() %>) [<%= res.getResourceType() %>]
                                </option>
                            <%
                                        }
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Quantity to Allocate</label>
                        <input type="number" name="quantity" class="form-control" value="100" required min="1">
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Assign Available Volunteer</label>
                            <select name="volunteerId" class="form-control">
                                <option value="">None / Unassigned</option>
                                <%
                                    List<Volunteer> volList = (List<Volunteer>) request.getAttribute("availableVolunteers");
                                    if (volList != null) {
                                        for (Volunteer v : volList) {
                                %>
                                    <option value="<%= v.getVolunteerId() %>"><%= v.getName() %> (<%= v.getSkill() %>)</option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Assign Available Vehicle</label>
                            <select name="vehicleId" class="form-control">
                                <option value="">None / Hand-Carry</option>
                                <%
                                    List<Vehicle> vehList = (List<Vehicle>) request.getAttribute("availableVehicles");
                                    if (vehList != null) {
                                        for (Vehicle veh : vehList) {
                                %>
                                    <option value="<%= veh.getVehicleId() %>"><%= veh.getVehicleNumber() %> (<%= veh.getVehicleType() %>, <%= veh.getCapacityKg() %>kg)</option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('allocateModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">Confirm Allocation</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <% if (request.getParameter("prefillReq") != null) { %>
        <script>openModal('allocateModal');</script>
    <% } %>
</body>
</html>
