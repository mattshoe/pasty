package io.github.mattshoe.pasty.device.di

import com.intellij.openapi.components.service
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.mattshoe.pasty.device.*

@Module
interface DeviceModule {

    companion object {
        @Provides
        fun providesDeviceMonitorService(): DeviceService {
            return service<DeviceService>()
        }
    }

    @Binds
    fun deviceRepository(impl: DeviceRepositoryImpl): DeviceRepository

}