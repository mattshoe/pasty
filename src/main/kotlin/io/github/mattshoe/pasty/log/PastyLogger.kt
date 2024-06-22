package io.github.mattshoe.pasty.log

interface PastyLogger {
    fun info(message: CharSequence, logToUser: Boolean = true)
    fun debug(message: Any, logToUser: Boolean = true)
    fun warning(message: CharSequence, logToUser: Boolean = true)
    fun error(error: Throwable, logToUser: Boolean = true)
    fun error(message: CharSequence, error: Throwable? = null, logToUser: Boolean = true)
}

