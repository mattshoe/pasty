package io.github.mattshoe.pasty.bus

import com.intellij.openapi.components.Service
import io.github.mattshoe.pasty.di.PastyApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Service
class MessageBusServiceImpl: MessageBusService {

    init {
        PastyApp.dagger.inject(this)
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _history = MutableStateFlow(MessageHistory())
    private val _stream = MutableSharedFlow<Message>(replay = 0)

    override val history: Flow<MessageHistory> = _history
    override val stream: Flow<Message> = _stream

    override fun post(message: Message) {
        println("pre-post!")
        coroutineScope.launch(CoroutineExceptionHandler { _, e ->
            println(e)
        }) {
            println("Posting message!")
            _stream.emit(message)
        }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }

}