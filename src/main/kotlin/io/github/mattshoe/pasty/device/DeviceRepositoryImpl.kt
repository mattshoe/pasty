package io.github.mattshoe.pasty.device

import io.github.mattshoe.pasty.di.ApplicationScope
import io.github.mattshoe.pasty.log.PastyLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val deviceService: DeviceService,
    private val logger: PastyLogger
) : DeviceRepository {

    override val connectedDevices = deviceService.connectedDevices
    override val selectedDevice: Flow<Device> = deviceService.selectedDevice

    override fun setSelectedDevice(device: Device) {
        logger.debug("Device Selected: $device")
        deviceService.setSelectedDevice(device)
    }

}