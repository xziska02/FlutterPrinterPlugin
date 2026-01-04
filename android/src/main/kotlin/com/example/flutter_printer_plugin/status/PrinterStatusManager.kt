package com.example.flutter_printer_plugin.status

import com.example.flutter_printer_plugin.status.entity.PrinterStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PrinterStatusManager : IPrinterStatusManager {
    private val _statusFlow: MutableStateFlow<PrinterStatus> =
        MutableStateFlow(PrinterStatus.DISCONNECTED)

    override fun emitStatus(printerStatus: PrinterStatus) {
        _statusFlow.tryEmit(printerStatus)
    }

    override fun getStatusFlow(): Flow<PrinterStatus> = _statusFlow

    override fun getCurrentStatus(): PrinterStatus = _statusFlow.value
}