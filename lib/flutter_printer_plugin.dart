import 'package:flutter_printer_plugin/models/alignment_models.dart';
import 'package:flutter_printer_plugin/models/printer_models.dart';

import 'flutter_printer_plugin_method_channel.dart';
import 'flutter_printer_plugin_platform_interface.dart';

class FlutterPrinterPlugin {
  final FlutterPrinterPluginPlatform _printerPlugin = MethodChannelFlutterPrinterPlugin();
  
  Future<bool> initialize() async {
    return _printerPlugin.initialize();
  }

  Future<void> printText(
    String text, {
    TextAlignment? align,
    bool bold = false,
  }) {
    return _printerPlugin.printText(
      text,
      align: align,
      bold: bold,
    );
  }

  Future<void> printQRCode(String data, {int size = 200}) async {
    return _printerPlugin.printQRCode(data, size: size);
  }

  Stream<PrinterStatus> get statusStream {
    return _printerPlugin.statusStream;
  }

  Future<PrinterStatus> get currentStatus async {
    return _printerPlugin.currentStatus;
  }

  Future<void> cutPaper() async {
    return _printerPlugin.cutPaper();
  }

  Future<void> disconnect() async {
    return _printerPlugin.disconnect();
  }
}
