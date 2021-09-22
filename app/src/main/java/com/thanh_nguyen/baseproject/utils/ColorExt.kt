package com.thanh_nguyen.baseproject.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import com.thanh_nguyen.baseproject.app.AppApplication


fun colorTransform(from: Int, to: Int, onColorUpdate: (Int) -> Unit){
    val colorFrom: Int = AppApplication.getContext().resources.getColor(from, null)
    val colorTo: Int = AppApplication.getContext().resources.getColor(to, null)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250 // milliseconds

    colorAnimation.addUpdateListener {
            animator -> onColorUpdate.invoke(animator.animatedValue as Int)
    }
    colorAnimation.start()
}