<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.disasterrelief.model.Resource" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Inventory - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <h1>
                    <i class="fa-solid fa-boxes-stacked" style="color: var(--accent-emerald);"></i>
                    <span>Relief Supplies & Warehouse Inventory</span>
                </h1>
                <p>Track stockpiles, monitor expiration dates, and register incoming humanitarian aid</p>
            </div>
            <div class="header-actions">
                <button class="btn btn-primary" onclick="openModal('addResourceModal')">
                    <i class="fa-solid fa-plus"></i>
                    <span>Register Supplies</span>
                </button>
            </div>
        </div>

        <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>Resource inventory updated: <%= request.getParameter("msg") %></span>
            </div>
        <% } %>

        <!-- CATEGORY CARDS (Demonstrating HashMap groupings) -->
        <div style="margin-bottom: 1.75rem;">
            <div class="card-title" style="font-size: 0.85rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 0.85rem;">
                <i class="fa-solid fa-layer-group" style="color: var(--accent-blue);"></i>
                <span>Category Breakdown (Grouped via HashMap&lt;ResourceType, List&lt;Resource&gt;&gt;)</span>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 1rem;">
                <%
                    Map<String, List<Resource>> grouped = (Map<String, List<Resource>>) request.getAttribute("groupedResources");
                    if (grouped != null) {
                        for (Map.Entry<String, List<Resource>> entry : grouped.entrySet()) {
                            int totalUnits = 0;
                            for (Resource r : entry.getValue()) totalUnits += r.getQuantity();
                %>
                    <div class="stat-card" style="padding: 1.1rem;">
                        <div class="stat-header">
                            <span class="stat-title"><%= entry.getKey() %></span>
                            <div class="stat-icon-wrapper stat-icon-blue">
                                <i class="fa-solid fa-box"></i>
                            </div>
                        </div>
                        <div class="stat-value" style="font-size: 1.5rem;"><%= totalUnits %></div>
                        <div class="stat-subtitle"><%= entry.getValue().size() %> item batches</div>
                    </div>
                <%
                        }
                    }
                %>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-dolly"></i>
                    <span>Warehouse Supplies Catalog</span>
                </div>
                <input type="text" id="resourceSearch" class="form-control" style="max-width: 260px;"
                       placeholder="Filter supplies..." onkeyup="filterTable('resourceSearch', 'resourcesTable')">
            </div>
            <div class="table-responsive">
                <table class="data-table" id="resourcesTable">
                    <thead>
                        <tr>
                            <th>Resource ID</th>
                            <th>Supply Name</th>
                            <th>Category</th>
                            <th>Donor / Stockpile</th>
                            <th>Available Stock</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Resource> resources = (List<Resource>) request.getAttribute("resources");
                            if (resources != null && !resources.isEmpty()) {
                                for (Resource r : resources) {
                                    String statusClass = "badge-low";
                                    if ("DEPLETED".equalsIgnoreCase(r.getAvailabilityStatus())) statusClass = "badge-critical";
                                    else if ("LOW_STOCK".equalsIgnoreCase(r.getAvailabilityStatus())) statusClass = "badge-high";
                        %>
                            <tr>
                                <td><strong>#<%= r.getResourceId() %></strong></td>
                                <td>
                                    <strong style="color: #fff;"><%= r.getResourceName() %></strong>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);"><%= r.getDescription() != null ? r.getDescription() : "" %></div>
                                </td>
                                <td><span class="badge badge-active"><%= r.getResourceType() %></span></td>
                                <td><i class="fa-solid fa-building-ngo" style="color: var(--accent-emerald); margin-right: 0.35rem;"></i><%= r.getOrgName() != null ? r.getOrgName() : "Central Depot" %></td>
                                <td>
                                    <strong style="font-size: 1.05rem; color: #fff;"><%= r.getQuantity() %></strong>
                                    <span style="color: var(--text-secondary); font-size: 0.8rem;"><%= r.getUnit() %></span>
                                </td>
                                <td style="color: <%= (r.getExpiryDate() != null && r.getExpiryDate().startsWith("202")) ? "var(--accent-amber)" : "var(--text-secondary)" %>;">
                                    <i class="fa-regular fa-calendar" style="margin-right: 0.3rem;"></i><%= r.getExpiryDate() != null ? r.getExpiryDate() : "N/A" %>
                                </td>
                                <td>
                                    <span class="badge <%= statusClass %>">
                                        <span class="pulse-dot"></span>
                                        <%= r.getAvailabilityStatus() %>
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-outline btn-sm" onclick="openRestockModal(<%= r.getResourceId() %>, '<%= r.getResourceName() %>')">
                                        <i class="fa-solid fa-truck-ramp-box"></i>
                                        <span>Restock</span>
                                    </button>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No warehouse supplies recorded.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- REGISTER RESOURCE MODAL -->
    <div id="addResourceModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-box-open" style="color: var(--accent-emerald);"></i>
                    <span>Register Relief Supplies</span>
                </h3>
                <button class="modal-close" onclick="closeModal('addResourceModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/resources" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Supply / Resource Name</label>
                        <input type="text" name="resourceName" class="form-control" placeholder="e.g. Mineral Water Bottles, Antibiotics" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Resource Category</label>
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
                            <label class="form-label">Quantity</label>
                            <input type="number" name="quantity" class="form-control" value="100" required min="1">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label">Packaging Unit</label>
                            <input type="text" name="unit" class="form-control" placeholder="e.g. Bottles, Boxes, Kits, Kg" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Batch Expiry Date</label>
                            <input type="text" name="expiryDate" class="form-control" placeholder="YYYY-MM-DD or N/A" value="N/A">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Description & Storage Instructions</label>
                        <textarea name="description" class="form-control" rows="2" placeholder="Cold storage, dry warehouse, pallet number..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('addResourceModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save to Inventory</button>
                </div>
            </form>
        </div>
    </div>

    <!-- RESTOCK MODAL -->
    <div id="restockModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa-solid fa-arrow-up-from-bracket" style="color: var(--accent-emerald);"></i>
                    <span>Restock Supplies</span>
                </h3>
                <button class="modal-close" onclick="closeModal('restockModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/resources" method="POST">
                <input type="hidden" name="action" value="restock">
                <input type="hidden" id="restockResourceId" name="resourceId">
                <div class="modal-body">
                    <p style="margin-bottom: 1.25rem; color: var(--text-secondary);">
                        Adding stock to: <strong id="restockResourceName" style="color: #fff;"></strong>
                    </p>
                    <div class="form-group">
                        <label class="form-label">Additional Units Received</label>
                        <input type="number" name="addQuantity" class="form-control" required min="1" value="100">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('restockModal')">Cancel</button>
                    <button type="submit" class="btn btn-success">Confirm Restock</button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
    <script>
        function openRestockModal(id, name) {
            document.getElementById('restockResourceId').value = id;
            document.getElementById('restockResourceName').innerText = name;
            openModal('restockModal');
        }
    </script>
</body>
</html>
