package com.example.finances.core.managers

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import com.example.finances.core.di.ApplicationContext
import com.example.finances.core.di.CoreScope
import javax.inject.Inject

@CoreScope
class VibrateUseCase @Inject constructor(@ApplicationContext context: Context) {
    private var durationMs = 0L
    private val vibrator: Vibrator? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val vibratorManager = context.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as? VibratorManager
            vibratorManager?.defaultVibrator
        }
        else -> {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    operator fun invoke() {
        if (durationMs > 0 && vibrator != null && vibrator.hasVibrator()) vibrator.vibrate(
            VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

    fun setDuration(duration: Long) {
        durationMs = duration
    }
}
