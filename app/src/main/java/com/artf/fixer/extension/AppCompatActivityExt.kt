package com.artf.fixer.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.artf.fixer.ViewModelFactory
import com.artf.fixer.utility.ServiceLocator

fun AppCompatActivity.getVmFactory(): ViewModelFactory {
    val repository = ServiceLocator.instance(this.applicationContext).getRepository()
    return ViewModelFactory(repository)
}

inline fun <reified T : ViewModel> AppCompatActivity.getVm(): T {
    return ViewModelProviders.of(this, getVmFactory()).get(T::class.java)
}