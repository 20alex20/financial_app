package com.example.finances.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.finances.core.di.ActivityComponent
import com.example.finances.core.ui.MainScreen

/**
 * Activity, отвечающая за отображение главного UI и инициализацию/запуск доп функционала
 */
class MainActivity : ComponentActivity() {
    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        activityComponent = appComponent.activityComponent().create(this)

        enableEdgeToEdge()
        setContent {
            MainScreen(activityComponent.viewModelFactory())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityComponent.networkConnectionObserver().unregister()
    }
}
