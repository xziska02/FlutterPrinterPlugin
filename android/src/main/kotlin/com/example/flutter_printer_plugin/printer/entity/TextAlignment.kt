package com.example.flutter_printer_plugin.printer.entity
enum class TextAlignment {
    START,
    CENTER,
    END;

    companion object {
        fun fromString(value: String?): TextAlignment {
            return when (value) {
                "start" -> START
                "center" -> CENTER
                "end" -> END
                else -> START
            }
        }
    }
}