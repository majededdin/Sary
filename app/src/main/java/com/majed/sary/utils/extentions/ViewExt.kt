package com.majed.sary.utils.extentions

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Service
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard() = kotlin.run {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard() = kotlin.run {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.showSnackbar(message: String, duration: Int) =
    Snackbar.make(this, message, duration).show()

fun View.showSnackbar(message: String, duration: Int, callback: Snackbar.Callback?) =
    Snackbar.make(this, message, duration).addCallback(callback).show()

fun View.toVisible() = kotlin.run { this.visibility = View.VISIBLE }

fun View.toGone() = kotlin.run { this.visibility = View.GONE }

fun View.toInvisible() = kotlin.run { this.visibility = View.INVISIBLE }

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.expandView() {
    this.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT)
    val targetHeight: Int = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.width = 1
    this.toVisible()
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            this@expandView.layoutParams.width =
                if (interpolatedTime == 1f) RelativeLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            this@expandView.requestLayout()
        }

        override fun willChangeBounds(): Boolean = true
    }
    a.duration = (targetHeight / 150).toLong()
    this.startAnimation(a)
}

fun View.collapseView() {
    val initialHeight: Int = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                this@collapseView.toGone()
            } else {
                this@collapseView.layoutParams.width =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapseView.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean = true
    }

    // 1dp/ms
    a.duration = ((initialHeight * 3) / this.context.resources.displayMetrics.density).toLong()
//        a.setDuration(500);
    this.startAnimation(a)
}

fun View.expandVertical() {
    this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val targetHeight: Int = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.toVisible()

    val va: ValueAnimator = ValueAnimator.ofInt(1, targetHeight)
    va.addUpdateListener { animation ->
        this@expandVertical.layoutParams.height = animation.animatedValue as Int
        this@expandVertical.requestLayout()
    }
    va.addListener(object : Animator.AnimatorListener {

        override fun onAnimationEnd(animation: Animator) {
            this@expandVertical.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    va.duration = 300
    va.interpolator = OvershootInterpolator()
    va.start()
}

fun View.collapseVertical() {
    val initialHeight: Int = this.measuredHeight

    val va: ValueAnimator = ValueAnimator.ofInt(initialHeight, 0)
    va.addUpdateListener { animation ->
        this@collapseVertical.layoutParams.height = animation.animatedValue as Int
        this@collapseVertical.requestLayout()
    }
    va.addListener(object : Animator.AnimatorListener {

        override fun onAnimationEnd(animation: Animator) {
            this@collapseVertical.toGone()
        }

        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    va.duration = 300
    va.interpolator = DecelerateInterpolator()
    va.start()
}