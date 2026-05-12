# Docker Build Script - Simple Version
Write-Host "Starting Docker daemon..." -ForegroundColor Cyan

# Start Docker Desktop
$dockerPath = "C:\Program Files\Docker\Docker\Docker.exe"
if (Test-Path $dockerPath) {
    Start-Process $dockerPath
    Write-Host "Waiting 45 seconds for Docker to initialize..." -ForegroundColor Yellow
    Start-Sleep -Seconds 45
} else {
    Write-Host "Docker Desktop not found. Please install it first." -ForegroundColor Red
    exit 1
}

# Navigate to project directory
cd "c:\Users\Ravi Ranjan\OneDrive\Desktop\java-security-log-analyzer-main"

# Build image
Write-Host ""
Write-Host "Building Docker image..." -ForegroundColor Cyan
docker build -t security-log-analyzer:2.0.0 -t security-log-analyzer:latest .

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "SUCCESS! Image built." -ForegroundColor Green
    docker images | grep security-log-analyzer
    Write-Host ""
    Write-Host "Run with: docker run -it security-log-analyzer:latest" -ForegroundColor Cyan
} else {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}
