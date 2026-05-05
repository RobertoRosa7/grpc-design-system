---
title: Referências e Fontes
type: editorial-instruction
version: 1.0
mandatory: true
applyTo: docs/ebook/manuscript/capitulos/**
---

# INSTRUÇÃO — REFERÊNCIAS E FONTES POR CAPÍTULO

## Obrigatoriedade

Todo capítulo do manuscrito **deve** encerrar com uma seção `## Referências` contendo as fontes que embasaram o conteúdo apresentado.

Esta seção é obrigatória e deve ser a **última seção do capítulo**, posicionada após `## Exercício` (quando existir).

---

## Estrutura Obrigatória da Seção

```markdown
## Referências

### Documentação Oficial

- [Nome do recurso](URL) — descrição de uma linha do que o link cobre

### Livros e Artigos

- SOBRENOME, Nome. **Título da obra**. Edição. Local: Editora, Ano. (capítulo ou seção relevante)

### RFCs e Especificações Técnicas

- RFC XXXX — _Título da RFC_. IETF, Ano. Disponível em: <URL>

### Repositórios e Exemplos de Código

- [nome/repositorio](URL) — descrição do que o repositório demonstra
```

---

## Regras de Formatação (ABNT NBR 6023:2018)

### Livros

```
SOBRENOME, Nome. **Título**: subtítulo. N. ed. Cidade: Editora, Ano.
```

Exemplo:

```
FOWLER, Martin. **Padrões de Arquitetura de Aplicações Corporativas**. Porto Alegre: Bookman, 2006.
```

### Documentos eletrônicos e sites

```
AUTOR ou ORGANIZAÇÃO. **Título do documento**. Local, Ano. Disponível em: <URL>. Acesso em: dia mês. ano.
```

Exemplo:

```
GOOGLE. **gRPC Documentation**. San Francisco, 2024. Disponível em: <https://grpc.io/docs/>. Acesso em: 4 maio 2026.
```

### RFCs

```
IETF. RFC XXXX: *Título*. [S.l.], Ano. Disponível em: <https://tools.ietf.org/html/rfcXXXX>.
```

---

## Fontes Recorrentes do Projeto

As fontes abaixo são base do livro e devem ser citadas nos capítulos em que forem diretamente relevantes:

| Fonte                                                                                       | Capítulos mais relevantes |
| ------------------------------------------------------------------------------------------- | ------------------------- |
| [grpc.io — Documentação oficial](https://grpc.io/docs/)                                     | 1, 2, 3, 7, 8             |
| [Protocol Buffers Language Guide (proto3)](https://protobuf.dev/programming-guides/proto3/) | 2, 6                      |
| [Spring gRPC — Spring Projects](https://spring.io/projects/spring-grpc)                     | 3, 5, 10                  |
| [grpc-java — GitHub](https://github.com/grpc/grpc-java)                                     | 3, 4, 5                   |
| FOWLER, Martin. **Patterns of Enterprise Application Architecture**. Addison-Wesley, 2002.  | 4, 9                      |
| MARTIN, Robert C. **Clean Architecture**. Prentice Hall, 2017.                              | 4, 9                      |
| VERNON, Vaughn. **Implementing Domain-Driven Design**. Addison-Wesley, 2013.                | 4, 17                     |
| [OpenTelemetry — Documentação](https://opentelemetry.io/docs/)                              | 12                        |
| [Resilience4j — Documentação](https://resilience4j.readme.io/)                              | 13                        |
| [RFC 7540 — HTTP/2](https://tools.ietf.org/html/rfc7540)                                    | 1, 7                      |
| [RFC 9113 — HTTP/2 (revisão)](https://tools.ietf.org/html/rfc9113)                          | 1, 7                      |
| [Kubernetes Documentation](https://kubernetes.io/docs/)                                     | 15                        |

---

## Checklist de Referências por Capítulo

Antes de considerar um capítulo concluído, verifique:

- [ ] A seção `## Referências` existe e é a última seção?
- [ ] A documentação oficial da tecnologia principal do capítulo está citada?
- [ ] RFCs relevantes estão referenciadas (ex.: RFC 7540 para HTTP/2)?
- [ ] Livros técnicos que embasam decisões arquiteturais estão listados?
- [ ] URLs foram verificadas e estão acessíveis?
- [ ] Formatação segue ABNT NBR 6023:2018?

---

## Profundidade Mínima por Tipo de Capítulo

| Tipo de capítulo                                 | Mínimo de referências |
| ------------------------------------------------ | --------------------- |
| Conceitual (fundamentos, teoria)                 | 4 referências         |
| Prático (implementação, código)                  | 3 referências         |
| Operacional (deploy, segurança, observabilidade) | 4 referências         |
| Estudo de caso                                   | 5 referências         |
| Apêndice                                         | 2 referências         |
