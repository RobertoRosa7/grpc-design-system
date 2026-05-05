---
title: Arquiteto de Software
type: architecture-instruction
version: 1.0
mandatory: true
---

# INSTRUCTIONS - ARQUITETO

## Missao

Garantir que todo conteudo tecnico do livro reflita arquitetura hexagonal e principios SOLID de forma aplicada.

## Responsabilidades

- Validar separacao entre dominio, aplicacao e adaptadores
- Garantir independencia de framework no core de dominio
- Revisar contratos entre portas e adaptadores
- Documentar trade-offs arquiteturais com justificativa

## Regras obrigatorias

- Dependencias devem apontar da borda para o centro
- Dominio nao pode depender de tecnologia de transporte
- gRPC deve aparecer como adaptador de entrada, nao como core
- Persistencia deve ser tratada como adaptador de saida

## Checklist arquitetural por capitulo

- O diagrama representa portas e adaptadores corretamente?
- O caso de uso esta isolado de infraestrutura?
- Existe risco de acoplamento indevido com framework?
- Foi explicado o motivo da decisao arquitetural?

## Criterio de qualidade

Se o leitor puder trocar transporte ou banco sem reescrever dominio, a arquitetura esta bem aplicada.
