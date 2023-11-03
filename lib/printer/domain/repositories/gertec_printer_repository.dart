import 'package:flutter/services.dart';

import 'package:gertec_pos_printer/printer/domain/exception/gertec_printer_exception.dart';
import 'package:gertec_pos_printer/printer/setup/barcode_print.dart';
import 'package:gertec_pos_printer/printer/setup/text_print.dart';
import 'package:gertec_pos_printer/printer/style/gertec_printer_style.dart';

import '../../setup/constants.dart';
import '../models/printer_response.dart';

import 'contract/i_gertec_printer_repository.dart';

class GertecPrinterRepository implements IGertecPrinterRepository {
  static const MethodChannel _channel = MethodChannel(channelName);

  //Function to print barcode
  @override
  Future<PrinterResponse> barcodePrint(BarcodePrint barcodePrint) async {
    try {
      final response =
          await _printer(GertecPrinterStyle.lineToMethodChannel(barcodePrint));
      cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to check status printer
  @override
  Future<PrinterResponse> checkStatusPrinter() async {
    try {
      final status = await _channel.invokeMethod('callStatusGertec');
      return PrinterResponse.fromJson(status);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to cut paper
  @override
  Future<PrinterResponse> cut() async {
    try {
      final response = await _channel.invokeMethod('callCutGertec');
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to print line
  @override
  Future<PrinterResponse> printLine(TextPrint textPrint) async {
    try {
      final response =
          await _printer(GertecPrinterStyle.lineToMethodChannel(textPrint));
      cut();
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to print list of text
  @override
  Future<PrinterResponse> printTextList(List<TextPrint> textPrintList) async {
    try {
      for (var line in textPrintList) {
        _printer(GertecPrinterStyle.lineToMethodChannel(line));
      }
      final response = await cut();
      return response;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to wrap line
  @override
  Future<PrinterResponse> wrapLine(int lineQuantity) async {
    try {
      final response = await _channel.invokeMethod(
        'callNextLine',
        {
          'lineQuantity': lineQuantity,
        },
      );
      return PrinterResponse.fromJson(response);
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to call method channel
  static Future<dynamic> _printer(Map<String, dynamic> content) async {
    return await _channel.invokeMethod(
      'callPrintGertec',
      content,
    );
  }
}
