package com.example.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finances.features.navigation.ui.NavGraph
import com.example.finances.features.network.domain.NetworkConnectionObserver
import com.example.finances.features.splash.domain.SplashScreenAnimator
import com.example.finances.ui.theme.FinancesTheme

/**
 * Activity, отвечающая за отображение главного UI и инициализацию/запуск доп функционала
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkConnectionObserver.init(this)
        SplashScreenAnimator.init(this)

        enableEdgeToEdge()
        setContent {
            FinancesTheme(dynamicColor = false) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavGraph(
                        Modifier.padding(0.dp, 0.dp, 0.dp, innerPadding.calculateBottomPadding())
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkConnectionObserver.release()
    }
}
