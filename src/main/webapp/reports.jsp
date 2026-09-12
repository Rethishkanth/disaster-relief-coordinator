<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports & Audit Logs - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="navbar.jsp" />

    <main class="container">
        <div class="page-header">
            <div class="page-title-group">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="header-icon-badge" style="background: rgba(99, 102, 241, 0.15); color: var(--accent);">
                        <i class="fa-solid fa-file-contract fa-lg"></i>
                    </div>
                    <div>
                        <h1>Operational Reports & Persistent File Logs</h1>
                        <p>Generate downloadable audit exports and review real-time file streams (<code>BufferedWriter</code> / <code>FileReader</code>)</p>
                    </div>
                </div>
            </div>
            <div class="header-actions">
                <form action="<%= request.getContextPath() %>/reports" method="POST" style="display:inline;">
                    <input type="hidden" name="type" value="csv">
                    <button type="submit" class="btn btn-success">
                        <i class="fa-solid fa-file-csv"></i> Export Allocations CSV
                    </button>
                </form>
                <form action="<%= request.getContextPath() %>/reports" method="POST" style="display:inline;">
                    <input type="hidden" name="type" value="txt">
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-file-lines"></i> Generate Briefing TXT
                    </button>
                </form>
            </div>
        </div>

        <% if (request.getParameter("download") != null) { %>
            <div class="alert alert-success" style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items: center; gap: 0.6rem;">
                    <i class="fa-solid fa-circle-check fa-lg"></i>
                    <span>Report file generated successfully: <strong><%= request.getParameter("download") %></strong></span>
                </div>
                <a href="<%= request.getContextPath() %>/reports?download=<%= request.getParameter("download") %>" class="btn btn-sm btn-success">
                    <i class="fa-solid fa-cloud-arrow-down"></i> Download File
                </a>
            </div>
        <% } %>

        <!-- TECHNICAL HIGHLIGHT BANNER -->
        <div class="card" style="margin-bottom: 1.5rem; background: linear-gradient(135deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.8)); border-color: rgba(99, 102, 241, 0.25);">
            <div class="card-body" style="padding: 1rem 1.4rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <i class="fa-solid fa-microchip" style="color: var(--primary);"></i>
                    <span style="font-size: 0.85rem; color: var(--text-secondary);">
                        <strong style="color: #fff;">Java I/O Subsystem:</strong> Thread-safe append streams with synchronized mutexes, continuous background flushing, and fail-safe file locks.
                    </span>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">BufferedWriter</span>
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">FileReader</span>
                    <span class="badge badge-active" style="font-family: monospace; font-size: 0.75rem;">CSV / TXT Streams</span>
                </div>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr; gap: 1.5rem; margin-bottom: 2rem;">
            <!-- SYSTEM ACTIVITY LOG -->
            <div class="card" style="border: 1px solid rgba(255,255,255,0.08);">
                <div class="card-header" style="background: rgba(15, 23, 42, 0.8); display: flex; justify-content: space-between; align-items: center;">
                    <div style="display: flex; align-items: center; gap: 0.6rem;">
                        <span style="display: inline-flex; gap: 6px;">
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #ef4444; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #f59e0b; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #10b981; display: inline-block;"></span>
                        </span>
                        <div class="card-title" style="font-size: 0.9rem; font-family: monospace; color: #cbd5e1; margin-left: 0.5rem;">
                            <i class="fa-solid fa-terminal" style="color: var(--primary); margin-right: 4px;"></i>
                            logs/system_activity.log
                        </div>
                    </div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">General Operational Events</span>
                </div>
                <div class="card-body" style="padding: 0.75rem; background: #030712;">
                    <div class="log-box" style="background: transparent; border: none; font-size: 0.82rem; line-height: 1.6;">
                        <%
                            List<String> sysLogs = (List<String>) request.getAttribute("systemLogs");
                            if (sysLogs != null && !sysLogs.isEmpty()) {
                                for (String line : sysLogs) {
                                    out.println(line);
                                }
                            } else {
                                out.println("[FileLogger] No entries recorded in system_activity.log yet.");
                            }
                        %>
                    </div>
                </div>
            </div>

            <!-- ALLOCATION TRANSACTIONS LOG -->
            <div class="card" style="border: 1px solid rgba(255,255,255,0.08);">
                <div class="card-header" style="background: rgba(15, 23, 42, 0.8); display: flex; justify-content: space-between; align-items: center;">
                    <div style="display: flex; align-items: center; gap: 0.6rem;">
                        <span style="display: inline-flex; gap: 6px;">
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #ef4444; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #f59e0b; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #10b981; display: inline-block;"></span>
                        </span>
                        <div class="card-title" style="font-size: 0.9rem; font-family: monospace; color: #cbd5e1; margin-left: 0.5rem;">
                            <i class="fa-solid fa-shield-halved" style="color: var(--accent); margin-right: 4px;"></i>
                            logs/allocations.log
                        </div>
                    </div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">Thread-Safe Stock Deductions & Matches</span>
                </div>
                <div class="card-body" style="padding: 0.75rem; background: #030712;">
                    <div class="log-box" style="background: transparent; border: none; font-size: 0.82rem; line-height: 1.6;">
                        <%
                            List<String> allocLogs = (List<String>) request.getAttribute("allocationLogs");
                            if (allocLogs != null && !allocLogs.isEmpty()) {
                                for (String line : allocLogs) {
                                    out.println(line);
                                }
                            } else {
                                out.println("[FileLogger] No entries recorded in allocations.log yet.");
                            }
                        %>
                    </div>
                </div>
            </div>

            <!-- DELIVERY TRACKING LOG -->
            <div class="card" style="border: 1px solid rgba(255,255,255,0.08);">
                <div class="card-header" style="background: rgba(15, 23, 42, 0.8); display: flex; justify-content: space-between; align-items: center;">
                    <div style="display: flex; align-items: center; gap: 0.6rem;">
                        <span style="display: inline-flex; gap: 6px;">
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #ef4444; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #f59e0b; display: inline-block;"></span>
                            <span style="width: 10px; height: 10px; border-radius: 50%; background: #10b981; display: inline-block;"></span>
                        </span>
                        <div class="card-title" style="font-size: 0.9rem; font-family: monospace; color: #cbd5e1; margin-left: 0.5rem;">
                            <i class="fa-solid fa-route" style="color: #60a5fa; margin-right: 4px;"></i>
                            logs/delivery_tracking.log
                        </div>
                    </div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">Driver Milestones & VHF Radio Updates</span>
                </div>
                <div class="card-body" style="padding: 0.75rem; background: #030712;">
                    <div class="log-box" style="background: transparent; border: none; font-size: 0.82rem; line-height: 1.6;">
                        <%
                            List<String> delLogs = (List<String>) request.getAttribute("deliveryLogs");
                            if (delLogs != null && !delLogs.isEmpty()) {
                                for (String line : delLogs) {
                                    out.println(line);
                                }
                            } else {
                                out.println("[FileLogger] No entries recorded in delivery_tracking.log yet.");
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
