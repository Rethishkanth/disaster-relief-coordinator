<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="com.disasterrelief.model.User" %>
<%
    User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
    String currentPath = request.getServletPath();
    String initials = "OP";
    if (currentUser != null && currentUser.getName() != null && !currentUser.getName().isEmpty()) {
        String[] parts = currentUser.getName().trim().split("\\s+");
        if (parts.length >= 2) {
            initials = ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        } else if (parts[0].length() >= 2) {
            initials = parts[0].substring(0, 2).toUpperCase();
        } else {
            initials = ("" + parts[0].charAt(0)).toUpperCase();
        }
    }
%>
<header class="navbar-wrapper">
    <nav class="navbar">
        <div class="navbar-left">
            <a href="<%= request.getContextPath() %>/dashboard" class="brand-container">
                <div class="brand-icon">
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                        <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon>
                    </svg>
                </div>
                <div class="brand-title">
                    <span class="brand-name">DISASTER RELIEF</span>
                    <span class="brand-subtitle">RESOURCE COORDINATOR</span>
                </div>
            </a>
        </div>

        <ul class="nav-links">
            <li>
                <a href="<%= request.getContextPath() %>/dashboard" class="nav-link <%= "/dashboard".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-chart-line nav-icon"></i>
                    <span>Overview</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/disasters" class="nav-link <%= "/disasters".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-triangle-exclamation nav-icon"></i>
                    <span>Disasters</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/shelters" class="nav-link <%= "/shelters".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-house-chimney nav-icon"></i>
                    <span>Shelters</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/resources" class="nav-link <%= "/resources".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-boxes-stacked nav-icon"></i>
                    <span>Resources</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/requests" class="nav-link <%= "/requests".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-clipboard-list nav-icon"></i>
                    <span>Requests</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/allocations" class="nav-link <%= "/allocations".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-bullseye nav-icon"></i>
                    <span>Allocations</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/delivery" class="nav-link <%= ("/delivery".equals(currentPath) || "/delivery-tracker".equals(currentPath)) ? "active" : "" %>">
                    <i class="fa-solid fa-truck-fast nav-icon"></i>
                    <span>Deliveries</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/volunteers" class="nav-link <%= "/volunteers".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-hands-holding-child nav-icon"></i>
                    <span>Volunteers</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/vehicles" class="nav-link <%= "/vehicles".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-truck-pickup nav-icon"></i>
                    <span>Vehicles</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/reports" class="nav-link <%= "/reports".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-file-waveform nav-icon"></i>
                    <span>Reports</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/backups" class="nav-link <%= "/backups".equals(currentPath) ? "active" : "" %>">
                    <i class="fa-solid fa-floppy-disk nav-icon"></i>
                    <span>Snapshots</span>
                </a>
            </li>
        </ul>

        <div class="navbar-right">
            <% if (currentUser != null) { %>
                <div class="user-profile">
                    <div class="user-avatar" title="<%= currentUser.getName() %>">
                        <%= initials %>
                    </div>
                    <div class="user-info">
                        <span class="user-name" title="<%= currentUser.getName() %>"><%= currentUser.getName() %></span>
                        <span class="user-role-badge <%= currentUser.getRole().toLowerCase() %>"><%= currentUser.getRole() %></span>
                    </div>
                </div>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline btn-sm logout-btn" title="Sign out">
                    <i class="fa-solid fa-right-from-bracket"></i>
                    <span>Logout</span>
                </a>
            <% } else { %>
                <a href="<%= request.getContextPath() %>/login" class="btn btn-primary btn-sm">
                    <i class="fa-solid fa-arrow-right-to-bracket"></i>
                    <span>Login</span>
                </a>
            <% } %>
        </div>
    </nav>
</header>
