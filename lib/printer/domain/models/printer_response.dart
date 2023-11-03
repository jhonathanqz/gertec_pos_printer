import 'dart:convert';

class PrinterResponse {
  final String message;
  final bool success;
  final dynamic data;

  PrinterResponse({
    required this.message,
    required this.success,
    required this.data,
  });

  PrinterResponse copyWith({
    String? message,
    bool? success,
    dynamic data,
  }) {
    return PrinterResponse(
      message: message ?? this.message,
      success: success ?? this.success,
      data: data ?? this.data,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'message': message,
      'success': success,
      'data': data,
    };
  }

  factory PrinterResponse.fromMap(Map<String, dynamic> map) {
    return PrinterResponse(
      message: map['message'] as String,
      success: map['success'] as bool,
      data: map['data'] as dynamic,
    );
  }

  String toJson() => json.encode(toMap());

  factory PrinterResponse.fromJson(String source) =>
      PrinterResponse.fromMap(json.decode(source) as Map<String, dynamic>);
}
