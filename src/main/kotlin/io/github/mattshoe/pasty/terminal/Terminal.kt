package io.github.mattshoe.pasty.terminal

interface Terminal {
    suspend fun executeCommand(vararg commands: String): TerminalOutput
    suspend fun executeCommand(commands: List<String>): TerminalOutput
}