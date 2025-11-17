#!/bin/bash

# Script para executar o backend Spring Boot com variáveis de ambiente

# Navegar para o diretório do backend
cd "$(dirname "$0")"

# Carregar variáveis de ambiente do .env.local (se existir)
if [ -f "../.env.local" ]; then
    echo "Carregando variáveis de ambiente de ../.env.local"
    export $(grep -v '^#' ../.env.local | grep -v '^$' | xargs)
fi

# Verificar se as variáveis necessárias estão definidas
if [ -z "$ORACLE_USER" ] || [ -z "$ORACLE_PASSWORD" ] || [ -z "$ORACLE_CONNECT_STRING" ]; then
    echo "ERRO: Variáveis de ambiente não encontradas!"
    echo "Por favor, configure as seguintes variáveis:"
    echo "  - ORACLE_USER"
    echo "  - ORACLE_PASSWORD"
    echo "  - ORACLE_CONNECT_STRING"
    echo ""
    echo "Você pode exportá-las manualmente ou criar um arquivo ../.env.local"
    exit 1
fi

echo "Variáveis de ambiente configuradas:"
echo "  ORACLE_USER: $ORACLE_USER"
echo "  ORACLE_CONNECT_STRING: $ORACLE_CONNECT_STRING"
echo ""

# Executar o Spring Boot
mvn spring-boot:run

