package com.example.flutter_printer_plugin.method_handler

import com.example.flutter_printer_plugin.printer.IPrinter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PrintQRMethodHandler(private val printer: IPrinter) : IMethodHandler {
    override suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        val data = call.argument<String>("data")
        val size = call.argument<Int>("size") ?: 200

        // Parameter validation
        if (data.isNullOrEmpty()) {
            result.error(
                "INVALID_PARAMETER",
                "QR code data cannot be empty",
                null
            )
            return
        }

        if (data.length > 2953) {
            result.error(
                "DATA_TOO_LONG",
                "QR code data is too long. Maximum length is 2953 characters",
                null
            )
            return
        }

        if (size < 50 || size > 1000) {
            result.error(
                "INVALID_SIZE",
                "QR code size must be between 50 and 1000. Provided: $size",
                null
            )
            return
        }

        try {
            printer.printQRCode(data = data, size = size)
            result.success(null)
        } catch (e: Exception) {
            result.error(
                "QR_PRINT_ERROR",
                "Failed to print QR code: ${e.message}",
                null
            )
        }
    }
}

