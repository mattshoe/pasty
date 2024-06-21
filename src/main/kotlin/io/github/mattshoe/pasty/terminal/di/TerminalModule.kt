package io.github.mattshoe.pasty.terminal.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import io.github.mattshoe.pasty.terminal.Terminal
import io.github.mattshoe.pasty.terminal.TerminalImpl

@Module
interface TerminalModule {

    @Binds
    @Reusable
    fun bindsTerminal(impl: TerminalImpl): Terminal
}