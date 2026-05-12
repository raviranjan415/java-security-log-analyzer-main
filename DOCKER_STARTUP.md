# 🐳 Docker Setup Instructions for Windows

## ⚠️ Docker Daemon Not Running

Your Docker installation is configured but the Docker daemon service is not currently running.

---

## ✅ Step 1: Start Docker Desktop

### Option A: Using GUI (Recommended)
1. **Click Windows Start button** or press `Win` key
2. **Type**: `docker desktop`
3. **Click**: Docker Desktop application
4. **Wait**: Until you see the checkmark ✅ in the system tray (bottom right)
5. **Confirm**: Run `docker ps` in PowerShell to verify

### Option B: Using PowerShell (Admin)
```powershell
# Open PowerShell as Administrator
Start-Process "C:\Program Files\Docker\Docker\Docker.exe"

# Wait 45-60 seconds for startup, then verify:
docker ps
```

### Option C: Using Command Prompt
```cmd
start "" "C:\Program Files\Docker\Docker\Docker.exe"
REM Wait 45-60 seconds and verify:
docker ps
```

---

## ✅ Step 2: Verify Docker is Running

Once Docker Desktop is open, run:

```powershell
docker --version
docker ps
```

Expected output:
```
Docker version 29.2.1, build a5c7197
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

---

## ✅ Step 3: Build Docker Image

Once Docker is running, build the image:

```powershell
cd "C:\Users\Ravi Ranjan\OneDrive\Desktop\java-security-log-analyzer-main"
docker build -t security-log-analyzer:2.0.0 -t security-log-analyzer:latest .
```

**First build time**: 3-5 minutes (downloads Maven and Java base images)  
**Subsequent builds**: 30-60 seconds (uses cached layers)

---

## ✅ Step 4: Verify Image Was Created

```powershell
docker images | grep security-log-analyzer
```

Expected output:
```
REPOSITORY                 TAG       IMAGE ID       CREATED          SIZE
security-log-analyzer      2.0.0     abc123def456   2 minutes ago     350MB
security-log-analyzer      latest    abc123def456   2 minutes ago     350MB
```

---

## ✅ Step 5: Run the Application

### Option 1: Run GUI Application
```powershell
docker run -it security-log-analyzer:latest
```

### Option 2: Run with Docker Compose
```powershell
docker-compose up
```

### Option 3: Run CLI Tools
```powershell
# Log Analyzer
docker run security-log-analyzer:latest java -cp security-log-analyzer-2.0.0.jar com.soc.analyzer.LogAnalyzer

# Login Checker
docker run -it security-log-analyzer:latest java -cp security-log-analyzer-2.0.0.jar com.soc.analyzer.LoginChecker
```

---

## ⏱️ Timeline

| Step | Action | Time |
|------|--------|------|
| 1 | Start Docker Desktop | 45-60 seconds |
| 2 | Verify `docker ps` works | < 5 seconds |
| 3 | Build image (first time) | 3-5 minutes |
| 4 | Verify image created | < 5 seconds |
| 5 | Run container | Instant |
| **Total** | **All steps** | **5-8 minutes** |

---

## 🔧 Troubleshooting

### Docker Desktop Won't Start
- **Check**: Is the file `C:\Program Files\Docker\Docker\Docker.exe` present?
- **Reinstall**: https://www.docker.com/products/docker-desktop
- **WSL2**: Ensure Windows Subsystem for Linux 2 is installed

### Cannot find docker command
- **Add to PATH**: Ensure Docker bin directory is in system PATH
- **Restart PowerShell**: Close and reopen terminal
- **Verify**: `where docker`

### "failed to connect to the docker API"
- **Cause**: Docker daemon is not running
- **Fix**: Start Docker Desktop (see Step 1 above)

### Build fails with "base image not found"
- **Cause**: No internet connection
- **Fix**: Check network and run: `docker pull maven:3.9-eclipse-temurin-17`

### Out of memory during build
- **Fix**: Increase Docker's memory limit in Docker Desktop Settings:
  - Click Docker icon → Settings → Resources → Memory → Set to 4GB minimum

---

## 📋 Quick Reference

```powershell
# Verify Docker is running
docker ps

# Build image (from project directory)
docker build -t security-log-analyzer:latest .

# List all images
docker images

# Run container interactively
docker run -it security-log-analyzer:latest

# View running containers
docker ps

# View all containers
docker ps -a

# Stop container
docker stop container-id

# Remove image
docker rmi security-log-analyzer:latest

# View build logs
docker build --progress=plain .

# Debug - open shell in container
docker run -it security-log-analyzer:latest /bin/bash
```

---

## 📚 Next Steps

1. ✅ Start Docker Desktop (see Step 1)
2. ✅ Verify with `docker ps`
3. ✅ Build image: `docker build -t security-log-analyzer:latest .`
4. ✅ Run application: `docker run -it security-log-analyzer:latest`
5. 📖 See DOCKER_GUIDE.md for advanced usage

---

## 🔗 Additional Resources

- Docker Docs: https://docs.docker.com/
- Troubleshooting: https://docs.docker.com/desktop/troubleshoot/
- WSL2 Setup: https://docs.docker.com/desktop/install/windows-install/#wsl-2-backend

---

**Created**: May 12, 2026  
**Docker Version**: 29.2.1  
**Status**: Waiting for Docker daemon to start
