package com.artf.fixer.utility

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun getDateFormat(): SimpleDateFormat {
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df
}