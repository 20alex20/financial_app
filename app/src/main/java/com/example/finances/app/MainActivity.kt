package com.example.finances.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.core.ui.MainScreen
import com.example.finances.core.ui.theme.FinancesTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationCoordinator: AppNavigationCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DI
        (application as FinancesApplication)
            .appComponent
            .activityComponent()
            .create(this, (application as FinancesApplication).appComponent)
            .inject(this)

        setContent {
            FinancesTheme {
                MainScreen(navigationCoordinator)
            }
        }
    }
}
