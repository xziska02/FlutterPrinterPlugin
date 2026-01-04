sealed class PrinterStatus {
  String get name;

  static PrinterStatus fromString(String statusString) {
    return switch (statusString.toLowerCase()) {
      'connected' => Connected(),
      'disconnected' => Disconnected(),
      'outOfPaper' => OutOfPaper(),
      'error' => Error(),
      _ => Error(),
    };
  }
}

class Connected extends PrinterStatus {
  @override
  String get name => 'Connected';
}

class Disconnected extends PrinterStatus {
  @override
  String get name => 'Disconnected';
}

class OutOfPaper extends PrinterStatus {
  @override
  String get name => 'Out of Paper';
}

class Error extends PrinterStatus {
  @override
  String get name => 'Error';
}

class Initializing extends PrinterStatus {
  @override
  String get name => 'Initializing';
}

// Custom exception for printer-related errors
class PrinterException implements Exception {
  final String message;
  final String? code;

  PrinterException(this.message, {this.code});

  @override
  String toString() => 'PrinterException: $message';
}
