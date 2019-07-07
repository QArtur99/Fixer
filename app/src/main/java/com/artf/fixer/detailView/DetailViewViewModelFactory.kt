package com.artf.fixer.detailView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailViewViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewViewModel::class.java)) {
            return DetailViewViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}