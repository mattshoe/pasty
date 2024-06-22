package io.github.mattshoe.pasty.viewmodel

import com.intellij.openapi.application.EDT
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing

abstract class ViewModel {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Swing.immediate)

    open fun onCleared() {
        viewModelScope.cancel()
    }
}