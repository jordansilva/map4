package com.jordansilva.map4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun launch(dispatcher: CoroutineDispatcher = Dispatchers.IO, block: suspend () -> Unit): Job {
        return viewModelScope.launch(dispatcher) {
            block()
        }
    }

}