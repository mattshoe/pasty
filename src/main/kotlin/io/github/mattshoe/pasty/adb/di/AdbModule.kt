package io.github.mattshoe.pasty.adb.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import io.github.mattshoe.pasty.adb.AdbRepository
import io.github.mattshoe.pasty.adb.AdbRepositoryImpl

@Module
interface AdbModule {
    @Binds
    @Reusable
    fun bindsAdbRepository(impl: AdbRepositoryImpl): AdbRepository
}