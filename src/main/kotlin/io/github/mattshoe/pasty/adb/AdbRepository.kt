package io.github.mattshoe.pasty.adb

interface AdbRepository {
    suspend fun paste(text: String)
    suspend fun setProxy()
}