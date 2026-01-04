package com.example.flutter_printer_plugin

import com.example.flutter_printer_plugin.status.IPrinterStatusManager
import io.flutter.plugin.common.EventChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class PrinterStreamHandler(
    private val statusManager: IPrinterStatusManager,
) : EventChannel.StreamHandler {
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var job: Job? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // Collect the Flow inside a coroutine
        job = scope.launch {
            statusManager.getStatusFlow().collect { status ->
                withContext(Dispatchers.Main) {
                    events?.success(status.name)
                }
            }
        }
    }

    override fun onCancel(arguments: Any?) {
        // Stop collecting when Flutter stops listening
        job?.cancel()
    }
}