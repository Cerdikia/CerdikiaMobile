package com.fhanafi.cerdikia.helper

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.util.TypedValue

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

fun View.setMarginTop(marginTopDp: Int) {
    val layoutParams = this.layoutParams as? MarginLayoutParams
    layoutParams?.let {
        it.topMargin = marginTopDp.dp
        this.layoutParams = it
    }
}