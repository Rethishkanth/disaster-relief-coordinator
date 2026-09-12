@echo off
setlocal enabledelayedexpansion

echo ==================================================================
echo   DISASTER RELIEF RESOURCE COORDINATOR - APPLICATION LAUNCHER
echo ==================================================================

REM Locate JDK in portable tools folder
for /d %%D in ("%~dp0..\tools\jdk*") do (
    if exist "%%D\bin\java.exe" (
        set "JAVA_HOME=%%D"
    )
)

if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo [Launcher] Using JDK at: %JAVA_HOME%
) else (
    echo [Launcher Warning] JAVA_HOME not detected.
)

REM Locate Maven
if exist "%~dp0..\tools\apache-maven-3.9.6\bin\mvn.cmd" (
    set "MVN_CMD=%~dp0..\tools\apache-maven-3.9.6\bin\mvn.cmd"
) else (
    set "MVN_CMD=mvn"
)

echo [Launcher] Launching embedded Tomcat web server on port 8080...
echo [Launcher] Access at: http://localhost:8080
echo.

call "%MVN_CMD%" compile exec:java -Dexec.mainClass="com.disasterrelief.AppLauncher"

pause
