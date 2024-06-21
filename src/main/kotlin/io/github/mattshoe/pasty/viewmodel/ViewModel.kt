package io.github.mattshoe.pasty.viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing

abstract class ViewModel {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Swing.immediate + CoroutineExceptionHandler { _, error ->
        println(error)
    })

    open fun onCleared() {
        viewModelScope.cancel()
    }
}