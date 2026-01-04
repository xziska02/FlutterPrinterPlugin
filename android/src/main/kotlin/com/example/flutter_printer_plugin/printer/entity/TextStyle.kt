package com.example.flutter_printer_plugin.printer.entity

sealed interface TextStyle {
    fun style(text: String): String

    object NormalTextStyle : TextStyle {
        override fun style(text: String): String = text
    }

    object ItalicTextStyle : TextStyle {
        override fun style(text: String): String = "?i?$text?i?"
    }

    object BoldTextStyle : TextStyle {
        override fun style(text: String): String = text.uppercase()
    }
}

