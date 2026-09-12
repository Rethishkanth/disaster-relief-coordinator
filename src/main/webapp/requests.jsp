<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.ResourceRequest" %>
<%@ page import="com.disasterrelief.model.Shelter" %>
<%@ page import="com.disasterrelief.model.ResourceType" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resource Requests - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-clipboard-list" style="color: var(--accent-amber);"></i>
                    <span>Shelter Resource Requests</span>
                </h1>
                <p>Prioritized demand queue ordered via PriorityQueue&lt;ResourceRequest&gt; multi-factor algorithm</p>
            </div>
            <div class="header-actions">
                <button class="btn btn-danger" onclick="openModal('requestModal')">
                    <i class="fa-solid fa-plus"></i>
                    <span>Submit Emergency Request</span>
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Emergency request registered and prioritized: <%= request.getParameter("msg") %></span>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-ranking-star"></i>
                    <span>All Requests & Priority Queue Rankings</span>
                </div>
                <input type="text" id="requestSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter requests..." onkeyup="filterTable('requestSearch', 'requestsTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="requestsTable">
                    <thead>
                        <tr>
                            <th>Rank / Score</th>
                            <th>Shelter Center</th>
                            <th>Category</th>
                            <th>Quantity Needed</th>
                            <th>Urgency</th>
                            <th>Evacuees Affected</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<ResourceRequest> requests = (List<ResourceRequest>) request.getAttribute("allRequests");
                            if (requests != null && !requests.isEmpty()) {
                                for (ResourceRequest reqItem : requests) {
                                    String uClass = "badge-medium";
                                    if ("CRITICAL".equalsIgnoreCase(reqItem.getUrgency())) uClass = "badge-critical";
                                    else if ("HIGH".equalsIgnoreCase(reqItem.getUrgency())) uClass = "badge-high";
                                    else if ("LOW".equalsIgnoreCase(reqItem.getUrgency())) uClass = "badge-low";

                                    String sClass = "badge-medium";
                                    if ("DELIVERED".equalsIgnoreCase(reqItem.getStatus())) sClass = "badge-low";
                                    else if ("ALLOCATED".equalsIgnoreCase(reqItem.getStatus()) || "DISPATCHED".equalsIgnoreCase(reqItem.getStatus())) sClass = "badge-active";
                        %>
                            <tr>
                                <td>
                                    <strong style="color: var(--accent-amber); font-size: 1.1rem;">
                                        <%= String.format("%.1f", reqItem.getPriorityScore()) %>
                                    </strong>
                                </td>
                                <td>
                                    <strong style="color: #fff;"><%= reqItem.getShelterName() %></strong>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <i class="fa-solid fa-location-dot" style="margin-right: 0.25rem;"></i><%= reqItem.getShelterLocation() %>
                                    </div>
                                </td>
                                <td><span class="badge badge-active"><%= reqItem.getResourceType() %></span></td>
                                <td>
                                    <strong style="color: #fff;"><%= reqItem.getQuantity() %></strong> <%= reqItem.getUnit() %>
                                </td>
                                <td>
                                    <span class="badge <%= uClass %>">
                                        <span class="pulse-dot"></span>
                                        <%= reqItem.getUrgency() %>
                                    </span>
                                </td>
                                <td><%= reqItem.getPeopleAffected() %> evacuees</td>
                                <td><span class="badge <%= sClass %>"><%= reqItem.getStatus() %></span></td>
                                <td>
                                    <% if ("PENDING".equalsIgnoreCase(reqItem.getStatus())) { %>
                                        <a href="<%= request.getContextPath() %>/allocations?prefillReq=<%= reqItem.getRequestId() %>" class="btn btn-primary btn-sm">
                                            <i class="fa-solid fa-bullseye"></i>
                                            <span>Allocate</span>
                                        </a>
                                    <% } else { %>
                                        <span style="font-size: 0.8rem; color: var(--text-muted); font-weight: 600;">Assigned</span>
                                    <% } %>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No resource requests found.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- SUBMIT EMERGENCY REQUEST MODAL -->
    <div id="requestModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-triangle-exclamation" style="color: var(--accent-rose);"></i>
                    <span>Submit Shelter Emergency Request</span>
                </h3>
                <button class="modal-close" onclick="closeModal('requestModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/requests" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Requesting Shelter Center</label>
                        <select name="shelterId" class="form-control" required>
                            <%
                                List<Shelter> shelters = (List<Shelter>) request.getAttribute("shelters");
                                if (shelters != null) {
                                    for (Shelter s : shelters) {
                            %>
                                <option value="<%= s.getShelterId() %>"><%= s.getName() %> (Pop: <%= s.getCurrentPopulation() %>)</option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Supplies Category</label>
                            <select name="resourceType" class="form-control" required>
                                <option value="WATER">WATER</option>
                                <option value="FOOD">FOOD</option>
                                <option value="MEDICINE">MEDICINE</option>
                                <option value="SHELTER_KIT">SHELTER_KIT</option>
                                <option value="CLOTHING">CLOTHING</option>
                                <option value="RESCUE_GEAR">RESCUE_GEAR</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Quantity Needed</label>
                            <input type="number" name="quantity" class="form-control" value="500" required min="1">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Packaging Unit</label>
                            <input type="text" name="unit" class="form-control" placeholder="e.g. Bottles, Meal Packs, Kits" value="Bottles" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Emergency Urgency</label>
                            <select name="urgency" class="form-control" required>
                                <option value="CRITICAL">CRITICAL (Immediate Life Threat)</option>
                                <option value="HIGH" selected>HIGH (Severe Shortage)</option>
                                <option value="MEDIUM">MEDIUM (Anticipated Deficit)</option>
                                <option value="LOW">LOW (Routine Restock)</option>
                            </select>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Evacuees Affected</label>
                            <input type="number" name="peopleAffected" class="form-control" value="450" required min="1">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Required Delivery Timeline</label>
                            <input type="text" name="requiredDate" class="form-control" placeholder="e.g. Within 4 hours, Today 18:00" value="Immediate">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Operational Request Notes</label>
                        <textarea name="notes" class="form-control" rows="2" placeholder="Specify water contamination, injured counts, road conditions..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('requestModal')">Cancel</button>
                    <button type="submit" class="btn btn-danger">Submit to Priority Queue</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
