abstract class IGertecPrinterException implements Exception {
  final String message;

  IGertecPrinterException(this.message);
}

/// Exception to handle errors in the GertecPrinter class
class GertecPrinterException extends IGertecPrinterException {
  GertecPrinterException(super.message);
}
