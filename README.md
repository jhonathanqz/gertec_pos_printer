<div align="center" id="top"> 
  <img src="https://github.com/jhonathanqz/gertec_pos_printer/blob/main/assets/logo.png" alt="Gertec_printer" height=250 width=400/>

  &#xa0;

</div>

<h1 align="center">Gertec_pos_printer</h1>

<p align="center">
  <a href="#dart-sobre">Sobre</a> &#xa0; | &#xa0; 
  <a href="#sparkles-funcionalidades">Funcionalidades</a> &#xa0; | &#xa0;
  <a href="#rocket-tecnologias">Tecnologias</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-pré-requisitos">Pré requisitos</a> &#xa0; | &#xa0;
  <a href="#checkered_flag-ajuda">Ajuda</a> &#xa0; | &#xa0;
  <a href="#memo-licença">Licença</a> &#xa0; | &#xa0;
  <a href="https://github.com/jhonathanqz" target="_blank">Autor</a>
</p>

<br>

# *** Package para trabalhar somente com Android ***

## :dart: Sobre ##

O package `gertec_pos_printer` não é Oficial da GERTEC. Essa é uma integração com a impressora do equipamento GPOS700.

### Package somente funciona com Android level 21 ou posterior.

### Package está implementado até o momento somente para o modelo GPOS700.

## :sparkles: Funcionalidades ##

Funções implementadas:

:heavy_check_mark: GertecPrinter().instance.cut() -> Corta o papel\

:heavy_check_mark: GertecPrinter().instance.printLine("message") -> Imprime em uma linha o parâmetro passado;\

:heavy_check_mark: GertecPrinter().instance.printTextList(['message1', 'message2']) -> Imprime uma lista de textos;\

:heavy_check_mark: GertecPrinter().instance.barcodePrint('barcode') -> Imprime um código de barras conforme configurações enviadas por parâmetro;\

:heavy_check_mark: GertecPrinter().instance.wrapLine(1) -> Avança a quantidade de linhas informada por parâmetro;\

:heavy_check_mark: GertecPrinter().instance.checkStatusPrinter() -> Devolve uma `String` com o status atual da impressora;\

## :rocket: Tecnologias ##

As seguintes ferramentas foram usadas na construção do projeto:

- [Flutter](https://flutter.dev/)
- [Lib Gertec](https://developer.gertec.com.br/)


## :white_check_mark: Pré requisitos ##

### *** Antes de começar: ****

O package somente funciona com Android Level Api 21 ou posterior. Essa é uma regra implementada pela própria Gertec.


## :checkered_flag: Ajuda ##

Caso precise de ajuda com o plugin, segue em anexo servidor do discord.

- [Ajuda](https://discord.gg/dH22WbgK)

## :memo: Licença ##

Este projeto está sob licença MIT. Veja o arquivo [LICENSE](LICENSE.md) para mais detalhes.


Feito por <a href="https://github.com/jhonathanqz" target="_blank">Jhonathan Queiroz</a>

&#xa0;

<a href="#top">Voltar para o topo</a>
