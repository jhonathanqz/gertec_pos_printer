import 'package:gertec_pos_printer/printer/domain/enum/gertec_type.dart';
import 'package:gertec_pos_printer/printer/gertec_pos_printer_controller.dart';

class GertecPOSPrinter {
  final GertecType _gertecType;

  GertecPOSPrinter({required GertecType gertecType}) : _gertecType = gertecType;
  //Function to get instance
  GertecPosPrinterController get instance =>
      GertecPosPrinterController(gertecType: _gertecType);
}
