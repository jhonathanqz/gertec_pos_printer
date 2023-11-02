import 'package:flutter/services.dart';

import 'package:gertec_pos_printer/printer/domain/exception/gertec_printer_exception.dart';
import 'package:gertec_pos_printer/printer/setup/barcode_print.dart';
import 'package:gertec_pos_printer/printer/setup/text_print.dart';
import 'package:gertec_pos_printer/printer/style/gertec_printer_style.dart';

import '../../setup/constants.dart';

import 'contract/i_gertec_printer_repository.dart';

class GertecPrinterRepository implements IGertecPrinterRepository {
  static const MethodChannel _channel = MethodChannel(channelName);

  //Function to print barcode
  @override
  Future<bool> barcodePrint(BarcodePrint barcodePrint) async {
    try {
      _printer(GertecPrinterStyle.lineToMethodChannel(barcodePrint));
      cut();
      return true;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to check status printer
  @override
  Future<String> checkStatusPrinter() async {
    try {
      final status = await _channel.invokeMethod('callStatusGertec');
      return status;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to cut paper
  @override
  Future<bool> cut() async {
    try {
      _channel.invokeMethod('callCutGertec');
      return true;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to print line
  @override
  Future<bool> printLine(TextPrint textPrint) async {
    try {
      _printer(GertecPrinterStyle.lineToMethodChannel(textPrint));
      cut();
      return true;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to print list of text
  @override
  Future<bool> printTextList(List<TextPrint> textPrintList) async {
    try {
      for (var line in textPrintList) {
        _printer(GertecPrinterStyle.lineToMethodChannel(line));
      }
      cut();
      return true;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to wrap line
  @override
  Future<bool> wrapLine(int lineQuantity) async {
    try {
      _channel.invokeMethod(
        'callNextLine',
        {
          'lineQuantity': lineQuantity,
        },
      );
      return true;
    } catch (e) {
      throw GertecPrinterException(e.toString());
    }
  }

  //Function to call method channel
  static void _printer(Map<String, dynamic> content) {
    _channel.invokeMethod(
      'callPrintGertec',
      content,
    );
  }
}
