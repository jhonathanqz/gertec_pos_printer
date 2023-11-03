import 'package:flutter/material.dart';

import 'package:gertec_pos_printer/gertec_pos_printer.dart';
import 'package:gertec_pos_printer/printer/domain/enum/barcode_type.dart';
import 'package:gertec_pos_printer/printer/domain/enum/gertec_type.dart';

import 'service/gertec_service.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late GertecPOSPrinter _gertecPrinterPlugin;
  late GertecService _gertecService;

  @override
  void initState() {
    _gertecPrinterPlugin = GertecPOSPrinter(gertecType: GertecType.gpos700);
    _gertecService = GertecService(gertecPrinter: _gertecPrinterPlugin);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Gertec Printer Example'),
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () => _gertecService.printLine('Print one line'),
              child: const Text('Print Line'),
            ),
            ElevatedButton(
              onPressed: () => _gertecService
                  .printTextList(['print line one', 'print line two']),
              child: const Text('Print Text List'),
            ),
            ElevatedButton(
              onPressed: () => _gertecService.barcodePrint(
                text: '789654136872685',
                height: 50,
                width: 50,
                type: BarcodeType.code128,
              ),
              child: const Text('Barcode Print'),
            ),
            ElevatedButton(
              onPressed: () => _gertecService.wrapLine(1),
              child: const Text('Wrap Line'),
            ),
            ElevatedButton(
              onPressed: () => _gertecService.checkStatusPrinter(),
              child: const Text('Check Status Print'),
            ),
          ],
        ),
      ),
    );
  }
}
