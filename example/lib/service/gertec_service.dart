import 'package:gertec_pos_printer/gertec_pos_printer.dart';
import 'package:gertec_pos_printer/printer/domain/enum/barcode_type.dart';
import 'package:gertec_pos_printer/printer/setup/barcode_print.dart';
import 'package:gertec_pos_printer/printer/setup/text_print.dart';

class GertecService {
  final GertecPOSPrinter _gertecPrinter;

  GertecService({required GertecPOSPrinter gertecPrinter})
      : _gertecPrinter = gertecPrinter;

  Future<bool> cut() async {
    try {
      return _gertecPrinter.instance.cut();
    } catch (e) {
      print(e);
      return false;
    }
  }

  Future<bool> printLine(String params) async {
    try {
      final message = TextPrint(message: params);
      return _gertecPrinter.instance.printLine(message);
    } catch (e) {
      print(e);
      return false;
    }
  }

  Future<bool> printTextList(List<String> params) async {
    try {
      final message = params.map((e) => TextPrint(message: e)).toList();
      return _gertecPrinter.instance.printTextList(message);
    } catch (e) {
      print(e);
      return false;
    }
  }

  Future<bool> barcodePrint({
    required String text,
    required int height,
    required int width,
    required BarcodeType type,
  }) async {
    try {
      final message = BarcodePrint(
          message: text, height: height, width: width, barcodeType: type);
      return _gertecPrinter.instance.barcodePrint(message);
    } catch (e) {
      print(e);
      return false;
    }
  }

  Future<bool> wrapLine(int lines) async {
    try {
      return _gertecPrinter.instance.wrapLine(lines);
    } catch (e) {
      print(e);
      return false;
    }
  }

  Future<String> checkStatusPrinter() async {
    try {
      return _gertecPrinter.instance.checkStatusPrinter();
    } catch (e) {
      print(e);
      return 'false';
    }
  }
}
