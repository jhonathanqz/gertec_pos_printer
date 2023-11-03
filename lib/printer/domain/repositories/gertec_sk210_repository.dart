import 'package:flutter/services.dart';

import 'package:gertec_pos_printer/printer/domain/models/printer_response.dart';
import 'package:gertec_pos_printer/printer/domain/repositories/contract/i_gertec_printer_repository.dart';
import 'package:gertec_pos_printer/printer/setup/barcode_print.dart';
import 'package:gertec_pos_printer/printer/setup/text_print.dart';

import '../../setup/constants.dart';
import '../../style/gertec_printer_style.dart';
import '../exception/gertec_printer_exception.dart';

class GertecSK210Repository implements IGertecPrinterRepository {
  static const MethodChannel _channel = MethodChannel(channelName);

  @override
  Future<PrinterResponse> barcodePrint(BarcodePrint barcodePrint) async {
    try {
      final response = await _channel.invokeMethod('callPrinterBarcode210',
          GertecPrinterStyle.lineToMethodChannel(barcodePrint));
      cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  @override
  Future<PrinterResponse> checkStatusPrinter() async {
    try {
      final response = await _channel.invokeMethod('callPrinterStatus210', {});
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  @override
  Future<PrinterResponse> cut() async {
    try {
      final response =
          await _channel.invokeMethod('callCut210', <String, dynamic>{
        'mode': 0,
      });
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  @override
  Future<PrinterResponse> printLine(TextPrint textPrint) async {
    try {
      final response = await _printer(textPrint);
      await cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  @override
  Future<PrinterResponse> printTextList(List<TextPrint> textPrintList) async {
    try {
      dynamic response = {};
      for (var line in textPrintList) {
        response = await _printer(line);
      }
      cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  @override
  Future<PrinterResponse> wrapLine(int lineQuantity) async {
    try {
      final response =
          await _channel.invokeMethod('callPrinterWrap210', <String, dynamic>{
        'linesWrap': lineQuantity,
      });
      cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  Future<dynamic> _printer(TextPrint textPrint) async {
    return await _channel.invokeMethod(
        'callPrint210', GertecPrinterStyle.lineToMethodChannel(textPrint));
  }
}
