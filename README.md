<div align="center" id="top"> 
  <img src="https://github.com/jhonathanqz/gertec_pos_printer/blob/main/assets/logo.png" title="Gertec_pos_printer" alt="Gertec_printer" height=250 width=400/>

&#xa0;

</div>

<h1 align="center">Gertec_pos_printer</h1>

<p align="center">
  <a href="#dart-sobre">Sobre</a> &#xa0; | &#xa0; 
  <a href="#sparkles-funcionalidades">Funcionalidades</a> &#xa0; | &#xa0;
  <a href="#rocket-tecnologias">Tecnologias</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-pré-requisitos">Pré requisitos</a> &#xa0; | &#xa0;
  <a href="#checkered_flag-ajuda">Ajuda</a> &#xa0; | &#xa0;
  <a href="https://github.com/jhonathanqz" target="_blank">Autor</a>
</p>

<br>

<a href="https://buymeacoffee.com/jhonathanqr" target="_blank">
  <img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Book" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;">
</a>

[![Github Badge](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white&link=https://github.com/jhonathanqz)](https://github.com/jhonathanqz)

# **_ Package para trabalhar somente com Android _**

## :dart: Sobre

O package `gertec_pos_printer` não é Oficial da GERTEC. Essa é uma integração com a impressora dos modelos GERTEC GPOS700 e GERTEC SK-210.

### Package somente funciona com Android level 21 ou posterior.

### Package está implementado até o momento somente para os modelos GPOS700 e SK-210.

## :sparkles: Funcionalidades

Para utilizar o plugin é necessário criar uma instância da classe `GertecPOSPrinter` passando como parâmetro o modelo de equipamento utilizado `GertecType`.

Funções implementadas:

```dart
final GertecPOSPrinter gertecPosPrinter = GertecPOSPrinter(gertecType: GertecType.gpos700);
```

:heavy_check_mark: gertecPosPrinter.instance.cut() -> Corta o papel\

:heavy_check_mark: gertecPosPrinter.instance.printLine("message") -> Imprime em uma linha o parâmetro passado;\

:heavy_check_mark: gertecPosPrinter.instance.printTextList(['message1', 'message2']) -> Imprime uma lista de textos;\

:heavy_check_mark: gertecPosPrinter.instance.barcodePrint('barcode') -> Imprime um código de barras conforme configurações enviadas por parâmetro;\

:heavy_check_mark: gertecPosPrinter.instance.wrapLine(1) -> Avança a quantidade de linhas informada por parâmetro;\

:heavy_check_mark: gertecPosPrinter.instance.checkStatusPrinter() -> Devolve uma `String` com o status atual da impressora;\

## :rocket: Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

- [Flutter](https://flutter.dev/)
- [Lib Gertec](https://developer.gertec.com.br/)

## :white_check_mark: Pré requisitos

### **\* Antes de começar: \*\***

O package somente funciona com Android Level Api 21 ou posterior. Essa é uma regra implementada pela própria Gertec.

## :checkered_flag: Ajuda

Caso precise de ajuda com o plugin, segue em anexo servidor do discord.

- [Ajuda](https://discord.gg/dH22WbgK)

</br>

Feito por <a href="https://github.com/jhonathanqz" target="_blank">Jhonathan Queiroz</a>

&#xa0;

<a href="#top">Voltar para o topo</a>
