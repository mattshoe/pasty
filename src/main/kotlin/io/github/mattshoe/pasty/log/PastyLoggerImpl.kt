package io.github.mattshoe.pasty.log

import com.intellij.openapi.diagnostic.thisLogger
import javax.inject.Inject

class PastyLoggerImpl @Inject constructor(): PastyLogger {
    private enum class Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR
    }

    override fun info(message: CharSequence) {
        log(Level.INFO, message)
    }

    override fun debug(message: CharSequence) {
        log(Level.DEBUG, message)
    }

    override fun warning(message: CharSequence) {
        log(Level.WARNING, message)
    }

    override fun error(message: CharSequence, error: Throwable?) {
        log(Level.ERROR, "$message\n\t$error")
    }

    private fun log(level: Level, message: CharSequence) {
        with ("PASTY_LOG::${level.name}:: $message") {
            println(this)
            when (level) {
                Level.INFO -> thisLogger().info(this)
                Level.DEBUG -> thisLogger().debug(this)
                Level.WARNING -> thisLogger().warn(this)
                Level.ERROR -> thisLogger().error(this)
            }
        }
    }
}