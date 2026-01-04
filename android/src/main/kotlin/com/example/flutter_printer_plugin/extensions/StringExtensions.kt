package com.example.flutter_printer_plugin.extensions

import com.example.flutter_printer_plugin.printer.entity.TextAlignment

/**
 * Algins text based on
 *
 * @param textAlignment parameter defines on which place the text will be placed
 * @param width parameter defines the width of "paper"
 */
fun String.align(textAlignment: TextAlignment, width: Int = 32): String {
    val content = this.take(width)
    return when (textAlignment) {
        TextAlignment.CENTER -> content.padStart((width + content.length) / 2).padEnd(width)
        TextAlignment.END -> content.padStart(width)
        TextAlignment.START -> content.padEnd(width)
    }
}