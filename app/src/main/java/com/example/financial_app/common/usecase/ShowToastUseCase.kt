package com.example.financial_app.common.usecase

import android.content.Context
import android.widget.Toast

class ShowToastUseCase(private val context: Context) {
    operator fun invoke(message: String) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }
}
