package com.majed.sary.utils.extentions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.majed.sary.R

fun Context.getApplicationVersionName(): String =
    this.packageManager.getPackageInfo(this.packageName, 0).versionName

fun Context.showToastAsShort(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToastAsShort(stringRes: Int) =
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()

fun Context.showToastAsLong(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.convertPxToDp(px: Float): Float {
    val metrics = resources.displayMetrics
    return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.convertDpToPx(dp: Float): Int {
    val metrics = resources.displayMetrics
    return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}


fun Context.getColorAttribute(colorAttributeEnum: ColorAttributeEnum): Int {
    val typedValue = TypedValue()
    when (colorAttributeEnum) {
        ColorAttributeEnum.COLOR_PRIMARY ->
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        ColorAttributeEnum.COLOR_PRIMARY_DARK ->
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true)
        ColorAttributeEnum.COLOR_ACCENT ->
            theme.resolveAttribute(R.attr.colorSecondary, typedValue, true)
    }
    return typedValue.resourceId
}

enum class ColorAttributeEnum { COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACCENT }

fun Context.getColorFromRes(colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getDrawableFromRes(drawableRes: Int): Drawable =
    ContextCompat.getDrawable(this, drawableRes)!!