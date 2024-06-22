package io.github.mattshoe.pasty.di

import dagger.BindsInstance
import dagger.Component
import io.github.mattshoe.pasty.adb.di.AdbModule
import io.github.mattshoe.pasty.bus.MessageBusServiceImpl
import io.github.mattshoe.pasty.device.DeviceServiceImpl
import io.github.mattshoe.pasty.device.di.DeviceModule
import io.github.mattshoe.pasty.home.view.HomeWindowFactory
import io.github.mattshoe.pasty.log.ConsoleServiceImpl
import io.github.mattshoe.pasty.log.di.LoggerModule
import io.github.mattshoe.pasty.settings.SettingsService
import io.github.mattshoe.pasty.terminal.di.TerminalModule
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoggerModule::class,
        DeviceModule::class,
        TerminalModule::class,
        AdbModule::class
    ]
)
interface AppComponent {
    fun inject(window: HomeWindowFactory)
    fun inject(service: DeviceServiceImpl)
    fun inject(service: MessageBusServiceImpl)
    fun inject(service: ConsoleServiceImpl)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun settings(@ApplicationScope settings: SettingsService): Builder

        @BindsInstance
        fun applicationScope(@ApplicationScope coroutineScope: CoroutineScope): Builder

        @BindsInstance
        fun projectScope(@ProjectScope coroutineScope: CoroutineScope): Builder

        fun build(): AppComponent
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProjectScope