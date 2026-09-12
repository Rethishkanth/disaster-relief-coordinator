<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.disasterrelief.model.SystemSnapshot" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>State Snapshots - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="header-icon-badge" style="background: rgba(168, 85, 247, 0.15); color: #c084fc;">
                        <i class="fa-solid fa-box-archive fa-lg"></i>
                    </div>
                    <div>
                        <h1>Java Object Serialization & Recovery (.ser Snapshots)</h1>
                        <p>Preserve and restore complete operational state snapshots via <code>ObjectOutputStream</code> &amp; <code>ObjectInputStream</code></p>
                    </div>
                </div>
            </div>
            <div class="header-actions">
                <button class="btn btn-primary" onclick="openModal('createSnapshotModal')">
                    <i class="fa-solid fa-floppy-disk"></i> Create Snapshot (.ser)
                </button>
            </div>
        </div>

        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span><%= request.getAttribute("successMessage") %></span>
            </div>
        <% } %>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i>
                <span><%= request.getAttribute("errorMessage") %></span>
            </div>
        <% } %>

        <% if (request.getParameter("file") != null) { %>
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>New serialized state snapshot successfully written: <strong>snapshots/<%= request.getParameter("file") %></strong></span>
            </div>
        <% } %>

        <!-- ARCHITECTURE SPECIFICATION BANNER -->
        <div class="card" style="margin-bottom: 1.5rem; background: linear-gradient(135deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.8)); border-color: rgba(168, 85, 247, 0.3);">
            <div class="card-body" style="padding: 1rem 1.4rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <i class="fa-solid fa-layer-group" style="color: #c084fc;"></i>
                    <span style="font-size: 0.85rem; color: var(--text-secondary);">
                        <strong style="color: #fff;">Object Serialization Paradigm:</strong> Complete deep-object graph serialization with custom <code>serialVersionUID = 1L</code>, preserving cross-table relational structures in native binary form.
                    </span>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">Serializable</span>
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">ObjectOutputStream</span>
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">ObjectInputStream</span>
                </div>
            </div>
        </div>

        <!-- RESTORED STATE DETAIL INSPECTOR -->
        <% if (request.getAttribute("restoredSnapshot") != null) {
            SystemSnapshot snap = (SystemSnapshot) request.getAttribute("restoredSnapshot");
        %>
            <div class="card" style="border: 2px solid var(--accent-cyan); margin-bottom: 2rem; box-shadow: 0 0 25px rgba(6, 182, 212, 0.15);">
                <div class="card-header" style="background-color: rgba(6, 182, 212, 0.15); display: flex; justify-content: space-between; align-items: center;">
                    <div class="card-title" style="color: var(--accent-cyan); display: flex; align-items: center; gap: 0.5rem;">
                        <i class="fa-solid fa-magnifying-glass-chart"></i>
                        Deserialized Snapshot Inspection: <%= snap.getSnapshotId() %>
                    </div>
                    <span style="font-size: 0.8rem; color: var(--text-secondary); font-family: monospace;">
                        <i class="fa-regular fa-clock" style="margin-right: 4px;"></i> <%= snap.getCreatedAt() %>
                    </span>
                </div>
                <div class="card-body">
                    <p style="color: #fff; margin-bottom: 1.25rem;">
                        <span style="color: var(--text-muted); font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.05em;">Purpose Tag:</span>
                        <strong style="margin-left: 0.5rem; color: #38bdf8;"><%= snap.getDescription() %></strong>
                    </p>

                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 1rem;">
                        <div class="stat-card" style="padding: 1rem; border-color: rgba(6, 182, 212, 0.3);">
                            <span class="stat-title"><i class="fa-solid fa-hotel" style="color: #38bdf8;"></i> Preserved Shelters</span>
                            <div class="stat-value" style="font-size: 1.6rem; color: #38bdf8;"><%= snap.getShelters().size() %></div>
                            <div class="stat-meta">Deep-serialized records</div>
                        </div>
                        <div class="stat-card" style="padding: 1rem; border-color: rgba(16, 185, 129, 0.3);">
                            <span class="stat-title"><i class="fa-solid fa-boxes-stacked" style="color: #10b981;"></i> Preserved Resources</span>
                            <div class="stat-value" style="font-size: 1.6rem; color: #10b981;"><%= snap.getResources().size() %></div>
                            <div class="stat-meta">Inventory inventory lots</div>
                        </div>
                        <div class="stat-card" style="padding: 1rem; border-color: rgba(245, 158, 11, 0.3);">
                            <span class="stat-title"><i class="fa-solid fa-hand-holding-hand" style="color: #f59e0b;"></i> Preserved Requests</span>
                            <div class="stat-value" style="font-size: 1.6rem; color: #f59e0b;"><%= snap.getRequests().size() %></div>
                            <div class="stat-meta">Queue states preserved</div>
                        </div>
                        <div class="stat-card" style="padding: 1rem; border-color: rgba(99, 102, 241, 0.3);">
                            <span class="stat-title"><i class="fa-solid fa-handshake" style="color: var(--accent);"></i> Preserved Allocations</span>
                            <div class="stat-value" style="font-size: 1.6rem; color: var(--accent);"><%= snap.getAllocations().size() %></div>
                            <div class="stat-meta">Dispatch matches</div>
                        </div>
                        <div class="stat-card" style="padding: 1rem; border-color: rgba(59, 130, 246, 0.3);">
                            <span class="stat-title"><i class="fa-solid fa-truck-moving" style="color: #60a5fa;"></i> Preserved Fleet</span>
                            <div class="stat-value" style="font-size: 1.6rem; color: #60a5fa;"><%= snap.getVehicles().size() %></div>
                            <div class="stat-meta">Transport units</div>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <div class="card-title">
                    <i class="fa-solid fa-hard-drive" style="color: #c084fc; margin-right: 0.5rem;"></i>
                    Saved Serialized State Files (snapshots/*.ser)
                </div>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Snapshot Binary File</th>
                            <th>Format</th>
                            <th>Storage Path</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<String> snapshots = (List<String>) request.getAttribute("snapshots");
                            if (snapshots != null && !snapshots.isEmpty()) {
                                for (String fName : snapshots) {
                        %>
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 0.6rem;">
                                        <i class="fa-solid fa-file-zipper" style="color: #c084fc; font-size: 1.1rem;"></i>
                                        <strong style="color: #fff; font-family: monospace;"><%= fName %></strong>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge badge-active" style="display: inline-flex; align-items: center; gap: 4px;">
                                        <i class="fa-solid fa-code"></i> Java Binary (.ser)
                                    </span>
                                </td>
                                <td style="font-family: monospace; font-size: 0.8rem; color: var(--text-muted);">
                                    <i class="fa-regular fa-folder-open" style="margin-right: 4px;"></i> snapshots/<%= fName %>
                                </td>
                                <td style="text-align: right;">
                                    <form action="<%= request.getContextPath() %>/backups" method="POST" style="display:inline;">
                                        <input type="hidden" name="action" value="restore">
                                        <input type="hidden" name="fileName" value="<%= fName %>">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="fa-solid fa-rotate-left"></i> Deserialize &amp; Inspect
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 2.5rem;">
                                    <i class="fa-solid fa-inbox fa-2x" style="display: block; margin-bottom: 0.75rem; opacity: 0.5;"></i>
                                    No snapshots found. Click "Create Snapshot" above to serialize current operational state.
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- CREATE SNAPSHOT MODAL -->
    <div id="createSnapshotModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-floppy-disk" style="color: #c084fc;"></i>
                    <h3 class="modal-title">Serialize System State Snapshot</h3>
                </div>
                <button class="modal-close" onclick="closeModal('createSnapshotModal')">&times;</button>
            </div>
            <form action="<%= request.getContextPath() %>/backups" method="POST">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 1.25rem; line-height: 1.5; background: rgba(0,0,0,0.25); padding: 0.75rem 1rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        This operation invokes <code>ObjectOutputStream.writeObject()</code> to snapshot all active shelters, resources, allocations, vehicles, and requests to disk as a persistent <code>.ser</code> binary.
                    </p>
                    <div class="form-group">
                        <label class="form-label">Snapshot Purpose / Tag</label>
                        <input type="text" name="description" class="form-control"
                               placeholder="e.g. Pre-Cyclone landfall status snapshot, Review 2 defense backup" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('createSnapshotModal')">Cancel</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-check"></i> Save .ser Snapshot
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
