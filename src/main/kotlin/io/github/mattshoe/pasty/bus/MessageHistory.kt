package io.github.mattshoe.pasty.bus

import kotlin.random.Random

class MessageHistory {
    private val _messages = mutableListOf<Message>()
    val messages: List<Message> = _messages

    fun append(message: Message) {
        _messages.add(message)
    }

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        // meh, good enough
        return Random.nextInt()
    }
}