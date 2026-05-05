---
title: DDD e Referencias Base
type: ddd-instruction
version: 1.0
mandatory: true
applyTo: docs/ebook/manuscript/capitulos/**
---

# INSTRUCTIONS - DDD E REFERÊNCIAS BASE

## Missao

Ancorar todo conteudo tecnico do livro nos conceitos de Domain-Driven Design e no modelo de design de sistemas de Chip Huyen. Os dois livros listados abaixo sao a base bibliografica principal do projeto e devem ser citados sempre que o conteudo do capitulo tiver correspondencia com seus temas.

---

## Livros Base Obrigatorios

### Livro 1 — DDD Aplicado

**KHONONOV, Vlad. Aprenda Domain-Driven Design: Alinhando Arquitetura de Software e Estrategia de Negocio. Sebastopol: O'Reilly Media, 2022.**

Este livro e a referencia principal para tudo que envolve:

- Bounded Contexts (Contextos Delimitados)
- Ubiquitous Language (Linguagem Onipresente)
- Aggregates, Entities e Value Objects
- Domain Events
- Subdominios (Core, Supporting, Generic)
- Estrategias de integracao entre contextos (ACL, Shared Kernel, Open Host Service)
- Distilacao de dominio e separacao de responsabilidades

**Quando citar:** sempre que um capitulo mencionar dominio, contrato de API, separacao de responsabilidades, linguagem do negocio, bounded context ou integracao entre servicos. Isso inclui, no minimo, os capitulos 1, 4, 5, 6, 9 e 17.

**Formato obrigatorio da citacao:**

> "Texto citado ou parafraseado." — KHONONOV, Vlad. **Aprenda Domain-Driven Design**, 2022.

---

### Livro 2 — Design de Sistemas Inteligentes

**HUYEN, Chip. Projetando Sistemas de Machine Learning: Uma Abordagem Iterativa para Aplicacoes Prontas para Producao. Sebastopol: O'Reilly Media, 2022.**

Este livro e a referencia principal para tudo que envolve:

- Design iterativo de sistemas e evolucao incremental de contratos
- Separacao entre sistemas de producao e sistemas de treinamento/inferencia
- Pipelines de dados e consistencia entre servicos
- Monitoramento, observabilidade e deteccao de degradacao em producao
- Trade-offs de latencia, throughput e confiabilidade
- Decisoes de arquitetura orientadas por dados reais (data-driven design)

**Quando citar:** sempre que um capitulo abordar evolucao de contrato, observabilidade, resiliencia, performance, monitoramento de servicos em producao, ou decisoes arquiteturais baseadas em restricoes de sistema. Isso inclui, no minimo, os capitulos 6, 8, 12, 13, 15 e 18.

**Formato obrigatorio da citacao:**

> "Texto citado ou parafraseado." — HUYEN, Chip. **Projetando Sistemas de Machine Learning**, 2022.

---

## Regras de Uso

### Citacao obrigatoria por capitulo

Todo capitulo deve conter **pelo menos uma citacao** de um dos dois livros base. Capitulos que trabalham com dominio, contrato ou integracao devem citar Khononov. Capitulos que trabalham com producao, observabilidade ou performance devem citar Huyen. Capitulos que cobrem ambos os temas devem citar os dois.

### Correspondencia entre conceitos do livro e os livros base

| Conceito abordado no capitulo          | Livro base a citar        |
| -------------------------------------- | ------------------------- |
| Bounded Context e linguagem de dominio | Khononov — DDD            |
| Versionamento e evolucao de contrato   | Khononov — DDD            |
| Agregados e eventos de dominio         | Khononov — DDD            |
| Integracao entre servicos (ACL, OHS)   | Khononov — DDD            |
| Trade-offs de latencia e throughput    | Huyen — Sistemas de ML    |
| Monitoramento e observabilidade        | Huyen — Sistemas de ML    |
| Evolucao iterativa de sistemas         | Huyen — Sistemas de ML    |
| Deteccao de degradacao em producao     | Huyen — Sistemas de ML    |
| Decisoes arquiteturais com dados reais | Huyen — Sistemas de ML    |
| Separacao dominio / infraestrutura     | Khononov — DDD + Cockburn |

### Proibicoes

- Nao usar os livros como enfeite. Cada citacao deve embasar uma decisao tecnica ou explicar um conceito diretamente relacionado ao trecho do capitulo.
- Nao parafrasear de forma que distorca o argumento original do autor.
- Nao citar capitulos ou paginas inexistentes nas obras. Quando houver duvida sobre a localizacao exata, cite apenas o livro sem numero de pagina.

---

## Modelo DDD aplicado ao OrderFlow

O sistema ficticio **OrderFlow**, usado ao longo do livro, deve ser modelado seguindo os principios de DDD de Khononov. A estrutura de subdominios do OrderFlow e:

### Subdominios

| Subdominio  | Tipo       | Responsabilidade                                                    |
| ----------- | ---------- | ------------------------------------------------------------------- |
| Pedidos     | Core       | Criacao, validacao e ciclo de vida de pedidos                       |
| Estoque     | Core       | Controle de disponibilidade e reserva de produtos                   |
| Pagamento   | Core       | Processamento e confirmacao de transacoes financeiras               |
| Notificacao | Supporting | Envio de emails, SMS e eventos de rastreamento                      |
| Logistica   | Supporting | Calculo de frete e rastreamento de entregas                         |
| Catalogo    | Generic    | Gestao de produtos (pode ser substituido por solucao de prateleira) |

### Bounded Contexts e Linguagem Onipresente

Cada subdominio tem sua propria linguagem. O mesmo termo pode ter significados diferentes entre contextos:

- **Produto** no Catalogo = entidade com nome, descricao e preco de lista.
- **Produto** no Estoque = entidade com SKU, quantidade disponivel e localizacao fisica.
- **Produto** no Pedido = linha de item com preco no momento da compra (snapshot imutavel).

Essa distincao deve ser respeitada nos contratos `.proto`. Cada servico define suas proprias mensagens — nunca compartilhe mensagens Protobuf entre bounded contexts.

### Integracao entre Bounded Contexts com gRPC

A estrategia de integracao entre os contextos do OrderFlow segue o padrao **Open Host Service (OHS)** de Khononov: cada servico expoe uma API publica bem definida (o contrato `.proto`) e usa uma **Anti-Corruption Layer (ACL)** para traduzir os dados do protocolo de transporte para os objetos de dominio internos.

```
Servico de Pedidos (cliente gRPC)
        ↓ chamada via stub gerado
Contrato .proto (Open Host Service)
        ↓ traducao na ACL
Adaptador de entrada (gRPC Controller)
        ↓ mapeamento para objeto de dominio
Caso de uso (dominio isolado)
```

> "Um Open Host Service define um protocolo que da acesso ao seu subsistema como um conjunto de servicos. Abra o protocolo para que qualquer um que precise integrar com voce possa usar." — KHONONOV, Vlad. **Aprenda Domain-Driven Design**, 2022.

---

## Checklist DDD por Capitulo

Antes de considerar um capitulo concluido, verifique:

- [ ] O capitulo usa a linguagem do dominio correto (Pedidos, Estoque, etc.) sem misturar termos de contextos diferentes?
- [ ] Os objetos Protobuf sao especificos do bounded context, sem reuso entre servicos?
- [ ] Ha pelo menos uma citacao de Khononov ou Huyen?
- [ ] A integracao entre servicos usa o padrao ACL/OHS descrito acima?
- [ ] O dominio (caso de uso) esta isolado do adaptador gRPC?

---

## Referencias Completas para o Backmatter

Inclua sempre no backmatter de cada capitulo relevante:

- KHONONOV, Vlad. **Aprenda Domain-Driven Design: Alinhando Arquitetura de Software e Estrategia de Negocio**. Sebastopol: O'Reilly Media, 2022.
- HUYEN, Chip. **Projetando Sistemas de Machine Learning: Uma Abordagem Iterativa para Aplicacoes Prontas para Producao**. Sebastopol: O'Reilly Media, 2022.
