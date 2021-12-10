package com.majed.sary.utils.extentions

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.majed.sary.R

private fun Activity.setTransition() {
    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
}

// ------------------------------------------- Activity Normal -------------------------------------

fun Activity.intentNormal(aClass: Class<*>) = intentNormal(aClass, false)

fun Activity.intentNormal(aClass: Class<*>, finish: Boolean) {
    startActivity(Intent(this, aClass))
    if (finish) finish()
}

fun Activity.intentNormal(aClass: Class<*>, bundle: Bundle) = intentNormal(aClass, bundle, false)

fun Activity.intentNormal(aClass: Class<*>, bundle: Bundle, finish: Boolean) {
    startActivity(Intent(this, aClass).putExtras(bundle))
    if (finish) finish()
}

// ------------------------------------------- Activity Slide InOut --------------------------------

fun Activity.intentWithSlideInOut(aClass: Class<*>) = intentWithSlideInOut(aClass, false)

fun Activity.intentWithSlideInOut(aClass: Class<*>, finish: Boolean) {
    startActivity(Intent(this, aClass))
    if (finish) finish()
    setTransition()
}

fun Activity.intentWithSlideInOutFinishAll(aClass: Class<*>) {
    startActivity(Intent(this, aClass))
    finishAffinity()
    setTransition()
}

// ------------------------------------------- Activity Bundle Slide InOut -------------------------

fun Activity.intentWithSlideInOut(aClass: Class<*>, bundle: Bundle) =
    intentWithSlideInOut(aClass, bundle, false)

fun Activity.intentWithSlideInOut(aClass: Class<*>, bundle: Bundle, finish: Boolean) {
    startActivity(Intent(this, aClass).putExtras(bundle))
    if (finish) finish()
    setTransition()
}

// ------------------------------------------- Activity Result Bundle Slide InOut -------------------------

fun Activity.intentResultWithSlideInOut(aClass: Class<*>) = intentResultWithSlideInOut(aClass, null)

fun Activity.intentResultWithSlideInOut(aClass: Class<*>, bundle: Bundle?): Intent {
    val intent = Intent(this, aClass)
    bundle?.let { intent.putExtras(it) }
    setTransition()
    return intent
}

// ------------------------------------------- Fragment Fade ---------------------------------------

fun FragmentActivity.loadWithFade(fragment: Fragment, containerView: Int) = kotlin.run {
    loadWithFade(fragment, containerView, false)
}

fun FragmentActivity.loadWithFade(
    fragment: Fragment, containerView: Int, withBackStack: Boolean
) = supportFragmentManager.commit {
    setCustomAnimations(
        android.R.anim.fade_in, android.R.anim.fade_out,
        android.R.anim.fade_in, android.R.anim.fade_out
    )
    replace(containerView, fragment, fragment.javaClass.name)
    if (withBackStack)
        addToBackStack(fragment.javaClass.name)
}

// ------------------------------------------- Fragment Slide InOut --------------------------------

fun FragmentActivity.loadWithSlideInOut(fragment: Fragment, containerView: Int) = kotlin.run {
    loadWithSlideInOut(fragment, containerView, false)
}

fun FragmentActivity.loadWithSlideInOut(
    fragment: Fragment, containerView: Int, withBackStack: Boolean
) {
    supportFragmentManager.commit {
        setCustomAnimations(
            R.anim.from_right_in, android.R.anim.fade_out,
            android.R.anim.fade_in, R.anim.from_left_out
        )
        replace(containerView, fragment, fragment.javaClass.name)
        if (withBackStack)
            addToBackStack(fragment.javaClass.name)
    }
}

// ------------------------------------------- Fragment  -------------------------------------------

fun FragmentActivity.loadFragment(fragment: Fragment, containerView: Int) = kotlin.run {
    loadFragment(fragment, containerView, false)
}

fun FragmentActivity.loadFragment(fragment: Fragment, containerView: Int, withBackStack: Boolean) {
    supportFragmentManager.commit {
        replace(containerView, fragment, fragment.javaClass.name)
        if (withBackStack)
            addToBackStack(fragment.javaClass.name)
    }
}

@Suppress("DEPRECATION")
fun Activity.transparentStatusBar() {
    this.window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //make fully Android Transparent Status bar
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        this.window.statusBarColor = Color.TRANSPARENT
    }
}

private fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = this.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}
