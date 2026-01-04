package com.example.flutter_printer_plugin

import com.example.flutter_printer_plugin.printer.IPrinter
import com.example.flutter_printer_plugin.printer.PaxPrinter
import com.example.flutter_printer_plugin.status.IPrinterStatusManager
import com.example.flutter_printer_plugin.status.PrinterStatusManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class FlutterPrinterPlugin : FlutterPlugin, ActivityAware {

    private lateinit var methodChannel: MethodChannel
    private lateinit var statusChannel: EventChannel

    private lateinit var printer: IPrinter
    private lateinit var statusManager: IPrinterStatusManager
    private lateinit var methodCallHandler: PrinterMethodCallHandler
    private lateinit var printerStreamHandler: PrinterStreamHandler

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        initializeDependencies()

        methodChannel = MethodChannel(binding.binaryMessenger, "flutter_printer_plugin")
        methodChannel.setMethodCallHandler(methodCallHandler)

        statusChannel = EventChannel(binding.binaryMessenger, "flutter_printer_plugin_status")
        statusChannel.setStreamHandler(printerStreamHandler)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        statusChannel.setStreamHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {}
    override fun onDetachedFromActivityForConfigChanges() {}
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}
    override fun onDetachedFromActivity() {}

    private fun initializeDependencies() {
        statusManager = PrinterStatusManager()
        printer = PaxPrinter(statusManager)

        methodCallHandler = PrinterMethodCallHandler(printer)
        printerStreamHandler = PrinterStreamHandler(statusManager)
    }
}

