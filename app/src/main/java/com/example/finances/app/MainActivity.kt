package com.example.finances.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.DaggerActivityComponent
import com.example.finances.core.ui.theme.LocalThemeController

class MainActivity : ComponentActivity() {
    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = DaggerActivityComponent.factory().create(this, appComponent)
        activityComponent.splashScreenAnimator()

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalThemeController provides activityComponent.themeController()
            ) {
                MainScreen(
                    activityComponent.appNavigationCoordinator(),
                    activityComponent.viewModelFactory(),
                    activityComponent.vibrateUseCase()
                )
            }
        }
    }
}
