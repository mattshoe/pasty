package io.github.mattshoe.pasty.log

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import io.github.mattshoe.pasty.bus.Message
import io.github.mattshoe.pasty.bus.MessageBusService
import javax.inject.Inject

class PastyLoggerImpl @Inject constructor(): PastyLogger {
    private enum class Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR
    }

    override fun info(message: CharSequence, logToUser: Boolean) {
        log(Level.INFO, message, logToUser)
    }

    override fun debug(message: Any, logToUser: Boolean) {
        log(Level.DEBUG, message, logToUser)
    }

    override fun warning(message: CharSequence, logToUser: Boolean) {
        log(Level.WARNING, message, logToUser)
    }

    override fun error(error: Throwable, logToUser: Boolean) {
        log(Level.ERROR, error.toString(), logToUser)
    }

    override fun error(message: CharSequence, error: Throwable?, logToUser: Boolean) {
        log(Level.ERROR, "$message\n\t$error", logToUser)
    }

    private fun log(level: Level, message: Any, logToUser: Boolean) {
        try {
            if (logToUser)
                logToMessageBus(level, message)

            "PASTY_LOG::${level.name}:: $message".apply {
                logToIntellij(level, this)
                logToBuildWindow(this)
            }
        } catch (e: Throwable) {
            println("Logger error: $e")
        }
    }

    private fun logToMessageBus(level: Level, message: Any) {
        service<MessageBusService>().apply {
            post(Message(message.toString()))
        }
    }

    private fun logToIntellij(level: Level, message: String) {
        when (level) {
            Level.INFO -> thisLogger().info(message)
            Level.DEBUG -> thisLogger().debug(message)
            Level.WARNING -> thisLogger().warn(message)
            Level.ERROR -> thisLogger().error(message)
        }
    }

    private fun logToBuildWindow(message: String) {
        println(message)
    }
}