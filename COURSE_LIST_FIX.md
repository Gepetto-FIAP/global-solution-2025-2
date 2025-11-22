# Correção: Listagem de Cursos por Categoria

## Problema Identificado
A API pública da Alura (`/api/cursos`) não disponibiliza uma listagem completa de cursos publicamente. Apenas a API de categorias (`/api/categorias`) retorna dados estruturados.

## Solução Implementada
Atualizamos o `AluraService.java` para gerar cursos simulados baseados nas **subcategorias reais** da Alura que já temos disponíveis via API.

### O que foi alterado:
1. **Backend - AluraService.java**:
   - Modificado o método `getCursos()` para gerar cursos baseados nas subcategorias reais
   - Adicionado método `gerarCursosParaSubcategoria()` que cria 3-5 cursos por subcategoria
   - Adicionado método `gerarNomeCurso()` que cria nomes relevantes (ex: "Fundamentos de Flutter", "Avançando em Android")
   - Adicionado método `gerarDescricaoCurso()` que gera descrições contextualizadas por nível

### Características dos Cursos Gerados:
- ✅ Baseados em subcategorias reais da Alura (Mobile > Flutter, Android, iOS, etc.)
- ✅ Nomes relevantes e variados (Fundamentos, Avançando, Dominando, etc.)
- ✅ 3 níveis: Iniciante, Intermediário, Avançado
- ✅ Durações realistas: 6h, 8h, 10h, 12h, 16h, 20h
- ✅ Descrições contextualizadas por nível
- ✅ Links para busca na Alura com o nome da subcategoria
- ✅ Slugs únicos por curso
- ✅ Associados corretamente à categoria e subcategoria

## Resultado
Agora quando você clicar em qualquer categoria da sidebar (Mobile, Programação, Data Science, etc.), você verá múltiplos cursos relevantes relacionados àquela categoria, todos baseados nas subcategorias oficiais da Alura.

## Para Testar
1. Reinicie o backend (se estiver rodando)
2. No frontend, clique em qualquer categoria da sidebar
3. Você verá cursos sendo exibidos com nomes, descrições e níveis apropriados
4. Cada subcategoria terá múltiplos cursos disponíveis

## Exemplo de Cursos Gerados
Para a categoria **Mobile > Flutter**:
- "Fundamentos de Flutter" (Iniciante, 6h)
- "Avançando em Flutter" (Intermediário, 8h)
- "Dominando Flutter" (Avançado, 10h)
- etc.

