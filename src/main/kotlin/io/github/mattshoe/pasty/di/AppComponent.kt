package io.github.mattshoe.pasty.di

import dagger.BindsInstance
import dagger.Component
import io.github.mattshoe.pasty.device.DeviceServiceImpl
import io.github.mattshoe.pasty.device.di.DeviceModule
import io.github.mattshoe.pasty.home.view.HomeWindowFactory
import io.github.mattshoe.pasty.log.di.LoggerModule
import io.github.mattshoe.pasty.terminal.di.TerminalModule
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoggerModule::class,
        DeviceModule::class,
        TerminalModule::class
    ]
)
interface AppComponent {
    fun inject(window: HomeWindowFactory)
    fun inject(service: DeviceServiceImpl)

    @Component.Builder
    interface Builder {
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