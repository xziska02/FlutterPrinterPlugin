package com.example.flutter_printer_plugin

import com.example.flutter_printer_plugin.method_handler.CutPaperMethodHandler
import com.example.flutter_printer_plugin.method_handler.DisconnectMethodHandler
import com.example.flutter_printer_plugin.method_handler.InitializeMethodHandler
import com.example.flutter_printer_plugin.method_handler.PrintQRMethodHandler
import com.example.flutter_printer_plugin.method_handler.PrintTextMethodHandler
import com.example.flutter_printer_plugin.printer.IPrinter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PrinterMethodCallHandler(
    printer: IPrinter,
) : MethodChannel.MethodCallHandler {
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val methodHandlers = mapOf(
        "initialize" to InitializeMethodHandler(printer),
        "printText" to PrintTextMethodHandler(printer),
        "printQRCode" to PrintQRMethodHandler(printer),
        "cutPaper" to CutPaperMethodHandler(printer),
        "disconnect" to DisconnectMethodHandler(printer),
    )

    override fun onMethodCall(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        scope.launch {
            try {
                val methodHandler = methodHandlers[call.method]
                if (methodHandler != null) {
                    methodHandler.handle(call, result)
                } else {
                    result.notImplemented()
                }
            } catch (e: Exception) {
                result.error(
                    "UNKNOWN_ERROR",
                    "An unexpected error occurred: ${e.message}",
                    null
                )
            }
        }
    }
}