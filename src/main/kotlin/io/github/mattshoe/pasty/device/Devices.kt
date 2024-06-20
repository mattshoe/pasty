package io.github.mattshoe.pasty.device

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class Devices {
    private val _connected = MutableSharedFlow<List<Device>>(replay = 1)
    val connected: SharedFlow<List<Device>> = _connected

    suspend fun setConnectedDevices(devices: List<Device>) {

    }
}

data class Device(
    val id: String
)