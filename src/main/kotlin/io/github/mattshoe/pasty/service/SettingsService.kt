package io.github.mattshoe.pasty.service

import com.intellij.openapi.components.PersistentStateComponent

interface SettingsService : PersistentStateComponent<SettingsState> {
    override fun getState(): SettingsState

    override fun loadState(state: SettingsState)
    fun updateProxyAddress(proxy: String)
}