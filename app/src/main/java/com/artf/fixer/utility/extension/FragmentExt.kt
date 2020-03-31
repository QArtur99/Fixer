package com.artf.fixer.utility.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.artf.fixer.di.ViewModelFactory
import com.artf.fixer.di.ServiceLocator

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = ServiceLocator.instance(requireContext().applicationContext).getRepository()
    return ViewModelFactory(repository)
}

inline fun <reified T : ViewModel> Fragment.getVm(): T {
    return ViewModelProviders.of(this.activity!!, getVmFactory()).get(T::class.java)
}