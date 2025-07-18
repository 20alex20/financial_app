package com.example.finances.core.managers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LogMessageWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "LogMessageWorker"
    }

    override fun doWork(): Result {
        Log.d(TAG, "Scheduled message: Hello from LogMessageWorker!")
        return Result.success()
    }
} 