<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.Vehicle" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vehicles Fleet - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="header-icon-badge" style="background: rgba(59, 130, 246, 0.15); color: #60a5fa;">
                        <i class="fa-solid fa-truck-moving fa-lg"></i>
                    </div>
                    <div>
                        <h1>Transport Fleet & Dispatch Vehicles</h1>
                        <p>Track all-terrain trucks, fast response vans, rescue boats, and airlift units</p>
                    </div>
                </div>
            </div>
            <div class="header-actions">
                <button class="btn btn-primary" onclick="openModal('vehicleModal')">
                    <i class="fa-solid fa-plus"></i> Register Vehicle
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Vehicle fleet updated successfully.</span>
            </div>
        <% } %>

        <%
            List<Vehicle> vehicles = (List<Vehicle>) request.getAttribute("vehicles");
            int totalVeh = (vehicles != null) ? vehicles.size() : 0;
            int availVeh = 0;
            int inTransitVeh = 0;
            int maintVeh = 0;
            int totalCapacityKg = 0;
            if (vehicles != null) {
                for (Vehicle v : vehicles) {
                    totalCapacityKg += v.getCapacityKg();
                    if ("AVAILABLE".equalsIgnoreCase(v.getAvailabilityStatus())) availVeh++;
                    else if ("IN_TRANSIT".equalsIgnoreCase(v.getAvailabilityStatus())) inTransitVeh++;
                    else if ("MAINTENANCE".equalsIgnoreCase(v.getAvailabilityStatus())) maintVeh++;
                }
            }
        %>

        <!-- FLEET METRICS CARDS -->
        <div class="stats-grid" style="margin-bottom: 1.5rem;">
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-truck"></i> Total Fleet Units</span>
                <div class="stat-value"><%= totalVeh %></div>
                <div class="stat-meta">Active multi-terrain units</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-circle-check" style="color: #10b981;"></i> Staging Bay Ready</span>
                <div class="stat-value" style="color: #10b981;"><%= availVeh %></div>
                <div class="stat-meta">Available for dispatch</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-route" style="color: #60a5fa;"></i> In Transit / Mission</span>
                <div class="stat-value" style="color: #60a5fa;"><%= inTransitVeh %></div>
                <div class="stat-meta">On delivery routes</div>
            </div>
            <div class="stat-card">
                <span class="stat-title"><i class="fa-solid fa-weight-hanging" style="color: #f59e0b;"></i> Total Cargo Payload</span>
                <div class="stat-value" style="color: #f59e0b;"><%= String.format("%,d", totalCapacityKg) %> <span style="font-size: 0.9rem; font-weight: 500;">kg</span></div>
                <div class="stat-meta">Combined fleet capacity</div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-list-check" style="color: #60a5fa; margin-right: 0.5rem;"></i>
                    Fleet Vehicles Roster
                </div>
                <input type="text" id="vehSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter vehicles..." onkeyup="filterTable('vehSearch', 'vehiclesTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="vehiclesTable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Registration / Call-Sign</th>
                            <th>Vehicle Type</th>
                            <th>Payload Capacity</th>
                            <th>Designated Driver</th>
                            <th>Contact Phone</th>
                            <th>Fleet Status</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (vehicles != null && !vehicles.isEmpty()) {
                                for (Vehicle veh : vehicles) {
                                    String stClass = "badge-low";
                                    String stIcon = "fa-circle-check";
                                    if ("IN_TRANSIT".equalsIgnoreCase(veh.getAvailabilityStatus())) {
                                        stClass = "badge-active";
                                        stIcon = "fa-truck-fast";
                                    } else if ("MAINTENANCE".equalsIgnoreCase(veh.getAvailabilityStatus())) {
                                        stClass = "badge-critical";
                                        stIcon = "fa-wrench";
                                    }

                                    String typeIcon = "fa-truck";
                                    if ("VAN".equalsIgnoreCase(veh.getVehicleType())) typeIcon = "fa-van-shuttle";
                                    else if ("BOAT".equalsIgnoreCase(veh.getVehicleType())) typeIcon = "fa-ship";
                                    else if ("HELICOPTER".equalsIgnoreCase(veh.getVehicleType())) typeIcon = "fa-helicopter";
                                    else if ("AMBULANCE".equalsIgnoreCase(veh.getVehicleType())) typeIcon = "fa-truck-medical";
                        %>
                            <tr>
                                <td><span style="font-family: monospace; color: var(--text-muted); font-weight: 600;">#<%= veh.getVehicleId() %></span></td>
                                <td>
                                    <strong style="color: #fff; font-family: monospace; font-size: 0.95rem;"><%= veh.getVehicleNumber() %></strong>
                                </td>
                                <td>
                                    <span class="badge badge-active" style="display: inline-flex; align-items: center; gap: 5px;">
                                        <i class="fa-solid <%= typeIcon %>"></i> <%= veh.getVehicleType() %>
                                    </span>
                                </td>
                                <td>
                                    <i class="fa-solid fa-weight-scale" style="color: var(--text-muted); font-size: 0.8rem; margin-right: 4px;"></i>
                                    <strong><%= veh.getCapacityKg() %></strong> kg
                                </td>
                                <td>
                                    <% if (veh.getDriverName() != null && !veh.getDriverName().isEmpty()) { %>
                                        <span style="display: inline-flex; align-items: center; gap: 5px; color: #fff;">
                                            <i class="fa-solid fa-id-badge" style="color: var(--accent);"></i> <%= veh.getDriverName() %>
                                        </span>
                                    <% } else { %>
                                        <span style="color: var(--text-muted);">Unassigned</span>
                                    <% } %>
                                </td>
                                <td>
                                    <i class="fa-solid fa-phone" style="color: var(--primary); font-size: 0.8rem; margin-right: 4px;"></i>
                                    <%= veh.getContact() %>
                                </td>
                                <td>
                                    <span class="badge <%= stClass %>" style="display: inline-flex; align-items: center; gap: 4px;">
                                        <i class="fa-solid <%= stIcon %>" style="font-size: 0.7rem;"></i>
                                        <%= veh.getAvailabilityStatus() %>
                                    </span>
                                </td>
                                <td style="text-align: right;">
                                    <button class="btn btn-outline btn-sm"
                                            onclick="openVehStatusModal(<%= veh.getVehicleId() %>, '<%= veh.getVehicleNumber() %>', '<%= veh.getAvailabilityStatus() %>')">
                                        <i class="fa-solid fa-sliders"></i> Update
                                    </button>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No vehicles registered.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- REGISTER VEHICLE MODAL -->
    <div id="vehicleModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-truck-moving" style="color: #60a5fa;"></i>
                    <h3 class="modal-title">Register Fleet Vehicle</h3>
                </div>
                <button class="modal-close" onclick="closeModal('vehicleModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/vehicles" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Vehicle Registration / Call-Sign</label>
                        <input type="text" name="vehicleNumber" class="form-control" placeholder="e.g. NDRF-TRK-105" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Vehicle Type</label>
                            <select name="vehicleType" class="form-control" required>
                                <option value="TRUCK">TRUCK (Heavy Cargo)</option>
                                <option value="VAN">VAN (Rapid Response)</option>
                                <option value="BOAT">BOAT (Inundated Sectors)</option>
                                <option value="HELICOPTER">HELICOPTER (Air-Drop)</option>
                                <option value="AMBULANCE">AMBULANCE (Medical Triage)</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Payload Capacity (kg)</label>
                            <input type="number" name="capacityKg" class="form-control" value="2000" required min="100">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Primary Driver Name</label>
                        <input type="text" name="driverName" class="form-control" placeholder="e.g. Baldev Singh" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Driver Contact Phone</label>
                        <input type="text" name="contact" class="form-control" placeholder="+91 98765 00000" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('vehicleModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-check"></i> Save Vehicle
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- UPDATE VEHICLE STATUS MODAL -->
    <div id="vehStatusModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-sliders" style="color: var(--primary);"></i>
                    <h3 class="modal-title">Update Fleet Vehicle Status</h3>
                </div>
                <button class="modal-close" onclick="closeModal('vehStatusModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/vehicles" method="POST">
                <input type="hidden" name="action" value="update_status">
                <input type="hidden" id="modalVehId" name="vehicleId">
                <div class="modal-body">
                    <p style="margin-bottom: 1.25rem; color: var(--text-secondary); background: rgba(0,0,0,0.25); padding: 0.75rem 1rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        Vehicle: <strong id="modalVehNum" style="color: #fff; font-family: monospace; font-size: 1rem;"></strong>
                    </p>
                    <div class="form-group">
                        <label class="form-label">Fleet Availability</label>
                        <select id="modalVehStatusSelect" name="status" class="form-control" required>
                            <option value="AVAILABLE">AVAILABLE (At depot staging bay)</option>
                            <option value="IN_TRANSIT">IN_TRANSIT (On dispatch mission)</option>
                            <option value="MAINTENANCE">MAINTENANCE (Refueling / Repair)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('vehStatusModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-check"></i> Update Status
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <script>
        function openVehStatusModal(id, num, status) {
            document.getElementById('modalVehId').value = id;
            document.getElementById('modalVehNum').innerText = num;
            document.getElementById('modalVehStatusSelect').value = status;
            openModal('vehStatusModal');
        }
    </script>
</body>
</html>
