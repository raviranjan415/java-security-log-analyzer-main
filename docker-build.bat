@echo off
REM Start Docker Desktop and wait for daemon to be ready

echo Starting Docker Desktop...
start "" "C:\Program Files\Docker\Docker\Docker.exe"

echo Waiting for Docker daemon to be ready (this takes 30-60 seconds)...
timeout /t 30 /nobreak

REM Check if Docker is ready
docker ps >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Waiting for Docker to fully start...
    timeout /t 30 /nobreak
)

echo Docker is ready!
docker --version

REM Proceed with build
cd /d C:\Users\Ravi Ranjan\OneDrive\Desktop\java-security-log-analyzer-main
echo.
echo Building Docker image (this will take 3-5 minutes on first run)...
docker build -t security-log-analyzer:2.0.0 -t security-log-analyzer:latest .

if %ERRORLEVEL% EQU 0 (
    echo.
    echo SUCCESS! Docker image built.
    echo.
    echo Run with: docker run -it security-log-analyzer:latest
    echo Or use:   docker-compose up
) else (
    echo.
    echo ERROR: Docker build failed
    exit /b 1
)
