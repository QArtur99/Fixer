package com.artf.fixer.utility.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.artf.fixer.di.ViewModelFactory
import com.artf.fixer.di.ServiceLocator

fun AppCompatActivity.getVmFactory(): ViewModelFactory {
    val repository = ServiceLocator.instance(this.applicationContext).getRepository()
    return ViewModelFactory(repository)
}

inline fun <reified T : ViewModel> AppCompatActivity.getVm(): T {
    return ViewModelProviders.of(this, getVmFactory()).get(T::class.java)
}