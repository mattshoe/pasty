package io.github.mattshoe.pasty.settings

import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class SettingsComponent {
    lateinit var proxyAddressField: JTextField
        private set
    lateinit var pollInterval: JSpinner

    fun panel(settings: PastySettings?) = JPanel().apply {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            anchor = GridBagConstraints.NORTHWEST
            fill = GridBagConstraints.HORIZONTAL
            insets = JBUI.insets(5)
        }
        buildProxyAddress(constraints, settings)
        buildPollInterval(constraints, settings)
        pushViewsToTop(constraints)
    }

    private fun JPanel.buildProxyAddress(
        constraints: GridBagConstraints,
        settings: PastySettings?
    ) {

        // Proxy Address row
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.anchor = GridBagConstraints.BASELINE_LEADING
        val tooltip = "The proxy address for the selected device."
        add(
            JLabel("Proxy Address:").apply {
                toolTipText = tooltip
            },
            constraints
        )

        proxyAddressField = JTextField(settings?.proxyAddress, 20).apply {
            toolTipText = tooltip
        }
        constraints.gridx = 1
        add(proxyAddressField, constraints)
    }

    private fun JPanel.buildPollInterval(
        constraints: GridBagConstraints,
        settings: PastySettings?
    ) {
        constraints.gridy = 1
        constraints.gridx = 0
        constraints.anchor = GridBagConstraints.BASELINE_LEADING
        val tooltip = "The interval at which Pasty will check to see what devices are connected."
        add(
            JLabel("Device Poll Interval:").apply {
                toolTipText = tooltip
            },
            constraints
        )

        pollInterval = JSpinner(SpinnerNumberModel(settings?.pollInterval ?: 3, 1, Int.MAX_VALUE, 1)).apply {
            toolTipText = tooltip
        }
        constraints.gridx = 1
        add(pollInterval, constraints)
    }

    private fun JPanel.pushViewsToTop(constraints: GridBagConstraints) {
        // Ensure everything aligns to the top-left
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.gridwidth = GridBagConstraints.REMAINDER
        add(Box.createGlue(), constraints)
    }


}