# 🐳 Docker Implementation Guide

## Overview

Docker containerizes your Java Security Log Analyzer application, enabling:
- **Consistent Environment** - Same Java version everywhere
- **Easy Distribution** - Single container image for all users
- **Isolation** - Runs independently from host system
- **Scalability** - Deploy on any Docker-compatible system
- **CI/CD Integration** - Automated builds and deployments

---

## Installation

### Check Docker Installation
```powershell
docker --version
docker run hello-world
```

Expected output: Docker version 20.10+ and "Hello from Docker!" message

### Install Docker
- **Windows**: [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop)
- **Mac**: [Docker Desktop for Mac](https://www.docker.com/products/docker-desktop)
- **Linux**: `sudo apt install docker.io` (Ubuntu/Debian)

---

## Project Structure

```
java-security-log-analyzer/
├── Dockerfile                   ← Docker build configuration
├── docker-compose.yml           ← Multi-container orchestration
├── .dockerignore                ← Files to exclude from build
├── pom.xml
├── src/
└── logins.txt
```

---

## 📦 Building the Docker Image

### Option 1: Using Docker Compose (Recommended)
```powershell
cd c:\Users\Ravi Ranjan\OneDrive\Desktop\java-security-log-analyzer-main
docker-compose build
```

### Option 2: Using Docker CLI
```powershell
cd c:\Users\Ravi Ranjan\OneDrive\Desktop\java-security-log-analyzer-main
docker build -t security-log-analyzer:2.0.0 .
docker build -t security-log-analyzer:latest .
```

### Build Output
```
Step 1/14 : FROM maven:3.9-eclipse-temurin-17 AS builder
Step 2/14 : WORKDIR /build
...
Step 14/14 : ENTRYPOINT ["java", "-jar", "security-log-analyzer-2.0.0.jar"]
Successfully built abc123def456
Successfully tagged security-log-analyzer:2.0.0
```

**Build Time**: ~3-5 minutes (first build downloads Maven base image)

---

## ▶️ Running the Docker Container

### Option 1: Using Docker Compose (GUI)
```powershell
docker-compose up security-analyzer
```

**On WSL2/Docker Desktop for Windows**:
- GUI window will launch automatically
- Close container with `Ctrl+C`

### Option 2: Using Docker CLI (GUI)
```powershell
docker run -it security-log-analyzer:latest
```

### Option 3: CLI Analyzer Mode (Headless)
```powershell
docker run -v ${PWD}/logins.txt:/app/logins.txt security-log-analyzer:latest java -cp security-log-analyzer-2.0.0.jar com.soc.analyzer.LogAnalyzer
```

### Option 4: Interactive Login Checker
```powershell
docker run -it security-log-analyzer:latest java -cp security-log-analyzer-2.0.0.jar com.soc.analyzer.LoginChecker
```

### Option 5: Docker Compose CLI Service
```powershell
docker-compose up analyzer-cli
```

---

## 🔧 Docker Commands Reference

### Image Management

| Command | Purpose |
|---------|---------|
| `docker build -t name:tag .` | Build image from Dockerfile |
| `docker images` | List all images |
| `docker image rm security-log-analyzer:2.0.0` | Remove image |
| `docker image prune` | Remove unused images |
| `docker image inspect security-log-analyzer` | View image details |
| `docker history security-log-analyzer` | View build layers |

### Container Management

| Command | Purpose |
|---------|---------|
| `docker run -it image-name` | Run container interactively |
| `docker run -d image-name` | Run container in background |
| `docker ps` | List running containers |
| `docker ps -a` | List all containers |
| `docker logs container-id` | View container logs |
| `docker exec -it container-id bash` | Execute command in running container |
| `docker stop container-id` | Stop running container |
| `docker rm container-id` | Remove container |

### Compose Commands

| Command | Purpose |
|---------|---------|
| `docker-compose build` | Build all services |
| `docker-compose up` | Start all services |
| `docker-compose up -d` | Start in background |
| `docker-compose down` | Stop and remove all services |
| `docker-compose logs` | View service logs |
| `docker-compose ps` | List running services |
| `docker-compose exec service-name bash` | Execute command in service |

---

## 📊 Working with Volumes (Data Persistence)

### Mount Logins File
```powershell
docker run -v C:\Users\YourName\Desktop\logins.txt:/app/logins.txt security-log-analyzer:latest
```

**On Windows** (use forward slashes or double backslashes):
```powershell
docker run -v C:/Users/Ravi Ranjan/Desktop/java-security-log-analyzer-main/logins.txt:/app/logins.txt security-log-analyzer:latest
```

### Using Docker Compose Volumes
```yaml
volumes:
  - ./logins.txt:/app/logins.txt          # Read custom data
  - ./security_report.txt:/app/output.txt # Write output
```

### Named Volumes (Persistent Storage)
```powershell
docker volume create analyzer-data
docker run -v analyzer-data:/app/data security-log-analyzer:latest
docker volume inspect analyzer-data
```

---

## 🌐 Environment Variables

### Set Environment Variables
```powershell
docker run -e JAVA_OPTS="-Xmx512m" security-log-analyzer:latest
```

### Docker Compose Environment
```yaml
environment:
  - DISPLAY=host.docker.internal:0
  - JAVA_OPTS=-Xmx512m
```

---

## 🐛 Troubleshooting

### Error: "Docker daemon is not running"
**Solution**: Start Docker Desktop
```powershell
# On Windows, either:
# 1. Click Docker Desktop icon
# 2. Or run: docker-machine start (if using Docker Toolbox)
```

### Error: "Cannot connect to X11 display" (Linux)
**Solution**: Forward X11 socket
```bash
docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY security-log-analyzer:latest
```

### Error: "Port 8080 is already in use"
**Solution**: Use different port mapping
```powershell
docker run -p 8081:8080 security-log-analyzer:latest
```

### Error: "Cannot find logins.txt"
**Solution**: Mount file or place in container
```powershell
docker run -v C:/path/to/logins.txt:/app/logins.txt security-log-analyzer:latest
```

### Error: "Out of memory"
**Solution**: Increase Docker memory limit
```powershell
docker run -m 1g security-log-analyzer:latest
```

### View Container Logs
```powershell
docker logs container-id
docker logs -f container-id  # Follow logs in real-time
```

### Access Container Shell
```powershell
docker exec -it container-id bash
ls -la /app
```

---

## 📈 Performance Optimization

### Dockerfile Best Practices
✅ Multi-stage build (reduces final image size)  
✅ Layer caching (reuse unchanged layers)  
✅ .dockerignore (exclude unnecessary files)  
✅ Non-root user (security)  

### Image Size
- **Stage 1 (Builder)**: ~500 MB (Maven + JDK 17)
- **Stage 2 (Runtime)**: ~300 MB (JRE 17 + JAR)
- **Recommended limit**: 500 MB

### Memory Management
```powershell
# Limit memory usage
docker run -m 512m security-log-analyzer:latest

# Monitor resource usage
docker stats
```

---

## 🚀 Advanced Usage

### Push to Docker Hub (Sharing)
```powershell
# Tag image
docker tag security-log-analyzer:latest username/security-log-analyzer:latest

# Login to Docker Hub
docker login

# Push image
docker push username/security-log-analyzer:latest

# Pull on another machine
docker pull username/security-log-analyzer:latest
```

### Create Custom Network
```powershell
docker network create analyzer-network
docker run --network analyzer-network --name analyzer security-log-analyzer:latest
```

### Run Multiple Containers
```powershell
docker run --name analyzer1 security-log-analyzer:latest
docker run --name analyzer2 security-log-analyzer:latest
docker ps  # See both running
```

### Debug Mode (Keep Container Running)
```powershell
docker run -it --entrypoint /bin/bash security-log-analyzer:latest
```

---

## 📋 Common Workflows

### Workflow 1: Build and Run GUI
```powershell
docker build -t security-log-analyzer:latest .
docker run -it security-log-analyzer:latest
```

### Workflow 2: Development Build (Rebuild every time)
```powershell
docker build -t security-log-analyzer:dev .
docker run -it security-log-analyzer:dev
```

### Workflow 3: Production Deployment
```powershell
# Build specific version
docker build -t security-log-analyzer:2.0.0 .

# Tag as latest
docker tag security-log-analyzer:2.0.0 security-log-analyzer:latest

# Push to registry
docker push myregistry.com/security-log-analyzer:2.0.0
```

### Workflow 4: With Custom Data
```powershell
docker run -v C:/data/logins.txt:/app/logins.txt security-log-analyzer:latest
```

### Workflow 5: Run Tests in Container
```powershell
docker run security-log-analyzer:latest mvn test
```

---

## 🔐 Security Considerations

### Best Practices
✅ Run as non-root user  
✅ Use read-only filesystems when possible  
✅ Scan images for vulnerabilities: `docker scan security-log-analyzer`  
✅ Use specific base image tags (not `latest`)  
✅ Keep base images updated  

### Scan for Vulnerabilities
```powershell
docker scan security-log-analyzer:latest
```

---

## 📚 Docker Compose Examples

### Minimal (GUI only)
```yaml
version: '3.8'
services:
  app:
    build: .
    stdin_open: true
    tty: true
```

### Full Stack (GUI + CLI)
```yaml
version: '3.8'
services:
  gui:
    build: .
    container_name: analyzer-gui
    stdin_open: true
    tty: true
  
  cli:
    build: .
    container_name: analyzer-cli
    entrypoint: bash
    stdin_open: true
    tty: true
```

---

## 🌍 Multi-Platform Builds

### Build for Multiple Architectures
```powershell
# Install buildx
docker buildx create --name mybuilder
docker buildx use mybuilder

# Build for multiple platforms
docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t username/security-log-analyzer:latest .
```

---

## 🔗 Useful Links

- Docker Docs: https://docs.docker.com/
- Docker Hub: https://hub.docker.com/
- Best Practices: https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
- Docker Compose Docs: https://docs.docker.com/compose/
- Security: https://docs.docker.com/engine/security/

---

## ✅ Verification Checklist

- [ ] Docker installed: `docker --version`
- [ ] Image built: `docker images | grep security-log-analyzer`
- [ ] Container runs: `docker run security-log-analyzer:latest`
- [ ] Volumes work: Data files accessible in container
- [ ] Logs visible: `docker logs container-id`
- [ ] Resource limits enforced: `docker stats`
- [ ] No security vulnerabilities: `docker scan`

---

**Last Updated**: May 12, 2026  
**Docker Version**: 29.2.1  
**Java Version**: 17  
**Base Images**: maven:3.9, eclipse-temurin:17-jre
