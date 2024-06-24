package io.github.mattshoe.pasty.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PastySettingsConfigurable : Configurable {

    private lateinit var settingsPanel: SettingsComponent
    private val settingsService: SettingsService
        get() = service()

    override fun isModified(): Boolean {
        return settingsPanel.proxyAddressField.text != settingsService.proxyAddress
    }

    override fun getDisplayName(): String {
        return "Pasty Settings"
    }

    override fun apply() {
        settingsService.proxyAddress = settingsPanel.proxyAddressField.text
        settingsService.pollInterval = (settingsPanel.pollInterval.value as? Int) ?: 3
    }

    override fun createComponent(): JComponent {
        if (!::settingsPanel.isInitialized) {
            settingsPanel = SettingsComponent()
        }

        return settingsPanel.panel(settingsService.state)
    }

    override fun reset() {
        settingsPanel.proxyAddressField.text = settingsService.proxyAddress
    }

    private fun isRestartRequired(): Boolean {
        return settingsService.pollInterval != settingsPanel.pollInterval.value
    }
}