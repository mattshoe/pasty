package io.github.mattshoe.pasty.di

import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import io.github.mattshoe.pasty.device.DeviceService
import io.github.mattshoe.pasty.service.ApplicationCoroutineScopeService
import io.github.mattshoe.pasty.service.ProjectCoroutineScopeService
import io.github.mattshoe.pasty.service.SettingsService

object PastyApp {
    lateinit var dagger: AppComponent

    fun initializePlugin(toolWindow: ToolWindow) {
        dagger = DaggerAppComponent.builder()
            .applicationScope(
                service<ApplicationCoroutineScopeService>().coroutineScope
            )
            .projectScope(
                toolWindow.project.service<ProjectCoroutineScopeService>().coroutineScope
            )
            .build()

        startDeviceMonitor()
        createSettings()
    }

    private fun startDeviceMonitor() {
        service<DeviceService>().apply {
            startMonitoringConnectedDevices()
        }
    }

    private fun createSettings() {
        service<SettingsService>()
    }

}