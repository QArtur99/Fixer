package com.artf.fixer.utility

import com.artf.fixer.data.model.HistoricalRateContainer
import com.artf.fixer.data.model.Rate
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

fun HistoricalRateContainer.asRateList(): List<Rate> {
    return rates?.map {
        Rate(
            timestamp.toString().plus(it.key), base!!, date!!, it.key, it.value
        )
    } ?: throw IOException()
}

inline fun <reified T> convertToString(item: T): String {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter<T>(T::class.java)
    return jsonAdapter.toJson(item)
}

inline fun <reified T> convertFromString(item: String): T {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter<T>(T::class.java)
    return jsonAdapter.fromJson(item)!!
}