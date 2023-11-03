import 'package:gertec_pos_printer/printer/domain/enum/gertec_type.dart';
import 'package:gertec_pos_printer/printer/domain/models/printer_response.dart';
import 'package:gertec_pos_printer/printer/setup/barcode_print.dart';
import 'package:gertec_pos_printer/printer/setup/text_print.dart';

import 'domain/repositories/contract/i_gertec_printer_repository.dart';
import 'domain/repositories/gertec_printer_repository.dart';
import 'domain/repositories/gertec_sk210_repository.dart';

class GertecPosPrinterController extends IGertecPrinterRepository {
  final GertecType _gertecType;

  final GertecPrinterRepository _gertecPrinterRepository =
      GertecPrinterRepository();
  final GertecSK210Repository _gertecSK210Repository = GertecSK210Repository();

  GertecPosPrinterController({required GertecType gertecType})
      : _gertecType = gertecType;

  @override
  Future<PrinterResponse> barcodePrint(BarcodePrint barcodePrint) async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.barcodePrint(barcodePrint);
      }
      return await _gertecSK210Repository.barcodePrint(barcodePrint);
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<PrinterResponse> checkStatusPrinter() async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.checkStatusPrinter();
      }
      return await _gertecSK210Repository.checkStatusPrinter();
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<PrinterResponse> cut() async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.cut();
      }
      return await _gertecSK210Repository.cut();
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<PrinterResponse> printLine(TextPrint textPrint) async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.printLine(textPrint);
      }
      return await _gertecSK210Repository.printLine(textPrint);
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<PrinterResponse> printTextList(List<TextPrint> textPrintList) async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.printTextList(textPrintList);
      }
      return await _gertecSK210Repository.printTextList(textPrintList);
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<PrinterResponse> wrapLine(int lineQuantity) async {
    try {
      if (_gertecType == GertecType.gpos700) {
        return await _gertecPrinterRepository.wrapLine(lineQuantity);
      }
      return await _gertecSK210Repository.wrapLine(lineQuantity);
    } catch (e) {
      rethrow;
    }
  }
}
