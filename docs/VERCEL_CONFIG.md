# Configuração da Vercel

## Variáveis de Ambiente

Para que o frontend Next.js implantado na Vercel se comunique corretamente com o backend Azure, você precisa configurar a seguinte variável de ambiente:

### Passo a Passo:

1. **Acesse o projeto na Vercel**:
   - Vá para [https://vercel.com](https://vercel.com)
   - Faça login e selecione o projeto `global-solution-2025-2`

2. **Navegue até Settings**:
   - Clique na aba **Settings**
   - No menu lateral, clique em **Environment Variables**

3. **Adicione a variável**:
   ```
   Name: NEXT_PUBLIC_API_URL
   Value: https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net
   ```

4. **Selecione os ambientes**:
   - ✅ Production
   - ✅ Preview
   - ✅ Development

5. **Salve e reimplante**:
   - Clique em **Save**
   - Vá para **Deployments**
   - Clique nos três pontos do último deployment
   - Selecione **Redeploy**

## Verificação

Após a reimplantação, teste o login na aplicação:

1. Acesse: https://global-solution-2025-2.vercel.app/auth/login
2. Tente fazer login (mesmo sem usuário cadastrado)
3. Você deve ver uma mensagem de erro clara (não "Failed to fetch")
4. Verifique o console do navegador - não deve haver erros de CORS

## URLs Configuradas

### Frontend (Vercel):
- **Produção**: https://global-solution-2025-2.vercel.app
- **Preview (feat/luiz)**: https://global-solution-2025-2-git-feat-luiz-gepetto-fiap.vercel.app

### Backend (Azure):
- **API**: https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net
- **Endpoint de Login**: https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net/api/auth/login
- **Endpoint de Registro**: https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net/api/auth/register

## Status Atual

✅ **Backend Azure**: Operacional  
✅ **CORS Configurado**: Aceita requisições da Vercel  
✅ **Conexão Oracle**: Funcionando  
✅ **JWT Authentication**: Implementado  
✅ **GitHub Actions**: Deployment automático configurado  

⚠️ **Pendente**: Configurar variável de ambiente na Vercel

## Troubleshooting

### Se ainda houver erro "Failed to fetch":

1. **Verifique se a variável foi salva corretamente**:
   - Settings > Environment Variables
   - Deve aparecer `NEXT_PUBLIC_API_URL`

2. **Limpe o cache e reimplante**:
   - Deployments > último deployment
   - Três pontos > Redeploy
   - ✅ Marque "Use existing Build Cache" como **desmarcado**

3. **Verifique no console do navegador**:
   ```javascript
   console.log(process.env.NEXT_PUBLIC_API_URL)
   ```
   Deve mostrar: `https://skillify-ake2azefc0bqhrgt.canadacentral-01.azurewebsites.net`

4. **Verifique os logs da Vercel**:
   - Deployments > Build logs
   - Procure por erros durante o build

### Se houver erro de CORS:

O CORS está configurado para aceitar estas origens:
- `http://localhost:3000` (desenvolvimento local)
- `https://global-solution-2025-2.vercel.app` (produção)
- `https://global-solution-2025-2-git-feat-luiz-gepetto-fiap.vercel.app` (preview)

Se a Vercel usar uma URL diferente, adicione-a em `backend/src/main/java/com/catalogo/habilidades/config/CorsFilter.java`.

## Próximos Passos

Após configurar a Vercel:

1. ✅ Testar login e registro
2. ✅ Verificar se o JWT está sendo armazenado
3. ✅ Testar navegação autenticada
4. ✅ Verificar persistência de sessão
5. ✅ Testar logout

## Recursos Adicionais

- **Azure App Service**: Portal Azure > skill_group > skillify
- **GitHub Actions**: [Workflows](https://github.com/Gepetto-FIAP/global-solution-2025-2/actions)
- **Oracle Database**: oracle.fiap.com.br:1521/orcl (RM558857)
