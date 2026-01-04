package com.example.flutter_printer_plugin.method_handler

import com.example.flutter_printer_plugin.printer.IPrinter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class DisconnectMethodHandler(private val printer: IPrinter) : IMethodHandler {
    override suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        try {
            printer.disconnect()
            result.success(null)
        } catch (e: Exception) {
            result.error(
                "DISCONNECT_ERROR",
                "Failed to disconnect: ${e.message}",
                null
            )
        }
    }
}

