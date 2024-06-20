package io.github.mattshoe.pasty.log.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.log.PastyLoggerImpl

@Module
interface LoggerModule {
    @Binds
    @Reusable
    fun bindsLogger(impl: PastyLoggerImpl): PastyLogger
}