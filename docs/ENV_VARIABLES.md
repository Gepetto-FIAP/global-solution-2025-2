# Variáveis de Ambiente

Este documento descreve as variáveis de ambiente necessárias para o projeto.

## Arquivo .env.local

Crie um arquivo `.env.local` na raiz do projeto com as seguintes variáveis:

```env
# URL base do backend Java REST API
# Em desenvolvimento, geralmente será http://localhost:8080
# Em produção, substitua pela URL do servidor
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## Variáveis

### NEXT_PUBLIC_API_URL

- **Tipo**: String (URL)
- **Obrigatória**: Sim
- **Descrição**: URL base do backend Java REST API
- **Exemplo**: `http://localhost:8080`
- **Nota**: Variáveis que começam com `NEXT_PUBLIC_` são expostas ao cliente. Não inclua informações sensíveis aqui.

## Configuração do Backend Java

O backend Java precisará das seguintes variáveis de ambiente (não são do Next.js):

```env
# Oracle Database
ORACLE_USER=seu_usuario
ORACLE_PASSWORD=sua_senha
ORACLE_CONNECT_STRING=sua_conexao

# JWT Secret (chave secreta para assinar tokens JWT)
JWT_SECRET=sua-chave-secreta-super-forte-altere-em-producao
```

## Como Usar

1. Copie o conteúdo acima para um arquivo `.env.local` na raiz do projeto
2. Ajuste os valores conforme seu ambiente
3. O arquivo `.env.local` está no `.gitignore` e não será commitado
4. Reinicie o servidor de desenvolvimento após alterar variáveis de ambiente

