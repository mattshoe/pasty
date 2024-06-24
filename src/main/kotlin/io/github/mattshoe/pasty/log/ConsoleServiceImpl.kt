package io.github.mattshoe.pasty.log

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.string.println
import io.github.mattshoe.pasty.bus.MessageBusService
import io.github.mattshoe.pasty.di.PastyApp
import io.github.mattshoe.pasty.service.AbstractService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@Service(Service.Level.PROJECT)
class ConsoleServiceImpl(
    private val project: Project
): AbstractService(), ConsoleService {

    init {
        PastyApp.dagger.inject(this)
    }

    @Inject
    lateinit var logger: PastyLogger

    private val console = ConsoleViewImpl(project, true)
    private val descriptor = RunContentDescriptor(console, null, console.component, "Pasty Console")

    init {
        RunContentManager.getInstance(project)
            .showRunContent(
                DefaultRunExecutor.getRunExecutorInstance(),
                descriptor
            )

        service<MessageBusService>().stream
            .onEach {
                console.print("${it.text}\n", ConsoleViewContentType.NORMAL_OUTPUT)
            }
            .catch {
                println(it)
            }
            .launchIn(coroutineScope)
    }
}