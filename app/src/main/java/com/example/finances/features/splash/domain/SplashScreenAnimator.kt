package com.example.finances.features.splash.domain

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider

object SplashScreenAnimator {
    private var splashViewProvider: SplashScreenViewProvider? = null

    private val removeSplashView = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            splashViewProvider?.remove()
        }
    }

    fun init(activity: Activity) {
        val splashScreen = activity.installSplashScreen()
        splashScreen.setOnExitAnimationListener { provider ->
            AnimatorSet().apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
                playTogether(
                    ObjectAnimator.ofFloat(provider.view, View.SCALE_X, 1f, 1.3f),
                    ObjectAnimator.ofFloat(provider.view, View.SCALE_Y, 1f, 1.3f),
                    ObjectAnimator.ofFloat(provider.view, View.ALPHA, 1f, 0f)
                )
                addListener(removeSplashView)
                start()
            }.also { splashViewProvider = provider }
        }
    }
}
