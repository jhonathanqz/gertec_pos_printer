import '../domain/enum/font_type.dart';
import '../domain/enum/print_type.dart';
import '../domain/enum/text_alignment.dart';

import 'print_structure.dart';

//Class to define properties from text print
class TextPrint extends PrintStructure {
  final TextAlignment alignment;
  final int fontSize;
  final FontType fontType;
  final bool bold;
  final bool underline;
  final bool italic;

  TextPrint({
    required String message,
    this.alignment = TextAlignment.left,
    this.fontSize = 16,
    this.fontType = FontType.def,
    this.bold = false,
    this.underline = false,
    this.italic = false,
  }) : super(PrintType.text, message);

  @override
  Map<String, dynamic> toJson() => {
        'message': message,
        'alignment': alignment.toString(),
        'fontSize': fontSize,
        'fontType': fontType.toString(),
        'bold': bold,
        'underline': underline,
        'italic': italic,
      };
}
