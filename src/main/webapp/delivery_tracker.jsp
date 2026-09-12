<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delivery Tracking - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-truck-fast" style="color: var(--accent-cyan);"></i>
                    <span>Live Dispatch & Delivery Tracking</span>
                </h1>
                <p>Monitor real-time transit milestones, driver telemetry, and shelter handover confirmations</p>
            </div>
            <div class="header-actions">
                <button class="btn btn-outline" onclick="window.location.reload();">
                    <i class="fa-solid fa-arrows-rotate"></i>
                    <span>Refresh Progress</span>
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Delivery dispatch milestone updated successfully.</span>
            </div>
        <% } %>

        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1.5rem; margin-bottom: 2rem;">
            <!-- ACTIVE SHIPMENTS LIST -->
            <div class="card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="fa-solid fa-route"></i>
                        <span>Active Supply Dispatches</span>
                    </div>
                </div>
                <div class="card-body">
                    <%
                        List<Allocation> list = (List<Allocation>) request.getAttribute("allocations");
                        if (list != null && !list.isEmpty()) {
                            for (Allocation a : list) {
                                DeliveryStatus ds = DeliveryStatus.fromString(a.getStatus());
                                int pct = ds.getProgressPercentage();
                                String progColor = "progress-blue";
                                if (pct == 100) progColor = "progress-emerald";
                                else if (pct == 50) progColor = "progress-amber";
                    %>
                        <div style="background-color: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.35rem; margin-bottom: 1.25rem; transition: border-color 0.2s ease;">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.85rem; flex-wrap: wrap; gap: 0.5rem;">
                                <div>
                                    <div style="font-size: 0.75rem; font-weight: 700; color: var(--accent-blue); letter-spacing: 0.05em; text-transform: uppercase;">
                                        DISPATCH #<%= a.getAllocationId() %>
                                    </div>
                                    <h3 style="font-size: 1.15rem; color: #fff; margin-top: 0.25rem; font-weight: 700;">
                                        <%= a.getAllocatedQuantity() %> <%= a.getUnit() %> <%= a.getResourceName() %>
                                    </h3>
                                    <p style="font-size: 0.85rem; color: var(--text-secondary); margin-top: 0.35rem;">
                                        <i class="fa-solid fa-location-dot" style="color: var(--accent-rose); margin-right: 0.35rem;"></i>Destination: <strong style="color: #fff;"><%= a.getShelterName() %></strong> (<%= a.getShelterLocation() %>)
                                    </p>
                                </div>
                                <span class="badge <%= pct == 100 ? "badge-low" : "badge-active" %>">
                                    <span class="pulse-dot"></span>
                                    <%= a.getStatus() %> (<%= pct %>%)
                                </span>
                            </div>

                            <!-- Progress Bar -->
                            <div class="progress-container" style="height: 10px; margin-bottom: 0.85rem;">
                                <div class="progress-bar <%= progColor %>" style="width: <%= pct %>%;"></div>
                            </div>

                            <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.825rem; color: var(--text-secondary); border-top: 1px solid var(--border-color); padding-top: 0.85rem; margin-top: 0.85rem; flex-wrap: wrap; gap: 0.5rem;">
                                <div style="display: flex; gap: 1rem; align-items: center;">
                                    <span>
                                        <i class="fa-solid fa-user-shield" style="color: var(--accent-emerald); margin-right: 0.35rem;"></i>
                                        <%= a.getVolunteerName() != null ? a.getVolunteerName() : "Unassigned" %>
                                    </span>
                                    <span>
                                        <i class="fa-solid fa-truck" style="color: var(--accent-blue); margin-right: 0.35rem;"></i>
                                        <%= a.getVehicleNumber() != null ? a.getVehicleNumber() + " (" + a.getVehicleType() + ")" : "None" %>
                                    </span>
                                </div>
                                <button class="btn btn-outline btn-sm"
                                        onclick="openDeliveryModal(<%= a.getAllocationId() %>, '<%= a.getStatus() %>', '<%= a.getTrackingNotes() != null ? a.getTrackingNotes().replace("'", "\\'") : "" %>')">
                                    <i class="fa-solid fa-pen-to-square"></i>
                                    <span>Update Status</span>
                                </button>
                            </div>

                            <% if (a.getTrackingNotes() != null && !a.getTrackingNotes().trim().isEmpty()) { %>
                                <div style="background-color: rgba(8, 13, 26, 0.7); border: 1px solid var(--border-color); padding: 0.65rem 0.85rem; border-radius: var(--radius-sm); font-size: 0.825rem; color: #cbd5e1; margin-top: 0.65rem; display: flex; align-items: flex-start; gap: 0.5rem;">
                                    <i class="fa-solid fa-radio" style="color: var(--accent-cyan); margin-top: 0.2rem;"></i>
                                    <span><%= a.getTrackingNotes() %></span>
                                </div>
                            <% } %>
                        </div>
                    <%
                            }
                        } else {
                    %>
                        <p style="text-align: center; color: var(--text-muted); padding: 2.5rem;">No delivery dispatches currently active.</p>
                    <% } %>
                </div>
            </div>

            <!-- LIVE TRANSIT AUDIT LOG -->
            <div class="card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="fa-solid fa-tower-cell" style="color: var(--accent-cyan);"></i>
                        <span>Dispatch Radio Log</span>
                    </div>
                </div>
                <div class="card-body" style="padding: 0.85rem;">
                    <div class="log-box" style="max-height: 520px;">
                        <%
                            List<String> deliveryLogs = (List<String>) request.getAttribute("deliveryLogs");
                            if (deliveryLogs != null && !deliveryLogs.isEmpty()) {
                                for (String l : deliveryLogs) {
                                    out.println(l);
                                }
                            } else {
                                out.println("[Delivery Service] Awaiting vehicle dispatches...");
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- UPDATE DELIVERY STATUS MODAL -->
    <div id="deliveryModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-truck-ramp-box" style="color: var(--accent-blue);"></i>
                    <span>Update Delivery Dispatch Status</span>
                </h3>
                <button class="modal-close" onclick="closeModal('deliveryModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/delivery" method="POST">
                <input type="hidden" id="modalAllocId" name="allocationId">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Delivery Milestone</label>
                        <select id="modalStatusSelect" name="status" class="form-control" required>
                            <option value="PREPARING">PREPARING (Depot Loading)</option>
                            <option value="DISPATCHED">DISPATCHED (Departed Base)</option>
                            <option value="IN_TRANSIT">IN_TRANSIT (En Route to Sector)</option>
                            <option value="DELIVERED">DELIVERED (Handover Complete & Verified)</option>
                            <option value="CANCELLED">CANCELLED</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Driver / Field Telemetry Notes</label>
                        <textarea id="modalNotesText" name="notes" class="form-control" rows="3" placeholder="Road condition, fuel checkpoint, ETA, shelter officer signature..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('deliveryModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">Update Delivery</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <script>
        function openDeliveryModal(allocId, currentStatus, currentNotes) {
            document.getElementById('modalAllocId').value = allocId;
            document.getElementById('modalStatusSelect').value = currentStatus;
            document.getElementById('modalNotesText').value = currentNotes;
            openModal('deliveryModal');
        }
    </script>
</body>
</html>
