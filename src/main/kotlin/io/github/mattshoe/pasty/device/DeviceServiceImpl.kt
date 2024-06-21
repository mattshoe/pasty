package io.github.mattshoe.pasty.device

import com.intellij.openapi.components.Service
import io.github.mattshoe.pasty.di.PastyApp
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.terminal.Terminal
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@Service
class DeviceServiceImpl: DeviceService {

    companion object {
        private const val INTERVAL_SECONDS = 3
    }

    init {
        PastyApp.dagger.inject(this)
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _connectedDevices = MutableStateFlow<List<Device>>(emptyList())
    private val _selectedDevice = MutableSharedFlow<Device>(replay = 1)
    private val hasStarted = AtomicBoolean(false)
    private val id = UUID.randomUUID()

    @Inject
    lateinit var logger: PastyLogger

    @Inject
    lateinit var terminal: Terminal

    override val selectedDevice = _selectedDevice.distinctUntilChanged()
    override val connectedDevices = _connectedDevices

    override fun startMonitoringConnectedDevices() {
        logger.debug("START WAS INVOKED!!!!!!")
        if (!hasStarted.getAndSet(true)) {
            coroutineScope.launch {
                while (true) {
                    logger.debug("Device Monitor: $id")
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
            _selectedDevice.emit(device)
        }
    }

    private suspend fun listDevices() {
        terminal.executeCommand("adb", "devices").let {
            logger.debug(it.stdout)
            parseDevices(it.stdout)
            if (it.stderr.isNotEmpty()) {
                logger.error(it.stderr)
            }
        }
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
                        logger.debug(device)
                        devices.add(device)
                    }
                }
            }
        }

        _connectedDevices.update { devices }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }
}