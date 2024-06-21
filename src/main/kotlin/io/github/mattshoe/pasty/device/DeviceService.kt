package io.github.mattshoe.pasty.device

import com.vladsch.flexmark.html.Disposable
import kotlinx.coroutines.flow.Flow

interface DeviceService: Disposable {
    val connectedDevices: Flow<List<Device>>
    val selectedDevice: Flow<Device>

    fun startMonitoringConnectedDevices()
    fun setSelectedDevice(device: Device)
}

