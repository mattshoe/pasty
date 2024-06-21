package io.github.mattshoe.pasty.device

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    val connectedDevices: Flow<List<Device>>
    val selectedDevice: Flow<Device>

    fun setSelectedDevice(device: Device)
}