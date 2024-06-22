package io.github.mattshoe.pasty.di

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import io.github.mattshoe.pasty.bus.MessageBusService
import io.github.mattshoe.pasty.device.DeviceService
import io.github.mattshoe.pasty.log.ConsoleService
import io.github.mattshoe.pasty.service.ApplicationCoroutineScopeService
import io.github.mattshoe.pasty.service.ProjectCoroutineScopeService
import io.github.mattshoe.pasty.settings.SettingsService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object PastyApp {
    lateinit var dagger: AppComponent
    lateinit var consoleService: ConsoleService
    lateinit var messageBusService: MessageBusService

    fun initializePlugin(toolWindow: ToolWindow) {
        dagger = DaggerAppComponent.builder()
            .settings(
                service<SettingsService>()
            )
            .applicationScope(
                service<ApplicationCoroutineScopeService>().coroutineScope
            )
            .projectScope(
                toolWindow.project.service<ProjectCoroutineScopeService>().coroutineScope
            )
            .build()

        startLoggingService(toolWindow.project)
        startDeviceMonitor()
        createMessageBusService()
    }

    private fun startLoggingService(project: Project) {
        consoleService = project.service<ConsoleService>()
    }

    private fun startDeviceMonitor() {
        service<DeviceService>().apply {
            startMonitoringConnectedDevices()
        }
    }

    private fun createMessageBusService() {
        messageBusService = service()
        messageBusService.history
            .onEach {
                println("Historical message! $it")
            }.launchIn(service<ApplicationCoroutineScopeService>().coroutineScope)
        messageBusService.stream
            .onEach {
                println("Streamed message! $it")
            }.launchIn(service<ApplicationCoroutineScopeService>().coroutineScope)
    }

}