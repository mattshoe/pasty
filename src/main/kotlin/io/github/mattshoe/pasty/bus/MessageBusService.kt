package io.github.mattshoe.pasty.bus

import com.vladsch.flexmark.html.Disposable
import io.github.mattshoe.pasty.service.Service
import kotlinx.coroutines.flow.Flow

interface MessageBusService: Service {
    val history: Flow<MessageHistory>
    val stream: Flow<Message>

    fun post(message: Message)
}

