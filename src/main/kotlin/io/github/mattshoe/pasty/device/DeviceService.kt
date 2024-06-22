package io.github.mattshoe.pasty.device

import com.vladsch.flexmark.html.Disposable
import io.github.mattshoe.pasty.service.Service
import kotlinx.coroutines.flow.Flow

interface DeviceService: Service {
    val connectedDevices: Flow<List<Device>>
    val selectedDevice: Flow<Device>

    fun startMonitoringConnectedDevices()
    fun setSelectedDevice(device: Device)
}

