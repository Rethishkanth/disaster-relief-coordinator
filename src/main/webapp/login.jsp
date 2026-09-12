<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Operations Login - Disaster Relief Resource Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body style="min-height: 100vh; display: flex; align-items: center; justify-content: center; background: radial-gradient(circle at top, #1e293b 0%, #0f172a 50%, #020617 100%);">

    <div style="width: 100%; max-width: 480px; padding: 2rem 1.5rem;">
        <div style="text-align: center; margin-bottom: 2rem;">
            <div style="width: 68px; height: 68px; margin: 0 auto 1.25rem; background: linear-gradient(135deg, #ef4444, #b91c1c); border-radius: 18px; display: flex; align-items: center; justify-content: center; box-shadow: 0 10px 25px rgba(239, 68, 68, 0.4); border: 1px solid rgba(255, 255, 255, 0.2);">
                <i class="fa-solid fa-triangle-exclamation" style="font-size: 2rem; color: #fff;"></i>
            </div>
            <h1 style="font-size: 1.65rem; font-weight: 800; color: #fff; letter-spacing: -0.02em; margin-bottom: 0.35rem;">
                Disaster Relief Coordinator
            </h1>
            <p style="color: var(--text-secondary); font-size: 0.88rem;">
                Emergency Command &amp; Multi-Agency Resource Logistics
            </p>
            <div style="margin-top: 0.75rem; display: inline-flex; align-items: center; gap: 0.5rem; background: rgba(255, 255, 255, 0.05); padding: 0.25rem 0.85rem; border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.1);">
                <span class="pulse-dot"></span>
                <span style="font-size: 0.75rem; font-weight: 600; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.06em;">
                    v1.0-RC Operations Portal
                </span>
            </div>
        </div>

        <div class="card" style="box-shadow: 0 20px 40px rgba(0, 0, 0, 0.6); border-color: rgba(99, 102, 241, 0.3); backdrop-filter: blur(16px); background: rgba(15, 23, 42, 0.85);">
            <div class="card-body" style="padding: 2rem;">
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger" style="margin-bottom: 1.25rem;">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                        <span><%= request.getAttribute("errorMessage") %></span>
                    </div>
                <% } %>

                <% if ("logged_out".equals(request.getParameter("msg"))) { %>
                    <div class="alert alert-success" style="margin-bottom: 1.25rem;">
                        <i class="fa-solid fa-circle-check"></i>
                        <span>You have been safely signed out from the terminal.</span>
                    </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/login" method="POST">
                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="loginEmail" style="display: flex; align-items: center; gap: 0.5rem;">
                            <i class="fa-regular fa-envelope" style="color: var(--primary);"></i> Official Relief Email
                        </label>
                        <input type="email" id="loginEmail" name="email" class="form-control"
                               placeholder="e.g. admin@relief.org" required
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
                    </div>

                    <div class="form-group" style="margin-bottom: 1.5rem;">
                        <label class="form-label" for="loginPassword" style="display: flex; align-items: center; gap: 0.5rem;">
                            <i class="fa-solid fa-lock" style="color: var(--primary);"></i> Access Passcode
                        </label>
                        <input type="password" id="loginPassword" name="password" class="form-control"
                               placeholder="Enter secure passcode..." required>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem; font-size: 0.95rem; font-weight: 600; justify-content: center; box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);">
                        <i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In to Operations Portal
                    </button>
                </form>

                <div style="margin-top: 2rem; border-top: 1px solid rgba(255, 255, 255, 0.1); padding-top: 1.25rem;">
                    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.85rem;">
                        <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.06em; font-weight: 700;">
                            Quick Demo Sign-In (1-Click Fill)
                        </span>
                        <span style="font-size: 0.7rem; color: var(--accent);"><i class="fa-solid fa-bolt"></i> Auto-Auth</span>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.6rem;">
                        <button type="button" class="btn btn-outline btn-sm" style="justify-content: flex-start; padding: 0.5rem 0.75rem; text-align: left;"
                                onclick="fillLogin('admin@relief.org', 'admin123')">
                            <i class="fa-solid fa-user-shield" style="color: #f59e0b; margin-right: 6px;"></i>
                            <div>
                                <div style="font-weight: 600; color: #fff; font-size: 0.8rem;">Coordinator</div>
                                <div style="font-size: 0.68rem; color: var(--text-muted);">HQ Admin</div>
                            </div>
                        </button>

                        <button type="button" class="btn btn-outline btn-sm" style="justify-content: flex-start; padding: 0.5rem 0.75rem; text-align: left;"
                                onclick="fillLogin('shelter1@relief.org', 'shelter123')">
                            <i class="fa-solid fa-hotel" style="color: #38bdf8; margin-right: 6px;"></i>
                            <div>
                                <div style="font-weight: 600; color: #fff; font-size: 0.8rem;">Shelter Mgr</div>
                                <div style="font-size: 0.68rem; color: var(--text-muted);">Zone Hub</div>
                            </div>
                        </button>

                        <button type="button" class="btn btn-outline btn-sm" style="justify-content: flex-start; padding: 0.5rem 0.75rem; text-align: left;"
                                onclick="fillLogin('redcross@relief.org', 'org123')">
                            <i class="fa-solid fa-square-plus" style="color: #ef4444; margin-right: 6px;"></i>
                            <div>
                                <div style="font-weight: 600; color: #fff; font-size: 0.8rem;">Red Cross</div>
                                <div style="font-size: 0.68rem; color: var(--text-muted);">Relief NGO</div>
                            </div>
                        </button>

                        <button type="button" class="btn btn-outline btn-sm" style="justify-content: flex-start; padding: 0.5rem 0.75rem; text-align: left;"
                                onclick="fillLogin('volunteer1@relief.org', 'vol123')">
                            <i class="fa-solid fa-hands-holding-child" style="color: #10b981; margin-right: 6px;"></i>
                            <div>
                                <div style="font-weight: 600; color: #fff; font-size: 0.8rem;">Volunteer</div>
                                <div style="font-size: 0.68rem; color: var(--text-muted);">First Response</div>
                            </div>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div style="text-align: center; margin-top: 1.5rem;">
            <p style="font-size: 0.75rem; color: var(--text-muted); line-height: 1.5;">
                Disaster Relief Resource Coordinator &bull; B.Tech College Major Project<br>
                <span style="color: rgba(255,255,255,0.3);">Dual-Mode Storage: MySQL with Automatic Embedded H2 Engine Fallback</span>
            </p>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/app.js"></script>
</body>
</html>
