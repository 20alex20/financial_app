package com.example.finances.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.finances.core.di.ActivityComponent
import com.example.finances.core.di.DaggerActivityComponent
import com.example.finances.core.ui.MainScreen
import com.example.finances.core.utils.SyncTimeManager

class MainActivity : ComponentActivity() {
    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        activityComponent = DaggerActivityComponent.factory().create(this, appComponent)
        activityComponent.dataSyncOnConnection()
        activityComponent.splashScreenAnimator()

        val lastSyncTime = SyncTimeManager.getLastSyncTime(this)
        Toast.makeText(this, "Last sync: $lastSyncTime", Toast.LENGTH_LONG).show()

        enableEdgeToEdge()
        setContent {
            MainScreen(activityComponent.viewModelFactory())
        }
    }
}
