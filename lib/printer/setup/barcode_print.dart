import '../domain/enum/barcode_type.dart';
import '../domain/enum/print_type.dart';

import 'print_structure.dart';

//Class to define properties from barcode print
class BarcodePrint extends PrintStructure {
  final int height;
  final int width;
  final BarcodeType barcodeType;

  BarcodePrint({
    required String message,
    required this.height,
    required this.width,
    required this.barcodeType,
  }) : super(PrintType.barcode, message);

  @override
  Map<String, dynamic> toJson() => {
        'message': message,
        'height': height,
        'width': width,
        'barcodeType': barcodeType.toString(),
      };
}
