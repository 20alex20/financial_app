package com.example.finances.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.DaggerActivityComponent

class MainActivity : ComponentActivity() {
    private lateinit var activityComponent: ActivityComponent

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = DaggerActivityComponent.factory().create(this, appComponent)
        activityComponent.splashScreenAnimator()

        enableEdgeToEdge()
        setContent {
            MainScreen(
                activityComponent.externalSettingsRepo(),
                activityComponent.viewModelFactory(),
                activityComponent.appNavigationCoordinator(),
                activityComponent.vibrateUseCase()
            )
        }
    }
}
