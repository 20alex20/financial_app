package com.example.finances.core.managers

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SyncTimeManager {
    private const val SYNC_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val SYNC_TIME_FILE = "last_sync_time.txt"
    private val formatter = DateTimeFormatter.ofPattern(SYNC_TIME_FORMAT)

    fun updateLastSyncTime(context: Context) {
        try {
            val currentTime = LocalDateTime.now().format(formatter)
            val file = File(context.filesDir, SYNC_TIME_FILE)
            FileWriter(file).use { writer ->
                writer.write(currentTime)
            }
        } catch (_: Exception) {
        }
    }

    fun getLastSyncTime(context: Context): String {
        return try {
            val file = File(context.filesDir, SYNC_TIME_FILE)
            if (file.exists()) {
                BufferedReader(FileReader(file)).use { reader ->
                    reader.readLine() ?: "Never synced"
                }
            } else {
                "Never synced"
            }
        } catch (_: Exception) {
            "Never synced"
        }
    }
}
