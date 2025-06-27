package com.example.financial_app

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
import com.example.financial_app.features.navigation.ui.NavGraph
import com.example.financial_app.features.network.domain.NetworkConnectionObserver
import com.example.financial_app.features.splash.domain.SplashScreenAnimator
import com.example.financial_app.ui.theme.FinancialAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkConnectionObserver.init(this)
        SplashScreenAnimator.init(this)

        enableEdgeToEdge()
        setContent {
            FinancialAppTheme(dynamicColor = false) {
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
