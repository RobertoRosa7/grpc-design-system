---
title: Formatacao de Codigo e Diagramas
type: editorial-instruction
version: 1.0
mandatory: true
applyTo: docs/ebook/manuscript/**
---

# INSTRUCAO - FORMATACAO DE CODIGO E DIAGRAMAS

## Missao

Garantir consistencia visual dos exemplos tecnicos no livro, evitando overflow de layout e padronizando exemplos graficos com PlantUML.

---

## Regra 1 - Identacao e largura de linha em exemplos de codigo

### Identacao obrigatoria

- Todo bloco de codigo em markdown deve usar **2 espacos** por nivel de identacao.
- Quando houver tabulacao, considerar **tab size = 2**.
- Nao usar identacao de 4 espacos nos exemplos do manuscrito.

### Largura de linha obrigatoria

- Evitar linhas longas que estouram o limite visual da div de codigo no render.
- Preferir limite de **100 caracteres por linha** em exemplos.
- Se necessario, quebrar chamadas encadeadas, argumentos, JSONs e comandos longos em multiplas linhas.

### Regras praticas para evitar overflow

- Em Java, quebrar fluent API em uma chamada por linha.
- Em JSON, usar formatacao multiline com um campo por linha quando o payload passar de 100 caracteres.
- Em comandos shell longos, usar continuacao de linha.
- Em URLs longas dentro de codigo, extrair para variavel quando aplicavel.

---

## Regra 2 - Todo exemplo grafico deve usar PlantUML

### Padrao obrigatorio

- Todo exemplo grafico do manuscrito deve ser modelado em **PlantUML**.
- Nao usar diagramas ascii para versao final dos capitulos.
- Nao usar diagramas desenhados manualmente sem fonte editavel.

### Artefatos obrigatorios por diagrama

Para cada diagrama citado no capitulo, criar:

1. Arquivo fonte `.puml`
2. Imagem gerada (`.svg` preferencialmente; `.png` quando necessario)

### Convencao de local e nome

- Salvar em `docs/ebook/assets/images/`
- Nomear com prefixo do capitulo:
  - `cap02-fluxo-geracao-documento.puml`
  - `cap02-fluxo-geracao-documento.svg`

### Inclusao no markdown

- Referenciar sempre a imagem gerada no capitulo.
- Manter o arquivo `.puml` versionado junto para evolucao futura.

---

## Checklist obrigatorio por capitulo

- [ ] Todos os codigos usam identacao de 2 espacos (ou tab=2)?
- [ ] Linhas longas foram quebradas para evitar overflow visual?
- [ ] Todo diagrama do capitulo tem fonte `.puml`?
- [ ] Todo `.puml` possui imagem `.svg` ou `.png` correspondente?
- [ ] O markdown referencia a imagem gerada, nao um desenho ascii?

---

## Criterio de qualidade

Se o conteudo renderiza sem overflow horizontal nos blocos de codigo e todos os diagramas sao reproduziveis a partir de PlantUML, a instrucao foi cumprida.
