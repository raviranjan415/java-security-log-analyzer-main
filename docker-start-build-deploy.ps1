# Starts Docker Desktop if needed, waits for daemon, builds image, and runs docker-compose
param(
    [int]$waitSeconds = 90
)

function Test-DockerRunning {
    try {
        docker ps > $null 2>&1
        return $LASTEXITCODE -eq 0
    } catch {
        return $false
    }
}

Write-Host "Docker start/build/deploy script" -ForegroundColor Cyan

# If Docker is not running, try to start Docker Desktop
if (-not (Test-DockerRunning)) {
    Write-Host "Docker daemon not running. Attempting to start Docker Desktop..." -ForegroundColor Yellow
    $paths = @(
        "C:\\Program Files\\Docker\\Docker\\Docker Desktop.exe",
        "C:\\Program Files\\Docker\\Docker\\Docker.exe",
        "C:\\Program Files (x86)\\Docker\\Docker\\Docker Desktop.exe"
    )
    $started = $false
    foreach ($p in $paths) {
        if (Test-Path $p) {
            Write-Host "Starting: $p" -ForegroundColor Gray
            Start-Process -FilePath $p -WindowStyle Minimized
            $started = $true
            break
        }
    }
    if (-not $started) {
        Write-Host "Could not find Docker Desktop executable. Please start Docker Desktop manually and re-run this script." -ForegroundColor Red
        exit 2
    }

    # Wait for Docker daemon to become available
    $elapsed = 0
    while (-not (Test-DockerRunning) -and $elapsed -lt $waitSeconds) {
        Start-Sleep -Seconds 5
        $elapsed += 5
        Write-Host "Waiting for Docker daemon... ($elapsed seconds)" -ForegroundColor Gray
    }

    if (-not (Test-DockerRunning)) {
        Write-Host "Docker daemon did not become ready within $waitSeconds seconds. Please start Docker Desktop manually and try again." -ForegroundColor Red
        exit 3
    }
}
else {
    Write-Host "Docker daemon already running." -ForegroundColor Green
}

# Build the Docker image
$projectDir = "c:\\Users\\Ravi Ranjan\\OneDrive\\Desktop\\java-security-log-analyzer-main"
Set-Location $projectDir

Write-Host "Building Docker image: security-log-analyzer:2.0.0" -ForegroundColor Cyan
$buildCmd = "docker build -t security-log-analyzer:2.0.0 -t security-log-analyzer:latest ."
Write-Host $buildCmd -ForegroundColor Gray

$proc = Start-Process -FilePath "docker" -ArgumentList @("build","-t","security-log-analyzer:2.0.0","-t","security-log-analyzer:latest",".") -NoNewWindow -Wait -PassThru
if ($proc.ExitCode -ne 0) {
    Write-Host "Docker build failed with exit code $($proc.ExitCode)" -ForegroundColor Red
    Write-Host "Run 'docker build --progress=plain .' for verbose output" -ForegroundColor Yellow
    exit 4
}

Write-Host "Docker image built successfully." -ForegroundColor Green

docker images | Select-String security-log-analyzer | ForEach-Object { Write-Host $_ }

# Run docker-compose (build and start services)
if (Test-Path (Join-Path $projectDir "docker-compose.yml")) {
    Write-Host "Running docker-compose up -d" -ForegroundColor Cyan
    $proc2 = Start-Process -FilePath "docker-compose" -ArgumentList @("up","-d","--build") -NoNewWindow -Wait -PassThru
    if ($proc2.ExitCode -ne 0) {
        Write-Host "docker-compose failed with exit code $($proc2.ExitCode)" -ForegroundColor Red
        Write-Host "You can try running 'docker-compose up' manually after ensuring Docker Desktop is running." -ForegroundColor Yellow
        exit 5
    }
    Write-Host "docker-compose services started." -ForegroundColor Green
    Write-Host "Use 'docker ps' to view running containers." -ForegroundColor Cyan
}
else {
    Write-Host "docker-compose.yml not found. Skipping compose. You can run the container manually:" -ForegroundColor Yellow
    Write-Host "  docker run -it security-log-analyzer:latest" -ForegroundColor Gray
}

Write-Host "Done." -ForegroundColor Cyan
exit 0
