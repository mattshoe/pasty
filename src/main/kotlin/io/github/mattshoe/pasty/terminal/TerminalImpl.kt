package io.github.mattshoe.pasty.terminal

import com.intellij.util.io.awaitExit
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class TerminalImpl @Inject constructor(): Terminal {

    override suspend fun executeCommand(vararg commands: String): TerminalOutput {
        return executeCommand(commands.toList())
    }

    override suspend fun executeCommand(commands: List<String>): TerminalOutput = withContext(Dispatchers.IO) {
        val process = ProcessBuilder()
            .command(commands)
            .redirectErrorStream(false)
            .start()

        val stdout = StringBuilder()
        val stderr = StringBuilder()

        val stdoutJob = launch {
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while (reader.cooperativeReadLine().also { line = it } != null) {
                    stdout.append(line).append("\n")
                }
            }
        }

        val stderrJob = launch {
            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                var line: String?
                while (reader.cooperativeReadLine().also { line = it } != null) {
                    stderr.append(line).append("\n")
                }
            }
        }

        joinAll(
            stdoutJob,
            stderrJob
        )

        process.awaitExit()

        TerminalOutput(stdout.toString(), stderr.toString())
    }

    private suspend fun BufferedReader.cooperativeReadLine(): String? = coroutineScope {
        ensureActive()
        readLine()
    }
}