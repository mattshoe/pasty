package io.github.mattshoe.pasty.terminal

import com.intellij.util.io.awaitExit
import io.github.mattshoe.pasty.log.PastyLogger
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class TerminalImpl @Inject constructor(
    private val logger: PastyLogger
): Terminal {

    override suspend fun executeCommand(vararg commands: String, logToUser: Boolean): TerminalOutput {
        return executeCommand(commands.toList(), logToUser)
    }

    override suspend fun executeCommand(commands: List<String>, logToUser: Boolean): TerminalOutput = withContext(Dispatchers.IO) {
        val text = commands.joinToString(" ") { it }
        logger.debug(text, logToUser)
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

        TerminalOutput(stdout.toString(), stderr.toString()).apply {
            if (this.stdout.isNotBlank() && this.stdout.isNotEmpty()) {
                logger.debug(this.stdout, logToUser)
            }
            if (this.stderr.isNotBlank() && this.stderr.isNotEmpty()) {
                logger.error(this.stderr, null, logToUser)
            }
        }
    }

    private suspend fun BufferedReader.cooperativeReadLine(): String? = coroutineScope {
        ensureActive()
        readLine()
    }
}