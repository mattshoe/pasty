package io.github.mattshoe.pasty.settings

import com.intellij.openapi.components.PersistentStateComponent
import io.github.mattshoe.pasty.service.Service


interface SettingsService: PersistentStateComponent<PastySettings>, Service {
    var proxyAddress: String?
    var pollInterval: Int
}
