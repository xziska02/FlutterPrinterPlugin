package com.example.flutter_printer_plugin.method_handler

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

interface IMethodHandler {
    suspend fun handle(
        call: MethodCall,
        result: MethodChannel.Result,
    )
}