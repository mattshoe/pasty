package io.github.mattshoe.pasty.home.viewmodel

import io.github.mattshoe.pasty.adb.AdbRepository
import io.github.mattshoe.pasty.device.Device
import io.github.mattshoe.pasty.device.DeviceRepository
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.repackaged.net.bytebuddy.implementation.bytecode.Throw
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val adbRepository: AdbRepository,
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
    val selectedDevice: Flow<Device> = deviceRepository.selectedDevice

    fun handleUserIntent(userIntent: HomeUserIntent) {
        when (userIntent) {
            is HomeUserIntent.DeviceSelected -> handleDeviceSelected(userIntent.device)
            is HomeUserIntent.Paste -> handlePasteText(userIntent.text)
            is HomeUserIntent.SetProxy -> handleProxyReset()
        }
    }

    private fun handleDeviceSelected(device: Device) {
        deviceRepository.setSelectedDevice(device)
    }

    private fun handlePasteText(text: String) {
        viewModelScope.launch {
            try {
                adbRepository.paste(text)
            } catch (e: Throwable) {
                logger.error(e)
            }
        }
    }

    private fun handleProxyReset() {
        viewModelScope.launch {
            try {
                adbRepository.setProxy()
            } catch (e: Throwable) {
                logger.error(e)
            }
        }
    }
}