import '../../../setup/barcode_print.dart';
import '../../../setup/text_print.dart';

abstract class IGertecPrinterRepository {
  Future<bool> cut();
  Future<bool> printLine(TextPrint textPrint);
  Future<bool> barcodePrint(BarcodePrint barcodePrint);
  Future<bool> printTextList(List<TextPrint> textPrintList);
  Future<bool> wrapLine(int lineQuantity);
  Future<String> checkStatusPrinter();
}
