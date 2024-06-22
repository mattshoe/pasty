package io.github.mattshoe.pasty.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent

@Service
class SettingsServiceImpl: SimplePersistentStateComponent<PastySettings>(PastySettings()), SettingsService {
    override var proxyAddress: String?
        get() = state.proxyAddress
        set(value) {
            state.proxyAddress = value
        }

    override var pollInterval: Int
        get() = state.pollInterval
        set(value) {
            state.pollInterval = value
        }


    override fun dispose() {
        //TODO
    }
}
