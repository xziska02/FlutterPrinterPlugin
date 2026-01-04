Flutter Printer Plugin - Setup and Run Instructions
=====================================================

SETUP
-----
1. Make sure you have Flutter installed
2. Clone or navigate to the project directory
3. Install dependencies:
   flutter pub get

4. Navigate to example directory:
   cd example
   flutter pub get

RUN THE EXAMPLE APP
-------------------
1. Connect an Android device or start an emulator
2. From the example directory, run:
   flutter run

HOW THE MOCK PRINTER WORKS
--------------------------
The plugin uses a mock printer implemented in PaxPrinter that simulates hardware behavior:

- All print output goes to Android logcat (not real paper)
- View output in Android Studio Logcat or with: adb logcat

Mock Features:
- Simulates delays (200-800ms for init, 75ms per line for printing)
- Random initialization failures (1 in 6 chance) to test error handling
- Automatically shows "Out of Paper" after 10 print operations
- Paper width: 42 characters (default)
- Status updates sent to Flutter app via EventChannel

USAGE
-----
import 'package:flutter_printer_plugin/flutter_printer_plugin.dart';

final printer = FlutterPrinterPlugin();

// Initialize printer
await printer.initialize();

// Print text
await printer.printText('Hello World', align: TextAlignment.center, bold: true);

// Print QR code
await printer.printQRCode('https://example.com', size: 200);

// Cut paper
await printer.cutPaper();

// Listen to status changes
printer.statusStream.listen((status) {
print('Printer status: ${status.name}');
});

// Disconnect
await printer.disconnect();

STATUS TYPES
------------
- Connected: Printer is ready
- Disconnected: Printer is not connected
- OutOfPaper: No paper available (simulated after 10 prints)
- Error: Printer error occurred

NOTES
-----
- All print output appears in logcat, not on physical paper
- The mock simulates real printer timing and error conditions

Simple output
-------------
- Receipt

I/System.out(22316): ------------------------------------------

I/System.out(22316):   ******** TICKET SALE RECEIPT ********   
I/System.out(22316):             STORE: Demo Events            
I/System.out(22316):           DATE: 2026-01-02 19:00          
I/System.out(22316):               ORDER: 123456               
I/System.out(22316):                                           
I/System.out(22316):            ITEM: Concert Ticket           
I/System.out(22316):           QTY: 1   PRICE: 25.00           
I/System.out(22316):                                           
I/System.out(22316):                TOTAL: 25.00               
I/System.out(22316):                 PAID: CARD                
I/System.out(22316):                                           
I/System.out(22316):       ------------------------------      
I/System.out(22316):        Thank you for your purchase        
I/System.out(22316): ------------------------------------------

- QR code

I/System.out(22316): ------------------------------------------

I/System.out(22316):             QR CODE (200x200)             
I/System.out(22316):             Data: QR Code data            
I/System.out(22316):               █    █    █                 
I/System.out(22316):                 █    █    █               
I/System.out(22316):                   █    █                  
I/System.out(22316):               █    █    █                 
I/System.out(22316):                 █    █    █               
I/System.out(22316):                   █    █                  
I/System.out(22316):               █    █    █                 
I/System.out(22316):                 █    █    █               
I/System.out(22316): ------------------------------------------

- Cut paper

I/System.out(22316): ==========================================

I/System.out(22316):             === PAPER CUT! ===            
I/System.out(22316): ==========================================
