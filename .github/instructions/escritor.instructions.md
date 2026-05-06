---
title: Escritor Editorial
type: writing-instruction
version: 1.0
mandatory: true
applyTo: docs/ebook/manuscript/capitulos/**
---

# INSTRUCTIONS - ESCRITOR EDITORIAL

## Missao

Transformar conteudo tecnico em narrativa envolvente, acessivel e memoravel. Cada capitulo deve parecer uma conversa com um engenheiro senior experiente — preciso, humano e direto ao ponto.

---

## Regra Obrigatoria de Volume

> **Esta regra nao pode ser ignorada.**

- Cada capitulo deve ter **no minimo 10 paginas** de conteudo editorial, sem limite maximo de paginas.
- O conteudo deve ser tao extenso quanto necessario para cobrir o topico com profundidade, exemplos e contexto adequados.
- Uma pagina equivale a aproximadamente **400 palavras** de texto corrido (excluindo blocos de codigo e tabelas).
- O volume de cada subtitulo deve ser proporcional a complexidade do tema abordado.
  - Topicos mais complexos merecem mais paginas. Nao truncar explicacoes para caber em limite rigido.
- O livro tem **19 capitulos** com pelo menos **190 paginas** de conteudo principal — podendo superar esse total.
- Exercicios e referencias ficam fora desse limite (sao contabilizados separadamente no backmatter).

Antes de considerar um capitulo concluido, faca a contagem: o texto tem pelo menos 4.000 palavras de conteudo corrido? Se o topico exige mais, escreva mais.

---

## Tom e Voz

- **Tom**: tecnico e humano ao mesmo tempo. Nao academico, nao coloquial — o equilibrio do engenheiro que escreve para outros engenheiros.
- **Voz**: direta, sem rodeios. Frases curtas para conceitos novos. Paragrafos medios para explicacoes contextuais.
- **Perspectiva**: o leitor esta aprendendo algo novo que ainda esta sendo consolidado no mercado. Trate-o como inteligente, nao como iniciante absoluto.
- **Proibido**: jargao sem explicacao, termos em ingles sem contexto, "como ja vimos" sem revisitar o conceito.

---

## Estrutura Narrativa Obrigatoria por Capitulo

Todo capitulo deve seguir esta sequencia:

### 1. Abertura com Cenario Real (1/2 pagina)

Abra com uma situacao concreta que o leitor ja viveu ou pode imaginar facilmente. Nao comece com definicao — comece com um problema.

Exemplo de abertura ruim:

> "gRPC e um framework de RPC de alta performance."

Exemplo de abertura boa:

> "Eram 23h de uma sexta-feira quando o time de plataforma recebeu o alerta: a latencia entre o servico de pedidos e o de estoque havia triplicado. O culpado? Uma requisicao REST malformatada que nao foi validada no servidor. Naquele momento, um contrato tipado teria evitado tudo."

### 2. Conceito Central (2 paginas)

Explique o conceito principal do subtitulo de forma progressiva:

- Primeiro, o problema que motivou o conceito.
- Depois, a solucao proposta.
- Por fim, por que essa solucao e melhor que as alternativas.

### 3. Exemplo Pratico Anotado (2 paginas)

Todo conceito central deve ter um exemplo de codigo funcional e **comentado linha a linha** quando relevante. O exemplo deve:

- Ser compilavel e executavel com as instrucoes do capitulo.
- Usar o mesmo dominio de negocio ao longo do livro (ex.: sistema de pedidos/estoque).
- Mostrar o erro mais comum logo apos o exemplo correto.

### 4. Citacao ou Referencia a Autor Reconhecido (1 paragrafo por subtitulo)

Ancore o conceito em autoridade reconhecida. Use autores como:

- Martin Fowler (padroes de arquitetura, refactoring)
- Robert C. Martin — Uncle Bob (SOLID, Clean Architecture)
- Vaughn Vernon (Domain-Driven Design)
- Sam Newman (Building Microservices)
- Gregor Hohpe (Enterprise Integration Patterns)
- Eric Evans (DDD)
- Alistair Cockburn (Arquitetura Hexagonal)

Formato obrigatorio da citacao:

> "Texto original ou parafraseado com atribuicao clara." — SOBRENOME, Nome. **Titulo da Obra**, Ano.

### 5. Historia Continuada (fio condutor do livro)

O livro deve ter um **fio narrativo unico**: a evolucao de um sistema ficticio chamado **OrderFlow** — uma plataforma de gestao de pedidos que começa simples e vai se tornando um ecossistema distribuido com gRPC.

- Cap 1-3: equipe descobre gRPC ao enfrentar problemas de escala com REST.
- Cap 4-5: arquiteta o primeiro servico com arquitetura hexagonal.
- Cap 6-9: matura contratos, streaming, interceptadores e SOLID.
- Cap 10-13: coloca em producao com seguranca, observabilidade e resiliencia.
- Cap 14-17: testa, faz deploy, migra e consolida o sistema.
- Cap 18-19: documenta boas praticas e prepara o time.

Cada capitulo deve fazer uma referencia explicita ao que aconteceu com o OrderFlow no capitulo anterior e o que o time vai resolver no atual.

### 6. Diagrama ou Ilustracao Descrita (1 por capitulo)

Se nao ha imagem disponivel, descreva o diagrama em texto estruturado com blocos ASCII ou Markdown para que o leitor consiga visualizar o fluxo mentalmente.

### 7. Resumo e Proximo Passo (1/2 pagina)

Feche o capitulo com:

- 3 a 5 pontos de resumo em bullets.
- 1 paragrafo conectando o proximo capitulo ao atual.
- **Erros comuns**: lista de 2 a 3 armadilhas tipicas.

---

## Regras de Escrita

### Legibilidade

- Paragrafos com no maximo 5 linhas.
- Frases com no maximo 25 palavras quando estiver introduzindo conceito novo.
- Use listas para enumeracoes de 3 ou mais itens.
- Use tabelas para comparacoes entre tecnologias ou abordagens.

### Coesao

- Cada subtitulo deve ter uma unica ideia central — sem desvios.
- A transicao entre subtitulos deve ser explicita: uma frase de conexao no final do bloco anterior.
- Palavras-chave tecnicas devem ser destacadas em **negrito** na primeira ocorrencia do capitulo.

### Explicacao Progressiva

- Nunca assuma que o leitor lembrou do capitulo anterior. Relembre brevemente o contexto.
- Introduza abreviacoes com o nome completo na primeira ocorrencia: "Protocol Buffers (Protobuf)".
- Analogias do mundo real sao bem-vindas, desde que nao trivializem o conceito.

### Exemplos

- Use sempre o mesmo pacote Java no livro: `br.com.orderflow`.
- Nomes de metodos e variaveis em ingles (padrao Java).
- Comentarios no codigo em portugues.
- Nao mostre codigo incompleto sem avisar: use `// ... (continua no proximo bloco)`.

---

## Checklist Obrigatorio por Capitulo

Antes de considerar o capitulo concluido, verifique cada item:

- [ ] O capitulo tem pelo menos 4.000 palavras de texto corrido (sem limite maximo — escreva o quanto o tema exigir)?
- [ ] Ha uma abertura com cenario real nos primeiros 2 paragrafos?
- [ ] O fio narrativo do OrderFlow foi mencionado?
- [ ] Ha pelo menos uma citacao de autor reconhecido?
- [ ] Todos os exemplos de codigo usam o pacote `br.com.orderflow`?
- [ ] Ha um diagrama ou descricao visual do conceito principal?
- [ ] Ha uma secao de Erros Comuns?
- [ ] Ha um Resumo com bullets e conexao ao proximo capitulo?
- [ ] O volume esta distribuido proporcionalmente entre os subtitulos?

---

## Criterio de Qualidade

Se um engenheiro junior conseguir ler o capitulo, entender o conceito, implementar o exemplo e explicar para um colega sem consultar fontes externas, o capitulo passou.
