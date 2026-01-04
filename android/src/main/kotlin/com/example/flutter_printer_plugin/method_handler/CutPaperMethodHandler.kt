package com.example.flutter_printer_plugin.method_handler

import com.example.flutter_printer_plugin.printer.IPrinter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class CutPaperMethodHandler(private val printer: IPrinter) : IMethodHandler {
    override suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        try {
            printer.cutPaper()
            result.success(null)
        } catch (e: Exception) {
            result.error(
                "CUT_PAPER_ERROR",
                "Failed to cut paper: ${e.message}",
                null
            )
        }
    }
}

