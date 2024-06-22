package io.github.mattshoe.pasty.terminal

interface Terminal {
    suspend fun executeCommand(vararg commands: String, logToUser: Boolean = true): TerminalOutput
    suspend fun executeCommand(commands: List<String>, logToUser: Boolean = true): TerminalOutput
}