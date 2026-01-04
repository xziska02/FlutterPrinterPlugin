package com.example.flutter_printer_plugin.method_handler

import com.example.flutter_printer_plugin.printer.IPrinter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class InitializeMethodHandler(private val printer: IPrinter) : IMethodHandler {
    override suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        try {
            val initialized = printer.initialize()
            result.success(initialized)
        } catch (e: Exception) {
            result.error(
                "INITIALIZATION_ERROR",
                "Failed to initialize printer: ${e.message}",
                null
            )
        }
    }
}