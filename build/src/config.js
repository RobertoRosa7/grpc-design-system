"use strict";

const path = require("path");

const ROOT = path.resolve(__dirname, "../..");

const config = {
  // Metadados do livro
  book: {
    title:
      "gRPC com Spring Boot em Microsserviços: Guia Completo do Zero à Produção",
    subtitle:
      "Arquitetura Hexagonal, SOLID, Streaming, Segurança, Observabilidade e Deploy para Sistemas Distribuídos Modernos",
    author: "Roberto Rosa da Silva",
    publisher: "Publicação Independente",
    language: "pt-br",
    year: "2026",
    cover: path.join(ROOT, "docs/ebook/assets/images/capa-template.svg"),
  },

  // Caminhos de entrada
  paths: {
    manuscript: path.join(ROOT, "docs/ebook/manuscript"),
    chapters: path.join(ROOT, "docs/ebook/manuscript/capitulos"),
    backmatter: path.join(ROOT, "docs/ebook/manuscript/backmatter"),
    frontmatter: path.join(ROOT, "docs/ebook/manuscript/frontmatter"),
    template: path.join(__dirname, "../template/page.html"),
    css: path.join(__dirname, "../styles/book.css"),
    images: path.join(ROOT, "docs/ebook/assets/images"),
  },

  // Caminhos de saÃ­da
  dist: {
    dir: path.join(ROOT, "dist"),
    epub: path.join(ROOT, "dist/book.epub"),
    pdf: path.join(ROOT, "dist/book.pdf"),
    html: path.join(ROOT, "dist/book.html"),
  },

  // Opcoes de PDF (puppeteer)
  pdf: {
    width: "7in",
    height: "10in",
    // Reserva maior no topo/rodapé para evitar sobreposição entre header/footer e conteúdo.
    // KDP 419 pag: gutter minimo 0.625in — usando 0.875in (recomendado KDP para 300-600 pags)
    margin: {
      top: "0.875in",
      right: "0.875in",
      bottom: "0.875in",
      left: "0.875in",
    },
    printBackground: true,
    displayHeaderFooter: true,
    headerTemplate:
      "<style>html,body{margin:0;padding:0}</style><div style=\"width:100%;padding:0.175in 0.875in 0.05in;font-family:'Georgia','Times New Roman',serif;font-size:8px;color:#64748b;box-sizing:border-box;line-height:1.2;\">gRPC com Spring Boot em Microsserviços</div>",
    footerTemplate:
      '<style>html,body{margin:0;padding:0}</style><div style="width:100%;padding:0.05in 0.875in 0.175in;font-family:\'Georgia\',\'Times New Roman\',serif;font-size:8px;color:#6b7280;display:flex;justify-content:flex-end;align-items:center;box-sizing:border-box;line-height:1.2;"><span>p. <span class="pageNumber"></span> / <span class="totalPages"></span></span></div>',
  },
};

module.exports = config;
