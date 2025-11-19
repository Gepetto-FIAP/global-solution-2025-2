# Script PowerShell para executar a aplicacao

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Catalogo de Habilidades API" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Carregar variaveis de ambiente do .env.local
$envFile = "../.env.local"
if (Test-Path $envFile) {
    Write-Host "Carregando variaveis de ambiente..." -ForegroundColor Yellow
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^(ORACLE_USER|ORACLE_PASSWORD|ORACLE_CONNECT_STRING|JWT_SECRET|JWT_EXPIRATION)=(.*)$') {
            $key = $matches[1]
            $value = $matches[2]
            [Environment]::SetEnvironmentVariable($key, $value, "Process")
        }
    }
    Write-Host "Variaveis carregadas" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "Arquivo .env.local nao encontrado" -ForegroundColor Yellow
    Write-Host "Usando valores padrao ou variaveis de ambiente do sistema"
    Write-Host ""
}

# Compilar se necessario
if (-not (Test-Path "target/habilidades-api.jar")) {
    Write-Host "Compilando projeto..." -ForegroundColor Yellow
    mvn clean package -DskipTests
    Write-Host ""
}

# Executar
Write-Host "Iniciando servidor..." -ForegroundColor Green
Write-Host "Servidor: http://localhost:8080" -ForegroundColor Cyan
Write-Host "API: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host ""
Write-Host "Pressione Ctrl+C para encerrar" -ForegroundColor Yellow
Write-Host ""

java -jar target/habilidades-api.jar
