package io.github.mattshoe.pasty.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class SettingsComponent {
    lateinit var proxyAddressField: JTextField
        private set

    fun panel(settings: PastySettings?) = JPanel().apply {
        layout = GridBagLayout()
        buildProxyAddress(this, settings)
    }

    private fun buildProxyAddress(panel: JPanel, settings: PastySettings?) {
        val constraints = GridBagConstraints().apply {
            anchor = GridBagConstraints.NORTHWEST
            fill = GridBagConstraints.HORIZONTAL
            insets = JBUI.insets(5)
        }

        // Proxy Address row
        constraints.gridx = 0
        constraints.gridy = 0
        panel.add(JLabel("Proxy Address:"), constraints)

        proxyAddressField = JTextField(settings?.proxyAddress,20)
        constraints.gridx = 1
        panel.add(proxyAddressField, constraints)

        // Ensure everything aligns to the top-left
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.gridwidth = GridBagConstraints.REMAINDER
        panel.add(Box.createGlue(), constraints)
    }


}

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
}