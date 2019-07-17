package com.artf.fixer.listView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.artf.fixer.domain.Rate
import com.artf.fixer.repository.Repository
import com.artf.fixer.model.ResultRate
import com.artf.fixer.utility.getDateFormat
import java.util.Date

class ListViewViewModel(
    private val repository: Repository
) :
    ViewModel() {

    private val _listItem = MutableLiveData<ResultRate>()
    val listItem: LiveData<ResultRate>
        get() = _listItem

    fun onRecyclerItemClick(listItem: ResultRate?) {
        _listItem.value = listItem
    }

    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    fun setSelectedDate(requestId: String) {
        _date.value = requestId
    }

    private val _activeRate = MutableLiveData<Rate>()
    val activeRate: LiveData<Rate>
        get() = _activeRate

    fun onBindRate(listItem: Rate) {
        _activeRate.value = listItem
    }

    init {
        val currentDateString = getDateFormat().format(Date())
        setSelectedDate(currentDateString)
    }

    // Paging
    private val repoResult = Transformations.map(date) { repository.getRates(it) }

    val ratesList = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}