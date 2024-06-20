package io.github.mattshoe.pasty.device

import com.intellij.openapi.components.Service
import com.vladsch.flexmark.html.Disposable
import io.github.mattshoe.pasty.log.PastyLogger
import io.github.mattshoe.pasty.log.PastyLoggerImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.repackaged.net.bytebuddy.implementation.bind.annotation.Super
import java.util.UUID
import javax.inject.Inject

interface DeviceMonitorService: Disposable {
    fun start()
}

@Service
class DeviceMonitorServiceImpl: DeviceMonitorService {
    @Inject
    lateinit var logger: PastyLogger

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val id = UUID.randomUUID()

    override fun start() {
        coroutineScope.launch {
            while (true) {
                logger.debug("Device Monitor: $id")
                delay(1000)
            }
        }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }
}