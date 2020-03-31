package com.artf.fixer.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HistoricalRateContainer(
    val date: String? = null,
    val success: Boolean? = null,
    @Json(name = "rates") val rates: Map<String, String>? = null,
    val historical: Boolean? = null,
    val timestamp: Int? = null,
    val base: String? = null
)

data class Rate(
    val id: String,
    val base: String,
    val date: String,
    val symbol: String,
    val rate: String
)
