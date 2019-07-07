package com.artf.fixer.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.artf.fixer.domain.Rate
import com.artf.fixer.network.FixerApi
import java.util.HashMap
import java.util.concurrent.Executor

class HistoricalRateDataSourceFactory(
    private val fixerApi: FixerApi,
    private val date: String,
    private val args: HashMap<String, String>,
    private val retryExecutor: Executor
) : DataSource.Factory<String, Rate>() {

    val sourceLiveData = MutableLiveData<HistoricalRateDataSource>()

    override fun create(): DataSource<String, Rate> {
        val source = HistoricalRateDataSource(fixerApi, date, args, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}