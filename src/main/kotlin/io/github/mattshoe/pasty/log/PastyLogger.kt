package io.github.mattshoe.pasty.log

interface PastyLogger {
    fun info(message: CharSequence)
    fun debug(message: CharSequence)
    fun warning(message: CharSequence)
    fun error(message: CharSequence, error: Throwable? = null)
}

