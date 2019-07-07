package com.artf.fixer.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.artf.fixer.domain.Rate
import com.artf.fixer.network.FixerApi
import com.artf.fixer.utility.Constants.Companion.ACCESS_KEY
import com.artf.fixer.utility.Constants.Companion.FIXER_API_TOKEN
import java.util.concurrent.Executor

class Repository(
    private val api: FixerApi? = null,
    private val diskExecutor: Executor,
    private val networkExecutor: Executor

) {

    @MainThread
    fun getRates(date: String): Listing<Rate> {
        val args = HashMap<String, String>()
        args[ACCESS_KEY] = FIXER_API_TOKEN

        val sourceFactory = HistoricalRateDataSourceFactory(api!!, date, args, networkExecutor)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(sourceFactory, config).build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.initialLoad }

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }
}