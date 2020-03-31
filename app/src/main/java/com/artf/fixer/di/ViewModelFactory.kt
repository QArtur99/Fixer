package com.artf.fixer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.fixer.data.repository.Repository
import com.artf.fixer.ui.detailView.DetailViewViewModel
import com.artf.fixer.ui.listView.ListViewViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val tasksRepository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ListViewViewModel::class.java) -> ListViewViewModel(tasksRepository)
                isAssignableFrom(DetailViewViewModel::class.java) -> DetailViewViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
