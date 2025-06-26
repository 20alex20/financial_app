package com.example.financial_app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.example.financial_app.features.navigation.ui.NavGraph
import com.example.financial_app.features.network.domain.NetworkConnectionObserver
import com.example.financial_app.ui.theme.FinancialAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkConnectionObserver.init(this)

        installSplashScreen().setOnExitAnimationListener(::animateSplashScreen)
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

fun animateSplashScreen(splashViewProvider: SplashScreenViewProvider) {
    val view = splashViewProvider.view
    val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 1.3f)
    val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 1.3f)
    val fadeOut = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)

    AnimatorSet().apply {
        duration = 500
        interpolator = AccelerateDecelerateInterpolator()
        playTogether(scaleX, scaleY, fadeOut)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) = splashViewProvider.remove()
        })
        start()
    }
}
