package com.example.flutter_printer_plugin.printer

import com.example.flutter_printer_plugin.printer.entity.TextAlignment
import com.example.flutter_printer_plugin.printer.entity.TextStyle

interface IPrinter {

    suspend fun initialize(): Boolean

    suspend fun printText(
        text: String,
        textAlignment: TextAlignment,
        textStyle: TextStyle,
    )

    suspend fun printQRCode(data: String, size: Int)

    suspend fun cutPaper()

    suspend fun disconnect()
}