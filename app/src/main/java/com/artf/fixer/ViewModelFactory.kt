package com.artf.fixer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.fixer.detailView.DetailViewViewModel
import com.artf.fixer.listView.ListViewViewModel
import com.artf.fixer.repository.Repository

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
