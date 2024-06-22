package io.github.mattshoe.pasty.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class AbstractService: Service {
    protected open val dispatcher = Dispatchers.Default
    protected val coroutineScope by lazy { CoroutineScope(SupervisorJob() + dispatcher) }

    override fun dispose() {
        coroutineScope.cancel()
    }
}