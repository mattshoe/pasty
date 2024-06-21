package io.github.mattshoe.pasty.log

interface PastyLogger {
    fun info(message: CharSequence)
    fun debug(message: Any)
    fun warning(message: CharSequence)
    fun error(error: Throwable)
    fun error(message: CharSequence, error: Throwable? = null)
}

