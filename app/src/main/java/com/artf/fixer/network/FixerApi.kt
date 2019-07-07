package com.artf.fixer.network

import com.artf.fixer.domain.HistoricalRateContainer
import com.artf.fixer.network.RetrofitModule.BASE_URL
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface FixerApi {

    @GET("$BASE_URL{date}")
    fun getHistoricalRates(
        @Path(value = "date", encoded = true) sortBy: String,
        @QueryMap args: Map<String, String>
    ): Call<HistoricalRateContainer>
}