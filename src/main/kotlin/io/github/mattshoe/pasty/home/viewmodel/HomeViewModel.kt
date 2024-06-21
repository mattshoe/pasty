package io.github.mattshoe.pasty.home.viewmodel

import io.github.mattshoe.pasty.device.Device
import io.github.mattshoe.pasty.device.DeviceRepository
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val logger: PastyLogger
): ViewModel() {
    init {
        deviceRepository.connectedDevices
            .onEach {
                logger.debug(it)
            }
            .catch {
                logger.error(it)
            }
            .launchIn(viewModelScope)
    }

    val connectedDevices: Flow<List<Device>> = deviceRepository.connectedDevices

    fun handleUserIntent(userIntent: HomeUserIntent) {
        when (userIntent) {
            is HomeUserIntent.DeviceSelected -> handleDeviceSelected(userIntent.device)
        }
    }

    private fun handleDeviceSelected(device: Device) {
        deviceRepository.setSelectedDevice(device)
    }
}