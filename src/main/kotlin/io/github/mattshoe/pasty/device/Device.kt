package io.github.mattshoe.pasty.device

data class Device(
    val id: String
) {
    companion object {
        val NONE = Device("No device selected!")
    }
}