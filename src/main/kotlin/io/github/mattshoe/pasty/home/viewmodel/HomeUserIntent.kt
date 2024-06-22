package io.github.mattshoe.pasty.home.viewmodel

import io.github.mattshoe.pasty.device.Device

sealed interface HomeUserIntent {
    data class DeviceSelected(val device: Device): HomeUserIntent
    data class Paste(val text: String): HomeUserIntent
}