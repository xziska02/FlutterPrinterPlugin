package com.example.flutter_printer_plugin.printer

import com.example.flutter_printer_plugin.extensions.align
import com.example.flutter_printer_plugin.printer.entity.TextAlignment
import com.example.flutter_printer_plugin.printer.entity.TextStyle
import com.example.flutter_printer_plugin.status.IPrinterStatusManager
import com.example.flutter_printer_plugin.status.entity.PrinterStatus
import kotlinx.coroutines.delay
import kotlin.math.min

/**
 * Mock implementation of a thermal printer for development and testing purposes.
 * 
 * This class simulates hardware behavior without requiring actual printer hardware:
 */
class PaxPrinter(
    private val statusManager: IPrinterStatusManager,
    private val paperWidth: Int = 42,
    private val border: String = "-".repeat(paperWidth),
    private val cutPaperBorder: String = "=".repeat(paperWidth),
) : IPrinter {
    // Mock out of paper state after printing
    private val maxPrintsBeforeOutOfPaper: Int = 10 // Simulate OutOfPaper after 10 prints
    private var printCount = 0
        set(value) {
            field = value

            if (field >= maxPrintsBeforeOutOfPaper) {
                statusManager.emitStatus(PrinterStatus.OUT_OF_PAPER)
                println("--- Out of Paper! ---".align(TextAlignment.CENTER, paperWidth))
            }
        }

    /**
     * Method initializes printer with randomized error initialization to showcase error states.
     */
    override suspend fun initialize(): Boolean {
        // Simulate variable initialization time (200ms to 800ms)
        // Real printers can take different amounts of time to initialize
        delay(200L * (1..4).random())

        println("--- Initializing ---".align(TextAlignment.CENTER, paperWidth))

        // Mock hardware initialization: 1 in 6 chance for error
        // This allows testing error handling in the Flutter app
        val isInitialized = (1..6).random() != 6

        if (isInitialized) {
            statusManager.emitStatus(PrinterStatus.CONNECTED)
            printCount = 0 // Reset print count on initialization
            println("--- Initialized  ---".align(TextAlignment.CENTER, paperWidth))
        } else {
            statusManager.emitStatus(PrinterStatus.ERROR)
            println("--- FAILED ---")
            throw IllegalArgumentException("Internal system malfunction [Randomized error]")
        }

        return true
    }

    /**
     * Print text in console, there are added some delays to simulate
     * simple printing job.
     */
    override suspend fun printText(
        text: String,
        textAlignment: TextAlignment,
        textStyle: TextStyle,
    ) {
        val lines = text.split("\n")

        // Simulate printer preparation time
        delay(200L)
        println(border)
        
        // Print each line with formatting and per-line delay
        for (line in lines) {
            // Align text to paper width and apply style
            val formatted = line.align(textAlignment, paperWidth)
            val finalOutput = textStyle.style(formatted)
            println(finalOutput)
            // Simulate line-by-line printing speed
            delay(75L)
        }
        println(border)

        // Simulate printer finishing operations
        delay(100L)
        // Increment print count - may trigger OUT_OF_PAPER status if threshold reached
        printCount++
    }

    /**
     * Prints simple format of QR in console,there are added some delays to simulate
     * simple printing job.
     */
    override suspend fun printQRCode(data: String, size: Int) {
        // Simulate QR code printing delay
        delay(200L)

        println(border)
        println("QR CODE (${size}x${size})".align(TextAlignment.CENTER, paperWidth))
        println("Data: $data".align(TextAlignment.CENTER, paperWidth))
        println()

        // Simulate QR code visual representation
        val qrSize = maxOf(min(size, paperWidth) / 10 , 8) // Scale down for console output

        // Print each line of QR
        for (i in 0 until qrSize) {
            val qrLine = buildString {
                for (j in 0 until qrSize) {
                    val pattern = (i * qrSize + j) % 3
                    append(if (pattern == 0) "█" else "  ")
                }
            }
            println(qrLine.align(TextAlignment.CENTER, paperWidth))
            // Simulate line-by-line printing speed
            delay(75L)
        }
        println()
        println(border)

        // Increment print count and simulate OutOfPaper after maxPrintsBeforeOutOfPaper prints
        delay(100L)
        printCount++
    }

    /**
     * Print "paper cut" after 250ms delay
     */
    override suspend fun cutPaper() {
        delay(250L)
        println(cutPaperBorder)
        println("=== PAPER CUT! ===".align(TextAlignment.CENTER, paperWidth))
        println(cutPaperBorder)
        printCount++
    }

    /**
     * Disconnects printer after simulated 200ms delay
     */
    override suspend fun disconnect() {
        delay(200L)
        println("--- DISCONNECTED! ---".align(TextAlignment.CENTER, paperWidth))

        statusManager.emitStatus(PrinterStatus.DISCONNECTED)
    }
}