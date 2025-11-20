# 🚀 Deploy Manual Azure - Portal Web

## ⚠️ Sua assinatura Azure for Students tem restrições de região

Use o **Portal Azure** para deploy manual - é mais simples!

---

## 📋 Passo a Passo Simples

### 1️⃣ Acesse o Portal Azure
🌐 **https://portal.azure.com**

### 2️⃣ Criar App Service para Backend Java

1. **Clique em "Create a resource"**
2. **Busque**: "Web App"
3. **Clique**: "Create"

**Configure:**
- **Resource Group**: Crie novo `skillify-rg`
- **Name**: `skillify-backend-seu-rm` (ex: skillify-backend-558857)
- **Publish**: Code
- **Runtime stack**: Java 21
- **Java web server stack**: Java SE (Embedded Web Server)
- **Operating System**: Linux
- **Region**: **Deixe o padrão** (Azure escolhe automaticamente)
- **Pricing plan**: Free F1

4. **Clique**: "Review + create" → "Create"
5. **Aguarde** ~2 minutos

---

### 3️⃣ Configurar Variáveis de Ambiente

No seu App Service criado:

1. **Menu lateral**: Configuration → Application settings
2. **Clique**: "New application setting" para cada:

```
ORACLE_USER = RM558857
ORACLE_PASSWORD = 240903
ORACLE_CONNECT_STRING = oracle.fiap.com.br:1521/orcl
JWT_SECRET = skillify-secret-key-super-secure-2025
JWT_EXPIRATION = 86400000
```

3. **Clique**: "Save" no topo

---

### 4️⃣ Deploy do Backend

**Opção A: Via Azure CLI** (no seu PC)
```powershell
# Já compilado! Basta fazer deploy
az webapp deploy --resource-group skillify_rg --name skillify-back-rm558857 --src-path backend/target/habilidades-api.jar --type jar
```

**Opção B: Via FTP** (sem Azure CLI)
1. No App Service → Deployment Center
2. Escolha: FTP
3. Copie credenciais FTP
4. Use FileZilla ou WinSCP
5. Faça upload do arquivo: `backend/target/habilidades-api.jar`

**Opção C: Via GitHub Actions** (recomendado)
1. No App Service → Deployment Center
2. Escolha: GitHub
3. Autorize e selecione:
   - Repo: `Gepetto-FIAP/global-solution-2025-2`
   - Branch: `feat/luiz`
4. Azure cria workflow automaticamente!

---

### 5️⃣ Deploy do Frontend na Vercel

**Frontend não pode ir na Azure** (plano gratuito tem limitações)

Use a **Vercel** (é instantâneo):

```powershell
# Instalar Vercel CLI
npm i -g vercel

# Deploy
vercel --prod
```

**Ou pelo site:**
1. Acesse: https://vercel.com
2. Import Git Repository
3. Conecte: `Gepetto-FIAP/global-solution-2025-2`
4. Branch: `feat/luiz`
5. **Environment Variables**:
   ```
   NEXT_PUBLIC_API_URL=https://skillify-back-rm558857-crfrhphvb6dzcmgw.canadacentral-01.azurewebsites.net
   ```
6. Deploy!

---

## ✅ Testar

**Backend**:
```powershell
curl https://skillify-back-rm558857-crfrhphvb6dzcmgw.canadacentral-01.azurewebsites.net/api/auth/login
```

**Frontend**:
```
https://seu-projeto.vercel.app
```

---

## 🔍 Ver Logs do Backend

**No Portal Azure:**
1. Seu App Service → Log stream
2. Ou use: Monitoring → Logs

**Via CLI:**
```powershell
az webapp log tail --name skillify-back-rm558857 --resource-group skillify_rg
```

---

## 💡 Arquitetura Final

```
Frontend (Next.js)  → Vercel (GRÁTIS, sem limites)
Backend (Java 21)   → Azure App Service (F1, GRÁTIS)
Database            → Oracle FIAP (já configurado)
```

---

## ❌ Limpar Recursos (quando não precisar mais)

```powershell
az group delete --name skillify_rg --yes
```

Ou no Portal: Resource Groups → skillify_rg → Delete

---

## 🎯 Resumo Rápido

1. ✅ **Portal Azure** → Criar Web App (Java 21)
2. ✅ **Configuration** → Adicionar variáveis ambiente
3. ✅ **Deploy** → Upload JAR ou GitHub Actions
4. ✅ **Vercel** → Deploy frontend
5. ✅ **Testar** e pronto! 🎉

**Tempo total: ~10 minutos**
