import 'package:flutter_printer_plugin/models/alignment_models.dart';
import 'package:flutter_printer_plugin/models/printer_models.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class FlutterPrinterPluginPlatform extends PlatformInterface {
  FlutterPrinterPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  Future<bool> initialize() async {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> printText(
    String text, {
    TextAlignment? align,
    bool bold = false,
  }) async {
    throw UnimplementedError('printText() has not been implemented.');
  }

  Future<void> printQRCode(String data, {int size = 200}) async {
    throw UnimplementedError('printQRCode() has not been implemented.');
  }

  Stream<PrinterStatus> get statusStream {
    throw UnimplementedError('statusStream() has not been implemented.');
  }

  Future<PrinterStatus> get currentStatus async {
    throw UnimplementedError('currentStatus() has not been implemented.');
  }

  Future<void> cutPaper() async {
    throw UnimplementedError('cutPaper() has not been implemented.');
  }

  Future<void> disconnect() async {
    throw UnimplementedError('disconnect() has not been implemented.');
  }
}
