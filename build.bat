@echo off
setlocal enabledelayedexpansion

echo ==================================================================
echo   DISASTER RELIEF RESOURCE COORDINATOR - BUILD SCRIPT
echo ==================================================================

REM Locate JDK in portable tools folder
for /d %%D in ("%~dp0..\tools\jdk*") do (
    if exist "%%D\bin\javac.exe" (
        set "JAVA_HOME=%%D"
    )
)

if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo [Build] Using JDK at: %JAVA_HOME%
) else (
    echo [Build Warning] JAVA_HOME not found in ..\tools\jdk* or environment.
)

REM Locate Maven
if exist "%~dp0..\tools\apache-maven-3.9.6\bin\mvn.cmd" (
    set "MVN_CMD=%~dp0..\tools\apache-maven-3.9.6\bin\mvn.cmd"
    echo [Build] Using portable Maven at: !MVN_CMD!
) else (
    set "MVN_CMD=mvn"
)

echo [Build] Running Maven clean package...
call "%MVN_CMD%" clean package -DskipTests=false
if %ERRORLEVEL% NEQ 0 (
    echo [Build ERROR] Build failed! Check compiler output above.
    exit /b %ERRORLEVEL%
)

echo.
echo ==================================================================
echo   BUILD SUCCESSFUL! WAR artifact created in target/
echo ==================================================================
