package com.artf.fixer.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.artf.fixer.domain.HistoricalRateContainer
import com.artf.fixer.domain.Rate
import com.artf.fixer.network.FixerApi
import com.artf.fixer.utility.asRateList
import com.artf.fixer.utility.getDateFormat
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.Calendar
import java.util.HashMap
import java.util.concurrent.Executor

class HistoricalRateDataSource(
    private val fixerApi: FixerApi,
    private val date: String,
    private val args: HashMap<String, String>,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, Rate>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Rate>) {}

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Rate>) {
        networkState.postValue(NetworkState.LOADING)

        fixerApi.getHistoricalRates(params.key, args).enqueue(
            object : retrofit2.Callback<HistoricalRateContainer> {
                override fun onFailure(call: Call<HistoricalRateContainer>, t: Throwable) {
                    retry = { loadAfter(params, callback) }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(
                    call: Call<HistoricalRateContainer>,
                    response: Response<HistoricalRateContainer>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val items = data?.asRateList() ?: throw IOException()
                        retry = null
                        callback.onResult(items, getPreviousDay(data))
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = { loadAfter(params, callback) }
                        networkState.postValue(NetworkState.error("error code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Rate>) {

        val request = fixerApi.getHistoricalRates(date, args)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.asRateList() ?: throw IOException()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, "", getPreviousDay(data))
        } catch (ioException: IOException) {
            retry = { loadInitial(params, callback) }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    private fun getPreviousDay(data: HistoricalRateContainer?): String {
        data?.date?.let {
            val df = getDateFormat()
            df.parse(it)?.let { date ->
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                return df.format(calendar.time)
            }
        }
        return ""
    }
}