package com.artf.fixer.utility

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class CustomScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {

    init {
        this.setOnTouchListener { _, _ -> true }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean {
        return true
    }
}