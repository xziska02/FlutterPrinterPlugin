package com.example.flutter_printer_plugin.method_handler

import com.example.flutter_printer_plugin.printer.IPrinter
import com.example.flutter_printer_plugin.printer.entity.TextAlignment
import com.example.flutter_printer_plugin.printer.entity.TextStyle
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PrintTextMethodHandler(private val printer: IPrinter) : IMethodHandler {
    override suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        val text = call.argument<String>("text")
        val textAlignment = call.argument<String>("alignment") ?: "start"
        val isBold = call.argument<Boolean>("isBold") ?: false

        val textStyle = if (isBold) {
            TextStyle.BoldTextStyle
        } else {
            TextStyle.NormalTextStyle
        }

        if (text.isNullOrEmpty()) {
            result.error(
                "INVALID_PARAMETER",
                "Text cannot be empty",
                null
            )
            return
        }

        if (text.length > 10000) {
            result.error(
                "TEXT_TOO_LONG",
                "Text is too long. Maximum length is 10000 characters",
                null
            )
            return
        }

        try {
            printer.printText(
                text = text,
                textAlignment = TextAlignment.fromString(textAlignment),
                textStyle = textStyle,
            )
            result.success(null)
        } catch (e: Exception) {
            result.error(
                "PRINT_ERROR",
                "Failed to print text: ${e.message}",
                null
            )
        }
    }
}

