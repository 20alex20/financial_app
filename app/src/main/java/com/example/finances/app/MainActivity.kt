package com.example.finances.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.DaggerActivityComponent
import com.example.finances.app.navigation.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = DaggerActivityComponent.factory().create(this, appComponent)
        activityComponent.networkConnectionObserver()

        enableEdgeToEdge()
        setContent {
            MainScreen(
                activityComponent.appNavigationCoordinator()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityComponent.networkConnectionObserver().unregister()
    }
}
