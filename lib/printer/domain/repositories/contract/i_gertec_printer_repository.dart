import '../../../setup/barcode_print.dart';
import '../../../setup/text_print.dart';
import '../../models/printer_response.dart';

abstract class IGertecPrinterRepository {
  Future<PrinterResponse> cut();
  Future<PrinterResponse> printLine(TextPrint textPrint);
  Future<PrinterResponse> barcodePrint(BarcodePrint barcodePrint);
  Future<PrinterResponse> printTextList(List<TextPrint> textPrintList);
  Future<PrinterResponse> wrapLine(int lineQuantity);
  Future<PrinterResponse> checkStatusPrinter();
}
