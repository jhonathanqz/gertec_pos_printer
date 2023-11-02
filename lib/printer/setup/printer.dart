import '../domain/enum/barcode_type.dart';
import '../domain/enum/font_type.dart';

class Printer {
  final String message;
  final String alignment;
  final int? fontSize;
  final FontType? fontType;
  final bool bold;
  final bool underline;
  final bool italic;
  final BarcodeType? barcodeType;
  final int? height;
  final int? width;

  const Printer({
    this.message = '',
    this.alignment = '',
    this.fontSize,
    this.fontType,
    this.bold = false,
    this.underline = false,
    this.italic = false,
    this.barcodeType,
    this.height,
    this.width,
  });

  Printer copyWith({
    String? message,
    String? alignment,
    int? fontSize,
    FontType? fontType,
    bool? bold,
    bool? underline,
    bool? italic,
    BarcodeType? barcodeType,
    int? height,
    int? width,
  }) =>
      Printer(
        message: message ?? this.message,
        alignment: alignment ?? this.alignment,
        fontSize: fontSize ?? this.fontSize,
        fontType: fontType ?? this.fontType,
        bold: bold ?? this.bold,
        underline: underline ?? this.underline,
        italic: italic ?? this.italic,
        barcodeType: barcodeType ?? this.barcodeType,
        height: height ?? this.height,
        width: width ?? this.width,
      );
}
