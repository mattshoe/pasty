package io.github.mattshoe.pasty.device

import com.intellij.openapi.components.Service
import io.github.mattshoe.pasty.di.PastyApp
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.service.AbstractService
import io.github.mattshoe.pasty.terminal.Terminal
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.math.log
import kotlin.time.Duration.Companion.seconds

@Service
class DeviceServiceImpl: AbstractService(), DeviceService {

    companion object {
        private const val INTERVAL_SECONDS = 3
        private val CONNECTED_DEVICES_EMPTY_STATE = listOf(Device.NONE)
    }

    private var previousDevices = CONNECTED_DEVICES_EMPTY_STATE
    private val _connectedDevices = MutableStateFlow(CONNECTED_DEVICES_EMPTY_STATE)
    private val _selectedDevice = MutableStateFlow(Device.NONE)
    private val hasStarted = AtomicBoolean(false)
    private val id = UUID.randomUUID()

    init {
        PastyApp.dagger.inject(this)

        _selectedDevice
            .onEach {
                if (it == Device.NONE)
                    logger.warning("No device selected!")
                else
                    logger.info("New device selected: ${it.id}")
            }.launchIn(coroutineScope)

        _connectedDevices
            .onEach {
                if (it == CONNECTED_DEVICES_EMPTY_STATE)
                    logger.warning("No devices attached!")
                else
                    deviceDiff(it).apply {
                        if (added.isNotEmpty()) {
                            added.forEach {
                                logger.info("New device attached: ${it.id}")
                            }
                        }
                        if (removed.isNotEmpty()) {
                            removed.forEach {
                                logger.info("Device disconnected: ${it.id}")
                            }
                        }
                    }
                previousDevices = it
            }
    }

    @Inject
    lateinit var logger: PastyLogger

    @Inject
    lateinit var terminal: Terminal

    override val selectedDevice = _selectedDevice
    override val connectedDevices = _connectedDevices

    override fun startMonitoringConnectedDevices() {
        if (!hasStarted.getAndSet(true)) {
            coroutineScope.launch {
                while (true) {
                    try {
                        listDevices()
                    } catch (e: Throwable) {
                        logger.error(e)
                    }

                    delay(INTERVAL_SECONDS.seconds)
                }
            }
        }
    }

    override fun setSelectedDevice(device: Device) {
        coroutineScope.launch {
            if (_connectedDevices.value.contains(device)) {
                _selectedDevice.emit(device)
            }
        }
    }

    private suspend fun listDevices() {
        parseDevices(
            terminal.executeCommand(
                "adb", "devices",
                logToUser = false
            ).stdout
        )
    }

    private fun parseDevices(stdout: String) {
        val devices = mutableListOf<Device>()
        if (stdout.trim().isNotEmpty()) {
            val deviceOutputList = stdout
                .split("\n")
                .drop(1)
                .filter { it.isNotEmpty() && it.isNotBlank() }
            deviceOutputList.let { lines ->
                lines.forEach { deviceOutput ->
                    deviceOutput.split("\t").let {
                        val device = Device(it[0])
                        devices.add(device)
                    }
                }
            }
        }

        updateDevicesState(devices)
    }

    private fun updateDevicesState(devices: List<Device>) {
        if (devices.isEmpty()) {
            setSelectedDevice(Device.NONE)
            _connectedDevices.update { CONNECTED_DEVICES_EMPTY_STATE }
        }
        else {
            _connectedDevices.update { devices }
            if (devices.size == 1 || !devices.contains(_selectedDevice.value)) {
                setSelectedDevice(devices.first())
            }
        }
    }

    private fun deviceDiff(newDevices: List<Device>): DeviceDiff {
        return DeviceDiff(
            newDevices.filter { it !in previousDevices },
            previousDevices.filter { it !in newDevices }
        )
    }

    private data class DeviceDiff(
        val added: List<Device>,
        val removed: List<Device>
    )
}