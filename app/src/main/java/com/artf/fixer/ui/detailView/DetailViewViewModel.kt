package com.artf.fixer.ui.detailView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artf.fixer.data.model.Rate

class DetailViewViewModel : ViewModel() {

    private val _rateItem = MutableLiveData<Rate>()
    val rateItem: LiveData<Rate>
        get() = _rateItem

    fun setRateItem(listItem: Rate) {
        _rateItem.value = listItem
    }
}