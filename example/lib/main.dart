import 'package:flutter/material.dart';
import 'package:flutter_printer_plugin/flutter_printer_plugin.dart';
import 'package:flutter_printer_plugin/models/alignment_models.dart';
import 'package:flutter_printer_plugin/models/printer_models.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blueAccent),
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class MyHomeUiState {
  PrinterStatus printerStatus = Disconnected();
  bool isBold = false;
  bool isPrinting = false;
  String? error;
  TextAlignment textAlignment = TextAlignment.start;
}

class _MyHomePageState extends State<MyHomePage> {
  final _flutterPrinterPlugin = FlutterPrinterPlugin();

  final MyHomeUiState _homeUiState = MyHomeUiState();

  final TextEditingController _textController = TextEditingController();
  final TextEditingController _qrController = TextEditingController();

  Future<void> initializePrinter() async {
    try {
      await _flutterPrinterPlugin.initialize();
      setState(() {
        _homeUiState.error = null;
      });
    } on PrinterException catch (e) {
      setState(() => _homeUiState.error = 'Failed to initialize: ${e.message}');
    } finally {
    }
  }

  Future<void> printText() async {
    setState(() => _homeUiState.isPrinting = true);
    try {
      await _flutterPrinterPlugin.printText(
        _textController.text.trim(),
        align: _homeUiState.textAlignment,
        bold: _homeUiState.isBold,
      );
    } on PrinterException catch (e) {
      setState(() => _homeUiState.error = e.message);
    } finally {
      setState(() => _homeUiState.isPrinting = false);
    }
  }

  Future<void> printQR() async {
    setState(() => _homeUiState.isPrinting = true);
    try {
      await _flutterPrinterPlugin.printQRCode(
        _qrController.text.trim(),
        size: 200,
      );
    } on PrinterException catch (e) {
      setState(() => _homeUiState.error = e.message);
    } finally {
      setState(() => _homeUiState.isPrinting = false);
    }
  }

  Future<void> cutPaper() async {
    setState(() => _homeUiState.isPrinting = true);
    try {
      await _flutterPrinterPlugin.cutPaper();
    } on PrinterException catch (e) {
      setState(() => _homeUiState.error = e.message);
    } finally {
      setState(() => _homeUiState.isPrinting = false);
    }
  }

  Future<void> disconnect() async {
    try {
      await _flutterPrinterPlugin.disconnect();
    } on PrinterException catch (e) {
      setState(() => _homeUiState.error = e.message);
    }
  }

  Color getColor(PrinterStatus printerStatus) {
    switch (printerStatus) {
      case Connected():
        return Colors.green;
      case Disconnected():
        return Colors.red;
      case OutOfPaper():
        return Colors.orange;
      case Error():
        return Colors.red;
      case Initializing():
        return Colors.blueAccent;
    }
  }

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<PrinterStatus>(
      stream: _flutterPrinterPlugin.statusStream,
      initialData: Disconnected(),
      builder: (context, snapshot) {
        final currentStatus = snapshot.data ?? Disconnected();

        return Scaffold(
          appBar: AppBar(
            backgroundColor: Theme.of(context).colorScheme.inversePrimary,
            title: Text(widget.title),
          ),
          body: _buildBody(context, currentStatus),
          floatingActionButton: null,
        );
      },
    );
  }

  Widget _buildBody(BuildContext context, PrinterStatus currentStatus) {
    return SingleChildScrollView(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Status Card
            Card(
              elevation: 4,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Printer Status',
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                    const SizedBox(height: 12),
                    Row(
                      children: [
                        Container(
                          width: 12,
                          height: 12,
                          decoration: BoxDecoration(
                            color: getColor(currentStatus),
                            shape: BoxShape.circle,
                          ),
                        ),
                        const SizedBox(width: 8),
                        Text(
                          currentStatus.name,
                          style: Theme.of(context).textTheme.bodyMedium
                              ?.copyWith(fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(width: 8),
                        if (_homeUiState.isPrinting) ...[
                          Text(
                            '(Printing)',
                            style: Theme
                                .of(context)
                                .textTheme
                                .bodyMedium
                                ?.copyWith(fontWeight: FontWeight.bold),
                          ),
                        ]
                      ],
                    ),
                    const SizedBox(height: 12),
                    if (_homeUiState.error != null) ...[
                      Text(
                        'ERROR: ${_homeUiState.error}',
                        style: Theme
                            .of(context)
                            .textTheme
                            .bodyMedium
                            ?.copyWith(fontWeight: FontWeight.bold),
                      ),
                    ]
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Initialize Button
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: currentStatus is Connected
                    ? null
                    : () {
                        initializePrinter();
                      },
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 12),
                  backgroundColor: Colors.deepPurple,
                  foregroundColor: Colors.white,
                ),
                child: const Text('Initialize Printer'),
              ),
            ),

            // Print Text Section
            if (currentStatus is Connected) ...[
              const SizedBox(height: 24),
              Text(
                'Print Text',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 12),
              TextField(
                controller: _textController,
                decoration: InputDecoration(
                  hintText: 'Enter text to print',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  contentPadding: const EdgeInsets.all(12),
                ),
                maxLines: 2,
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: DropdownButton<TextAlignment>(
                      value: _homeUiState.textAlignment,
                      isExpanded: true,
                      onChanged: (value) {
                        setState(
                          () => _homeUiState.textAlignment =
                              value ?? TextAlignment.start,
                        );
                      },
                      items: const [
                        DropdownMenuItem(
                          value: TextAlignment.start,
                          child: Text('Align Start'),
                        ),
                        DropdownMenuItem(
                          value: TextAlignment.center,
                          child: Text('Align Center'),
                        ),
                        DropdownMenuItem(
                          value: TextAlignment.end,
                          child: Text('Align End'),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: CheckboxListTile(
                      title: const Text('Bold'),
                      value: _homeUiState.isBold,
                      onChanged: (value) {
                        setState(() => _homeUiState.isBold = value ?? false);
                      },
                      contentPadding: EdgeInsets.zero,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: printText,
                  child: const Text('Print Text'),
                ),
              ),
              const SizedBox(height: 24),

              // Print QR Code Section
              Text(
                'Print QR Code',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 12),
              TextField(
                controller: _qrController,
                decoration: InputDecoration(
                  hintText: 'Enter QR code data',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  contentPadding: const EdgeInsets.all(12),
                ),
                maxLines: 2,
              ),
              const SizedBox(height: 12),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: printQR,
                  child: const Text('Print QR Code'),
                ),
              ),
              const SizedBox(height: 24),

              // Paper Cut Section
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: cutPaper,
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 12),
                      ),
                      child: const Text('Cut Paper'),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: disconnect,
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 12),
                        backgroundColor: Colors.red,
                        foregroundColor: Colors.white,
                      ),
                      child: const Text('Disconnect'),
                    ),
                  ),
                ],
              ),
            ],
          ],
        ),
      ),
    );
  }
}
