package io.github.mattshoe.pasty.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service
@State(name = "PastySettings", storages = [Storage("PastySettings.xml")])
class SettingsServiceImpl: SettingsService {
    private var state = SettingsState("nada")
    override fun getState(): SettingsState {
        return state
    }

    override fun loadState(state: SettingsState) {
        this.state = state
    }

    override fun updateProxyAddress(proxy: String) {
        state.proxy = proxy
    }
}