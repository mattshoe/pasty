package io.github.mattshoe.pasty.adb

import io.github.mattshoe.pasty.device.DeviceRepository
import io.github.mattshoe.pasty.terminal.Terminal
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AdbRepositoryImpl @Inject constructor(
    private val terminal: Terminal,
    private val deviceRepository: DeviceRepository
) : AdbRepository {
    override suspend fun paste(text: String) {
        val deviceId = deviceRepository.selectedDevice.firstOrNull()?.id ?: "UNKNOWN"
        terminal.executeCommand(
            "adb",
            "-s",
            deviceId,
            "shell",
            "input",
            "text",
            "\"${encodeTextForAdb(text)}\""
        )
        println("finished paste")
    }

    private fun encodeTextForAdb(text: String): String {
        val stringBuilder = StringBuilder()
        for (char in text) {
            when (char) {
                ' ' -> stringBuilder.append("\\ ")
                '\\' -> stringBuilder.append("\\\\")
                '"' -> stringBuilder.append("\\\"")
                '\'' -> stringBuilder.append("\\'")
                '`' -> stringBuilder.append("\\`")
                else -> stringBuilder.append(char)
            }
        }
        return stringBuilder.toString()
    }
}