package com.panassevich.panassevich.util.loans.ui

import android.animation.ObjectAnimator
import android.view.View

const val DEFAULT_DURATION = 250L

fun View.shake(amplitude: Float = 15f, duration: Long = DEFAULT_DURATION) {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_X,
        0f,
        amplitude,
        -amplitude,
        0f
    ).setDuration(duration).start()
}

fun shakeAnimator(target: View, amplitude: Float = 15f, duration: Long = DEFAULT_DURATION) =
    ObjectAnimator.ofFloat(
        target,
        View.TRANSLATION_X,
        0f,
        amplitude,
        -amplitude,
        0f
    ).setDuration(duration)