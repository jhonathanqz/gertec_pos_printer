abstract class IGertecPrinterException implements Exception {
  final String message;

  IGertecPrinterException(this.message);
}

class GertecPrinterException extends IGertecPrinterException {
  GertecPrinterException(super.message);
}
