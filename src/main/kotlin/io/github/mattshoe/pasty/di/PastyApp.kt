package io.github.mattshoe.pasty.di

import com.intellij.openapi.components.service
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.wm.ToolWindow
import io.github.mattshoe.pasty.device.DeviceMonitorService
import io.github.mattshoe.pasty.device.DeviceMonitorServiceImpl
import io.github.mattshoe.pasty.service.ApplicationCoroutineScopeService
import io.github.mattshoe.pasty.service.ProjectCoroutineScopeService

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

        if (!isDeviceMonitorRunning()) {
            startDeviceMonitor()
        }
    }


    private fun isDeviceMonitorRunning(): Boolean {
        return serviceIfCreated<DeviceMonitorService>() != null
    }

    private fun startDeviceMonitor() {
        service<DeviceMonitorService>().apply {
            dagger.inject(this as DeviceMonitorServiceImpl)
            start()
        }
    }

}