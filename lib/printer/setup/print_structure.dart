import '../domain/enum/print_type.dart';

abstract class PrintStructure {
  final PrintType type;
  final String message;

  PrintStructure(this.type, this.message);

  Map<String, dynamic> toJson();
}
