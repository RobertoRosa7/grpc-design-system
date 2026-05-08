# Guia de Agentes do Projeto

Este repositório usa agentes voltados para a criação, revisão e divulgação de um ebook técnico.

## Agente disponível

### Redator Técnico (`.github/agents/redator-tecnico.agent.md`)

Especialista em escrita de livros técnicos para publicação digital (incluindo Amazon KDP).

Use este agente para:

- Estruturar sumário e progressão didática dos capítulos
- Escrever conteúdo técnico com clareza e profundidade
- Revisar consistência editorial e terminologia
- Propor exemplos de código e exercícios práticos
- Adaptar linguagem para público iniciante, intermediário ou avançado
- Apoiar textos de divulgação (descrição do livro, copy curta e bullets)

## Como acionar

Exemplos de chamada no chat:

- `@redator-tecnico criar estrutura do capítulo sobre gRPC e Spring Boot`
- `@redator-tecnico revisar este trecho para deixar mais didático`
- `@redator-tecnico gerar descrição para Amazon KDP`

### Revisor de Linguagem (`.github/agents/revisor-linguagem.agent.md`)

Especialista em língua portuguesa e normas ABNT para publicações técnicas.

Use este agente para:

- Revisar ortografia, gramática, pontuação e concordância
- Verificar conformidade com o Acordo Ortográfico de 2009
- Aplicar normas ABNT (NBR 6022, 6023, 6024, 6027, 10520, 14724)
- Padronizar tratamento de siglas, termos técnicos em inglês e numerais
- Identificar inconsistências de estilo (pessoa gramatical, voz ativa/passiva)
- Revisar títulos de seções, sumário e numeração progressiva

Exemplos de chamada no chat:

- `@revisor-linguagem revisar o capítulo 1 para erros ortográficos`
- `@revisor-linguagem verificar conformidade ABNT do sumário`
- `@revisor-linguagem padronizar siglas e termos em inglês no texto`
- `@revisor-linguagem relatório de erros do trecho abaixo`

### KDP PDF 7x10 (`.github/agents/kdp-pdf-7x10.agent.md`)

Especialista em configuração e conformidade de PDF para Amazon KDP no formato 7x10.

Use este agente para:

- Corrigir erros de "texto fora das margens"
- Corrigir erros de "objeto fora das margens"
- Ajustar medianiz/margens para livros longos
- Eliminar páginas em branco no miolo
- Corrigir capa quebrada (fragmento em página seguinte)
- Validar header/footer dentro da área segura

Exemplos de chamada no chat:

- `@kdp-pdf-7x10 revisar erros do preview KDP`
- `@kdp-pdf-7x10 ajustar PDF para passar nas regras da Amazon`
- `@kdp-pdf-7x10 validar configuração final 7x10`

---

## Escopo deste repositório

Projeto de exemplo para criação e divulgação de ebook técnico.
Foco em conteúdo didático, qualidade editorial e material pronto para publicação.
