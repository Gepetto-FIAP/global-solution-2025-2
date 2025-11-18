#!/bin/bash

# Script simples para executar a aplicação

echo "=========================================="
echo "Catálogo de Habilidades API"
echo "=========================================="
echo ""

# Carregar variáveis de ambiente do .env.local
ENV_FILE="../.env.local"
if [ -f "$ENV_FILE" ]; then
    echo "📋 Carregando variáveis de ambiente de $ENV_FILE..."
    # Carregar variáveis do arquivo .env.local
    export $(grep -E '^(ORACLE_USER|ORACLE_PASSWORD|ORACLE_CONNECT_STRING|JWT_SECRET|JWT_EXPIRATION)=' "$ENV_FILE" | grep -v '^#' | xargs)
    echo "  ✓ Variáveis carregadas"
    echo ""
else
    echo "⚠️  Arquivo .env.local não encontrado em $ENV_FILE"
    echo "   Usando valores padrão ou variáveis de ambiente do sistema"
    echo ""
fi

# Compilar se necessário
if [ ! -f "target/habilidades-api.jar" ]; then
    echo "📦 Compilando projeto..."
    mvn clean package -DskipTests
    echo ""
fi

# Executar
echo "🚀 Iniciando servidor..."
echo "Servidor: http://localhost:8080"
echo "API: http://localhost:8080/api"
echo ""
echo "Pressione Ctrl+C para encerrar"
echo ""

java -jar target/habilidades-api.jar
