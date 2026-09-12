<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Operations Command Center - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-tower-broadcast" style="color: var(--accent-rose);"></i>
                    <span>Emergency Operations Command Center</span>
                </h1>
                <p>Unified Real-Time Disaster Coordination, Resource Logistics & Priority Dispatch</p>
            </div>
            <div class="header-actions">
                <form action="<%= request.getContextPath() %>/allocations" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="auto_allocate">
                    <button type="submit" class="btn btn-primary" title="Trigger PriorityQueue matching algorithm">
                        <i class="fa-solid fa-bolt"></i>
                        <span>Auto-Allocate Top Request</span>
                    </button>
                </form>
                <a href="<%= request.getContextPath() %>/requests" class="btn btn-danger">
                    <i class="fa-solid fa-plus"></i>
                    <span>New Emergency Request</span>
                </a>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Operation completed successfully: <%= request.getParameter("msg") %></span>
            </div>
        <% } %>

        <!-- TOP METRICS STATS GRID -->
        <div class="stat-grid">
            <div class="stat-card" style="border-top: 3px solid var(--accent-rose);">
                <div class="stat-header">
                    <span class="stat-title">Active Disasters</span>
                    <div class="stat-icon-wrapper stat-icon-rose">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                    </div>
                </div>
                <div class="stat-value">
                    <%= request.getAttribute("disasters") != null ? ((List<?>)request.getAttribute("disasters")).size() : 0 %>
                </div>
                <div class="stat-subtitle">Catastrophic emergency sectors</div>
            </div>

            <div class="stat-card" style="border-top: 3px solid var(--accent-blue);">
                <div class="stat-header">
                    <span class="stat-title">Evacuees Sheltered</span>
                    <div class="stat-icon-wrapper stat-icon-blue">
                        <i class="fa-solid fa-people-roof"></i>
                    </div>
                </div>
                <div class="stat-value">
                    <%= request.getAttribute("totalSheltered") != null ? request.getAttribute("totalSheltered") : 0 %>
                </div>
                <div class="stat-subtitle">Across <%= request.getAttribute("shelters") != null ? ((List<?>)request.getAttribute("shelters")).size() : 0 %> operational safe zones</div>
            </div>

            <div class="stat-card" style="border-top: 3px solid var(--accent-amber);">
                <div class="stat-header">
                    <span class="stat-title">Pending in PriorityQueue</span>
                    <div class="stat-icon-wrapper stat-icon-amber">
                        <i class="fa-solid fa-hourglass-half"></i>
                    </div>
                </div>
                <div class="stat-value">
                    <%= request.getAttribute("pendingRequests") != null ? ((List<?>)request.getAttribute("pendingRequests")).size() : 0 %>
                </div>
                <div class="stat-subtitle">Awaiting algorithmic supply matching</div>
            </div>

            <div class="stat-card" style="border-top: 3px solid var(--accent-cyan);">
                <div class="stat-header">
                    <span class="stat-title">In-Transit Deliveries</span>
                    <div class="stat-icon-wrapper stat-icon-cyan">
                        <i class="fa-solid fa-truck-fast"></i>
                    </div>
                </div>
                <div class="stat-value">
                    <%= request.getAttribute("activeDeliveriesCount") != null ? request.getAttribute("activeDeliveriesCount") : 0 %>
                </div>
                <div class="stat-subtitle">Vehicles & responders en route</div>
            </div>

            <div class="stat-card" style="border-top: 3px solid var(--accent-emerald);">
                <div class="stat-header">
                    <span class="stat-title">Warehouse Stock</span>
                    <div class="stat-icon-wrapper stat-icon-emerald">
                        <i class="fa-solid fa-boxes-stacked"></i>
                    </div>
                </div>
                <div class="stat-value">
                    <%= request.getAttribute("totalSuppliesCount") != null ? request.getAttribute("totalSuppliesCount") : 0 %>
                </div>
                <div class="stat-subtitle">Available units in inventory</div>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1.5rem; margin-bottom: 2rem;">
            <!-- PRIORITY QUEUE PENDING REQUESTS -->
            <div class="card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="fa-solid fa-fire" style="color: var(--accent-rose);"></i>
                        <span>Urgent Demands (PriorityQueue Ranked)</span>
                    </div>
                    <a href="<%= request.getContextPath() %>/requests" class="btn btn-outline btn-sm">View All Requests</a>
                </div>
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Priority Score</th>
                                <th>Shelter Center</th>
                                <th>Required Supplies</th>
                                <th>Evacuees</th>
                                <th>Urgency</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<ResourceRequest> pending = (List<ResourceRequest>) request.getAttribute("pendingRequests");
                                if (pending != null && !pending.isEmpty()) {
                                    for (ResourceRequest reqItem : pending) {
                                        String badgeClass = "badge-medium";
                                        if ("CRITICAL".equalsIgnoreCase(reqItem.getUrgency())) badgeClass = "badge-critical";
                                        else if ("HIGH".equalsIgnoreCase(reqItem.getUrgency())) badgeClass = "badge-high";
                                        else if ("LOW".equalsIgnoreCase(reqItem.getUrgency())) badgeClass = "badge-low";
                            %>
                                <tr>
                                    <td>
                                        <strong style="color: var(--accent-amber); font-size: 1.05rem;">
                                            <%= String.format("%.1f", reqItem.getPriorityScore()) %>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong><%= reqItem.getShelterName() %></strong>
                                        <div style="font-size: 0.75rem; color: var(--text-muted);"><%= reqItem.getShelterLocation() %></div>
                                    </td>
                                    <td>
                                        <span style="font-weight: 600;"><%= reqItem.getQuantity() %> <%= reqItem.getUnit() %></span>
                                        <span style="color: var(--text-secondary); font-size: 0.8rem;">(<%= reqItem.getResourceType() %>)</span>
                                    </td>
                                    <td><%= reqItem.getPeopleAffected() %></td>
                                    <td>
                                        <span class="badge <%= badgeClass %>">
                                            <span class="pulse-dot"></span>
                                            <%= reqItem.getUrgency() %>
                                        </span>
                                    </td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/allocations?prefillReq=<%= reqItem.getRequestId() %>" class="btn btn-primary btn-sm">
                                            <i class="fa-solid fa-bullseye"></i>
                                            <span>Allocate</span>
                                        </a>
                                    </td>
                                </tr>
                            <%
                                    }
                                } else {
                            %>
                                <tr>
                                    <td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2.5rem;">
                                        <i class="fa-solid fa-clipboard-check" style="font-size: 2rem; color: var(--accent-emerald); display: block; margin-bottom: 0.5rem;"></i>
                                        No pending resource requests in the PriorityQueue. All demands satisfied!
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- SYSTEM STATUS & ENGINE CARD -->
            <div class="card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="fa-solid fa-server"></i>
                        <span>System Architecture Status</span>
                    </div>
                </div>
                <div class="card-body">
                    <div style="margin-bottom: 1.25rem;">
                        <span class="form-label">Database Tier Mode</span>
                        <div style="display: flex; align-items: center; gap: 0.5rem;">
                            <span class="badge badge-active">
                                <span class="pulse-dot"></span>
                                Active
                            </span>
                            <strong style="font-size: 0.85rem; color: #fff;"><%= request.getAttribute("databaseMode") %></strong>
                        </div>
                    </div>

                    <div style="margin-bottom: 1.25rem;">
                        <span class="form-label">Multithreading Worker Engine</span>
                        <div style="display: flex; align-items: center; gap: 0.5rem;">
                            <span class="badge badge-low">
                                <span class="pulse-dot"></span>
                                Running
                            </span>
                            <span style="font-size: 0.85rem; color: var(--text-secondary);">4 Background Threads (Allocation, Tracking, Logging, Snapshot)</span>
                        </div>
                    </div>

                    <div style="margin-bottom: 1.25rem;">
                        <span class="form-label">Fleet & Volunteer Readiness</span>
                        <p style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.8;">
                            <i class="fa-solid fa-truck" style="color: var(--accent-blue);"></i> Available Fleet: <strong style="color: #fff;"><%= request.getAttribute("availableVehiclesCount") %></strong><br>
                            <i class="fa-solid fa-user-shield" style="color: var(--accent-emerald);"></i> Available Responders: <strong style="color: #fff;"><%= request.getAttribute("availableVolunteersCount") %></strong>
                        </p>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 0.6rem; margin-top: 1.5rem;">
                        <a href="<%= request.getContextPath() %>/reports" class="btn btn-outline btn-sm" style="width: 100%;">
                            <i class="fa-solid fa-file-export"></i>
                            <span>Generate Audit Reports (CSV/TXT)</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/backups" class="btn btn-outline btn-sm" style="width: 100%;">
                            <i class="fa-solid fa-floppy-disk"></i>
                            <span>State Serialization Snapshots (.ser)</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- RECENT ACTIVITY LOG STREAM -->
        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-terminal" style="color: var(--accent-emerald);"></i>
                    <span>Live Operational Activity Stream (FileLogger)</span>
                </div>
                <a href="<%= request.getContextPath() %>/reports" class="btn btn-outline btn-sm">View Full Logs</a>
            </div>
            <div class="card-body" style="padding: 0.85rem;">
                <div class="log-box">
                    <%
                        List<String> logs = (List<String>) request.getAttribute("recentLogs");
                        if (logs != null && !logs.isEmpty()) {
                            for (String logLine : logs) {
                                out.println(logLine);
                            }
                        } else {
                            out.println("[System Activity] All coordinator modules operational.");
                        }
                    %>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        Disaster Relief Resource Coordinator &bull; B.Tech Major Project &bull; 3-Tier Enterprise Architecture
    </footer>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
