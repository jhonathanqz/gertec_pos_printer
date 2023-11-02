import '../domain/enum/barcode_type.dart';
import '../domain/enum/font_type.dart';
import '../domain/enum/text_alignment.dart';
import '../setup/barcode_print.dart';
import '../setup/print_structure.dart';
import '../setup/text_print.dart';

abstract class GertecPrinterStyle {
  //Function return a String to fontType
  static String _getFontType(FontType type) {
    switch (type) {
      case FontType.def:
        return 'DEFAULT';
      case FontType.monospace:
        return 'MONOSPACE';
      case FontType.sansserif:
        return 'SANS_SERIF';
      case FontType.serif:
        return 'SERIF';
      default:
        return 'DEFAULT';
    }
  }

  //Function to return a String to alignment
  static String _getAlignment(TextAlignment alignment) {
    switch (alignment) {
      case TextAlignment.left:
        return 'LEFT';
      case TextAlignment.center:
        return 'CENTER';
      case TextAlignment.right:
        return 'RIGHT';
      default:
        return 'CENTER';
    }
  }

  //Function to return a String to barcodeType
  static String _getBarcodeType(BarcodeType type) {
    switch (type) {
      case BarcodeType.code128:
        return 'CODE_128';
      case BarcodeType.ean8:
        return 'EAN_8';
      case BarcodeType.ean13:
        return 'EAN_13';
      case BarcodeType.qrcode:
        return 'QR_CODE';
      case BarcodeType.pdf417:
        return 'PDF_417';
      default:
        return 'QR_CODE';
    }
  }

  //Function to convert a PrintStructure to Map<String, dynamic>
  static Map<String, dynamic> lineToMethodChannel(PrintStructure line) {
    if (line is TextPrint) {
      return {
        'type': 'text',
        'message': line.message,
        'alignment': _getAlignment(line.alignment),
        'fontSize': line.fontSize,
        'fontType': _getFontType(line.fontType),
        'bold': line.bold,
        'underline': line.underline,
        'italic': line.italic,
      };
    } else if (line is BarcodePrint) {
      return {
        'type': 'barcode',
        'message': line.message,
        'width': line.width,
        'height': line.height,
        'barcodeType': _getBarcodeType(line.barcodeType),
      };
    } else {
      throw Exception();
    }
  }
}
