# 🚀 Deployment Guide - Global Solution 2025

## Arquitetura da Aplicação

```
┌─────────────────────┐         ┌──────────────────────┐         ┌─────────────────┐
│   Vercel (Frontend) │────────>│  Azure App Service   │────────>│  Oracle FIAP    │
│   Next.js 16.0.3    │  HTTPS  │  Java 21 + Jersey    │  JDBC   │  Database       │
│                     │<────────│  REST API            │<────────│                 │
└─────────────────────┘         └──────────────────────┘         └─────────────────┘
```

## Status Atual ✅

### Frontend (Vercel)
- ✅ Implantado em: https://global-solution-2025-2.vercel.app
- ✅ Preview branch: https://global-solution-2025-2-git-feat-luiz-gepetto-fiap.vercel.app
- ⚠️ **Ação necessária**: Configurar variável `NEXT_PUBLIC_API_URL` (veja [VERCEL_CONFIG.md](./VERCEL_CONFIG.md))

### Backend (Azure)
- ✅ URL: https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net
- ✅ App Service: skillify (B1 Basic tier)
- ✅ Resource Group: skill_group
- ✅ Região: Canada Central
- ✅ Runtime: Java 21 (Temurin)
- ✅ Startup Command: `java -jar /home/site/wwwroot/app.jar`
- ✅ CORS: Configurado para localhost + Vercel
- ✅ Environment Variables: Configuradas via Portal Azure

### Database (Oracle)
- ✅ Servidor: oracle.fiap.com.br:1521/orcl
- ✅ Usuário: RM558857
- ✅ Conexão: HikariCP pool
- ✅ Tabelas: USUARIO criada

## Deployment Automático

### GitHub Actions - feat/luiz branch

Quando você faz push para a branch `feat/luiz` com mudanças em `backend/**`, o workflow automático:

1. ✅ Faz checkout do código
2. ✅ Configura Java 21
3. ✅ Compila com Maven (`mvn clean package -DskipTests`)
4. ✅ Autentica no Azure
5. ✅ Implanta no App Service usando `azure/webapps-deploy@v3`

**Arquivo**: `.github/workflows/deploy-feat-luiz.yml`

### GitHub Actions - main branch

Mesmo processo, mas para produção na branch `main`.

**Arquivo**: `.github/workflows/deploy-main.yml`

## Configuração Manual (se necessário)

### Backend Azure

```powershell
# Build local
cd backend
mvn clean package -DskipTests

# Deploy para Azure
az webapp deploy --resource-group skill_group --name skillify --src-path target/habilidades-api.jar --type jar
```

### Frontend Vercel

O deployment é automático via integração GitHub → Vercel.

Para forçar redeploy:
1. Acesse Vercel Dashboard
2. Deployments > último deployment
3. Três pontos > Redeploy

## Variáveis de Ambiente

### Backend (Azure App Service)

Configuradas via Portal Azure:

```bash
ORACLE_USER=RM558857
ORACLE_PASSWORD=240903
ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl
JWT_SECRET=chave_secreta_muito_segura_aqui_mude_isso_em_producao
JWT_EXPIRATION=604800000
PORT=8080  # Definido automaticamente pelo Azure
```

### Frontend (Vercel)

**⚠️ IMPORTANTE**: Configure manualmente no Vercel Dashboard:

```bash
NEXT_PUBLIC_API_URL=https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net
```

Veja instruções detalhadas em [VERCEL_CONFIG.md](./VERCEL_CONFIG.md)

## Endpoints da API

### Autenticação

#### POST /api/auth/register
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

Resposta (201):
```json
{
  "success": true,
  "message": "Usuário cadastrado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST /api/auth/login
```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

Resposta (200):
```json
{
  "success": true,
  "message": "Login realizado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## Testes

### Testar Backend (local)

```powershell
cd backend
./run.ps1
```

Servidor inicia em: http://localhost:8080

### Testar Backend (Azure)

```powershell
# OPTIONS (CORS preflight)
curl -Method OPTIONS -Uri "https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net/api/auth/login" -Headers @{"Origin"="https://global-solution-2025-2.vercel.app"}

# POST (Login)
curl -Method POST -Uri "https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net/api/auth/login" -ContentType "application/json" -Body '{"email":"test@test.com","password":"test123"}'
```

### Testar Frontend (local)

```bash
npm run dev
```

Acesse: http://localhost:3000

## Logs e Troubleshooting

### Azure App Service Logs

```powershell
# Streaming ao vivo
az webapp log tail --name skillify --resource-group skill_group

# Download de logs
az webapp log download --name skillify --resource-group skill_group --log-file logs.zip
```

### Vercel Logs

1. Acesse Vercel Dashboard
2. Selecione o projeto
3. Deployments > último deployment
4. Clique em "View Function Logs" ou "Build Logs"

## Resolução de Problemas Comuns

### "Failed to fetch" na Vercel

**Causa**: Variável `NEXT_PUBLIC_API_URL` não configurada ou CORS bloqueando.

**Solução**:
1. Configure a variável no Vercel (veja [VERCEL_CONFIG.md](./VERCEL_CONFIG.md))
2. Verifique se a origem está em `CorsFilter.java`
3. Reimplante o backend se mudou o CORS

### Backend retorna 503

**Causa**: Aplicação não iniciou ou crashou.

**Solução**:
1. Verifique logs: `az webapp log tail`
2. Confirme startup command: `java -jar /home/site/wwwroot/app.jar`
3. Verifique se o JAR existe: Azure Portal > SSH > `ls -la /home/site/wwwroot/`

### "Email ou senha incorretos"

**Causa**: Usuário não existe no banco ou senha errada.

**Solução**:
1. Primeiro faça registro via `/api/auth/register`
2. Depois faça login com as mesmas credenciais
3. Senhas são hasheadas com bcrypt - não podem ser consultadas diretamente

### GitHub Actions falhando

**Causa**: Secrets não configurados ou expirados.

**Solução**:
1. Verifique secrets no GitHub: Settings > Secrets and variables > Actions
2. Necessários: `AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID`
3. Reconfigure se necessário via Azure Portal > Entra ID

## Custos Estimados

- **Vercel**: Hobby (gratuito) - 100GB bandwidth, unlimited requests
- **Azure App Service B1**: ~$13-15 USD/mês (~R$ 60-70)
- **Oracle Database FIAP**: Fornecido pela instituição (gratuito)

**Total**: ~R$ 60-70/mês

## Próximos Passos

1. ⚠️ **Configurar variável na Vercel** (crítico)
2. ✅ Testar login/registro end-to-end
3. ✅ Implementar outras funcionalidades (habilidades, etc.)
4. ✅ Adicionar testes automatizados
5. ✅ Configurar monitoramento (Application Insights)
6. ✅ Adicionar health check endpoint
7. ✅ Implementar rate limiting
8. ✅ Configurar backup do banco

## Contatos e Recursos

- **GitHub Repo**: https://github.com/Gepetto-FIAP/global-solution-2025-2
- **Azure Portal**: https://portal.azure.com
- **Vercel Dashboard**: https://vercel.com/dashboard

---

**Última atualização**: 20 de novembro de 2025  
**Branch atual**: feat/luiz  
**Status**: ✅ Backend operacional, ⚠️ Frontend aguardando configuração Vercel
