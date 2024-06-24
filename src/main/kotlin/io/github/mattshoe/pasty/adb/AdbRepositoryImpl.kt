package io.github.mattshoe.pasty.adb

import io.github.mattshoe.pasty.device.DeviceRepository
import io.github.mattshoe.pasty.settings.PastySettings
import io.github.mattshoe.pasty.settings.SettingsService
import io.github.mattshoe.pasty.terminal.Terminal
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AdbRepositoryImpl @Inject constructor(
    private val terminal: Terminal,
    private val deviceRepository: DeviceRepository,
    private val settingsService: SettingsService
) : AdbRepository {
    override suspend fun paste(text: String) {
        val deviceId =
        terminal.executeCommand(
            "adb",
            "-s",
            selectedDeviceId(),
            "shell",
            "input",
            "text",
            "\"${encodeTextForAdb(text)}\""
        )
    }

    override suspend fun setProxy() {
        terminal.executeCommand(
            "adb",
            "-s",
            selectedDeviceId(),
            "shell",
            "settings",
            "put",
            "global",
            "http_proxy",
            ":0"
        )
        terminal.executeCommand(
            "adb",
            "-s",
            selectedDeviceId(),
            "shell",
            "settings",
            "put",
            "global",
            "http_proxy",
            settingsService.proxyAddress ?: ":0"
        )
    }

    private fun encodeTextForAdb(text: String): String {
        val stringBuilder = StringBuilder()
        for (char in text) {
            when (char) {
                '\\' -> stringBuilder.append("\\\\")
                '"' -> stringBuilder.append("\\\"")
                '\'' -> stringBuilder.append("\\'")
                '`' -> stringBuilder.append("\\`")
                else -> stringBuilder.append(char)
            }
        }
        return stringBuilder.toString()
    }

    private suspend fun selectedDeviceId(): String {
        return deviceRepository.selectedDevice.firstOrNull()?.id ?: "UNKNOWN"
    }
}