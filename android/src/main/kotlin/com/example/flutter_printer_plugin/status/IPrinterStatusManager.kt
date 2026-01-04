package com.example.flutter_printer_plugin.status

import com.example.flutter_printer_plugin.status.entity.PrinterStatus
import kotlinx.coroutines.flow.Flow

interface IPrinterStatusManager {

    fun emitStatus(printerStatus: PrinterStatus)

    fun getStatusFlow(): Flow<PrinterStatus>

    fun getCurrentStatus(): PrinterStatus
}