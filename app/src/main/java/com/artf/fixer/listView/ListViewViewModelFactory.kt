package com.artf.fixer.listView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.fixer.repository.Repository

class ListViewViewModelFactory(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewViewModel::class.java)) {
            return ListViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}