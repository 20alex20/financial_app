package com.example.financial_app.features.expenses.domain.usecase

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShowErrorUseCase(private val context: Context) {
    suspend operator fun invoke(message: String) = withContext(Dispatchers.Main) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }
} 