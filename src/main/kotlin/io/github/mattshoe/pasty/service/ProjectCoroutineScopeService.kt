package io.github.mattshoe.pasty.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.vladsch.flexmark.html.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

interface ProjectCoroutineScopeService: Disposable {
    val coroutineScope: CoroutineScope
}

@Service(Service.Level.PROJECT)
class ProjectCoroutineScopeServiceImpl(
    private val project: Project
): ProjectCoroutineScopeService {
    override val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun dispose() {
        coroutineScope.cancel()
    }

}