package com.artf.fixer.utility.extension

import android.content.res.Resources

fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)
fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)