package com.andresuryana.dicodingstories.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationHelper {

    fun slideUpWithFadeIn(target: View, duration: Long): AnimatorSet {
        val fadeInAnim = ObjectAnimator.ofFloat(target, View.ALPHA, 0f, 1f).setDuration(duration)
        val slideUpAnim = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, 50f, 0f).setDuration(duration)

        return AnimatorSet().apply {
            playTogether(fadeInAnim, slideUpAnim)
        }
    }
}