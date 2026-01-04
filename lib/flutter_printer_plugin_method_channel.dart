import 'package:flutter/services.dart';
import 'package:flutter_printer_plugin/models/printer_models.dart';

import 'flutter_printer_plugin_platform_interface.dart';
import 'models/alignment_models.dart';

class MethodChannelFlutterPrinterPlugin extends FlutterPrinterPluginPlatform {
  final _methodChannel = const MethodChannel('flutter_printer_plugin');
  final _eventChannel = const EventChannel('flutter_printer_plugin_status');

  MethodChannelFlutterPrinterPlugin();

  @override
  Future<bool> initialize() async {
    try {
      final result = await _methodChannel.invokeMethod<bool>('initialize');
      return result ?? false;
    } on PlatformException catch (e) {
      print('FOUND EXCEPTION: ${e.message}');
      throw PrinterException(
        'Failed to initialize printer: ${e.message}',
        code: e.code,
      );
    } catch (e) {
      throw PrinterException('Failed to initialize printer: $e');
    }
  }

  @override
  Future<void> printText(
    String text, {
    TextAlignment? align,
    bool bold = false,
  }) async {
    if (text.isEmpty) {
      throw PrinterException(
        'Text cannot be empty',
        code: 'INVALID_PARAMETER',
      );
    }

    if (text.length > 10000) {
      throw PrinterException(
        'Text is too long. Maximum length is 10000 characters',
        code: 'TEXT_TOO_LONG',
      );
    }

    try {
      await _methodChannel.invokeMethod('printText', {
        'text': text,
        'alignment': align?.name ?? 'start',
        'isBold': bold,
      });
    } on PlatformException catch (e) {
      throw PrinterException(
        'Failed to print text: ${e.message}',
        code: e.code,
      );
    } catch (e) {
      throw PrinterException('Failed to print text: $e');
    }
  }

  @override
  Future<void> printQRCode(String data, {int size = 200}) async {
    if (data.isEmpty) {
      throw PrinterException(
        'QR code data cannot be empty',
        code: 'INVALID_PARAMETER',
      );
    }

    if (data.length > 3000) {
      throw PrinterException(
        'QR code data is too long. Maximum length is 3000 characters',
        code: 'DATA_TOO_LONG',
      );
    }

    if (size < 50 || size > 1000) {
      throw PrinterException(
        'QR code size must be between 50 and 1000. Provided: $size',
        code: 'INVALID_SIZE',
      );
    }

    try {
      await _methodChannel.invokeMethod('printQRCode', {
        'data': data,
        'size': size,
      });
    } on PlatformException catch (e) {
      throw PrinterException(
        'Failed to print QR code: ${e.message}',
        code: e.code,
      );
    } catch (e) {
      throw PrinterException('Failed to print QR code: $e');
    }
  }

  @override
  Stream<PrinterStatus> get statusStream {
    return _eventChannel.receiveBroadcastStream().map((dynamic status) {
      try {
        if (status is String) {
          return _parseStatus(status);
        } else {
          return Disconnected();
        }
      } catch (e) {
        return Disconnected();
      }
    }).handleError((error) {
      print('Error in status stream: $error');
      return Disconnected();
    });
  }

  PrinterStatus _parseStatus(String status) {
    switch (status.toUpperCase()) {
      case 'CONNECTED':
        return Connected();
      case 'DISCONNECTED':
        return Disconnected();
      case 'OUT_OF_PAPER':
        return OutOfPaper();
      case 'ERROR':
        return Error();
      default:
        return Disconnected();
    }
  }

  @override
  Future<PrinterStatus> get currentStatus async => Future.value(Disconnected());

  @override
  Future<void> cutPaper() async {
    try {
      await _methodChannel.invokeMethod<bool>('cutPaper');
    } on PlatformException catch (e) {
      throw PrinterException(
        'Failed to cut paper: ${e.message}',
        code: e.code,
      );
    } catch (e) {
      throw PrinterException('Failed to cut paper: $e');
    }
  }

  @override
  Future<void> disconnect() async {
    try {
      await _methodChannel.invokeMethod<bool>('disconnect');
    } on PlatformException catch (e) {
      throw PrinterException(
        'Failed to disconnect: ${e.message}',
        code: e.code,
      );
    } catch (e) {
      throw PrinterException('Failed to disconnect: $e');
    }
  }
}
