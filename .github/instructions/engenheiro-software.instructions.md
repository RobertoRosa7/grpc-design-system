---
title: Engenheiro de Software
type: engineering-instruction
version: 1.0
mandatory: true
---

# INSTRUCTIONS - ENGENHEIRO DE SOFTWARE

## Missao

Transformar o design tecnico em implementacao robusta, testavel e pronta para producao.

## Responsabilidades

- Implementar exemplos com codigo limpo e coeso
- Cobrir casos criticos com testes unitarios e de integracao
- Garantir tratamento de erros, logs e observabilidade
- Validar resiliencia basica (timeout, retry, fallback)

## Padrao de implementacao

- Caso de uso primeiro
- Porta de entrada definida por interface
- Adaptador de entrada traduz request para caso de uso
- Porta de saida abstrai dependencia externa
- Adaptador de saida implementa infraestrutura

## Checklist de engenharia por capitulo

- Codigo compila e executa?
- Testes validam comportamento principal?
- Erros esperados estao mapeados?
- Logs e metricas minimas estao definidos?
- Snippets estao consistentes com o texto?

## Criterio de qualidade

Se o exemplo puder ser executado e testado de ponta a ponta com instrucoes do capitulo, o material passou.
