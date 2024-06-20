package io.github.mattshoe.pasty.service

import com.intellij.openapi.components.Service
import com.vladsch.flexmark.html.Disposable
import kotlinx.coroutines.*

interface ApplicationCoroutineScopeService: Disposable {
    val coroutineScope: CoroutineScope
}

@Service
class ApplicationCoroutineScopeServiceImpl: ApplicationCoroutineScopeService {
    override val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun dispose() {
        coroutineScope.cancel()
    }
}