package com.disasterrelief;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * Embedded Server Launcher for Disaster Relief Resource Coordinator.
 * Enables 1-click execution on any evaluator or lab PC without needing external Tomcat installed.
 */
public class AppLauncher {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("       DISASTER RELIEF RESOURCE COORDINATOR - SERVER STARTUP      ");
        System.out.println("==================================================================");

        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(PORT);
            tomcat.getConnector(); // Triggers default connector initialization

            // Determine webapp path
            String webappDirLocation = "src/main/webapp";
            File webappDir = new File(webappDirLocation);
            if (!webappDir.exists()) {
                // Try parent path if run from target or subfolder
                webappDir = new File("../src/main/webapp");
            }

            String baseDir = new File("target/tomcat-embed").getAbsolutePath();
            tomcat.setBaseDir(baseDir);

            Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
            System.out.println("[AppLauncher] Serving webapp from: " + webappDir.getAbsolutePath());

            // Add compiled classes to classpath
            File additionWebInfClasses = new File("target/classes");
            if (additionWebInfClasses.exists()) {
                org.apache.catalina.WebResourceRoot resources = new org.apache.catalina.webresources.StandardRoot(ctx);
                resources.addPreResources(new org.apache.catalina.webresources.DirResourceSet(
                        resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
                ctx.setResources(resources);
            }

            tomcat.start();

            System.out.println("\n------------------------------------------------------------------");
            System.out.println("  APPLICATION RUNNING AT: http://localhost:" + PORT);
            System.out.println("------------------------------------------------------------------");
            System.out.println("  DEMO LOGIN ACCOUNTS (Click any button on login page):");
            System.out.println("  1. Coordinator:   admin@relief.org      / admin123");
            System.out.println("  2. Shelter:       shelter1@relief.org   / shelter123");
            System.out.println("  3. Organization:  redcross@relief.org   / org123");
            System.out.println("  4. Volunteer:     volunteer1@relief.org / vol123");
            System.out.println("------------------------------------------------------------------");
            System.out.println("  Press Ctrl+C in terminal to stop server.\n");

            tomcat.getServer().await();

        } catch (Exception e) {
            System.err.println("[AppLauncher] Error starting embedded server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
