package io.github.mattshoe.pasty.di

import dagger.BindsInstance
import dagger.Component
import io.github.mattshoe.pasty.device.DeviceMonitorServiceImpl
import io.github.mattshoe.pasty.home.view.HomeWindowFactory
import io.github.mattshoe.pasty.log.di.LoggerModule
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoggerModule::class
    ]
)
interface AppComponent {
    fun inject(window: HomeWindowFactory)
    fun inject(service: DeviceMonitorServiceImpl)

    @Component.Builder
    interface Builder {
        @BindsInstance
        @ApplicationScope
        fun applicationScope(coroutineScope: CoroutineScope): Builder

        @BindsInstance
        @ProjectScope
        fun projectScope(coroutineScope: CoroutineScope): Builder

        fun build(): AppComponent
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProjectScope