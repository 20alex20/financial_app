package com.example.finances.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Activity, отвечающая за отображение главного UI и инициализацию/запуск доп функционала
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkConnectionObserver.init(this)
        SplashScreenAnimator.init(this)

        enableEdgeToEdge()
        setContent { MainScreen() }
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkConnectionObserver.release()
    }
}
