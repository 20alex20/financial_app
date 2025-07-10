package com.example.finances.core.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.example.finances.core.di.ActivityScope
import javax.inject.Inject

/**
 * Responsible for initializing and launching splash screen animation
 */
@ActivityScope
class SplashScreenAnimator @Inject constructor(activity: Activity) {
    private var splashViewProvider: SplashScreenViewProvider? = null

    private val removeSplashView = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            splashViewProvider?.remove()
        }
    }

    init {
        val splashScreen = activity.installSplashScreen()
        splashScreen.setOnExitAnimationListener { provider ->
            AnimatorSet().apply {
                duration = ANIMATION_DURATION
                interpolator = AccelerateDecelerateInterpolator()
                playTogether(
                    ObjectAnimator.ofFloat(provider.view, View.SCALE_X, SCALE_START, SCALE_END),
                    ObjectAnimator.ofFloat(provider.view, View.SCALE_Y, SCALE_START, SCALE_END),
                    ObjectAnimator.ofFloat(provider.view, View.ALPHA, ALPHA_START, ALPHA_END)
                )
                addListener(removeSplashView)
                start()
            }.also { splashViewProvider = provider }
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 500L
        private const val SCALE_START = 1f
        private const val SCALE_END = 1.3f
        private const val ALPHA_START = 1f
        private const val ALPHA_END = 0f
    }
} 