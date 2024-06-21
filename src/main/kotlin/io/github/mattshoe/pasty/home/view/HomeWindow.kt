package io.github.mattshoe.pasty.home.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.intellij.util.io.awaitExit
import io.github.mattshoe.pasty.device.Device
import io.github.mattshoe.pasty.home.viewmodel.HomeUserIntent
import io.github.mattshoe.pasty.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Preview
@Composable
fun HomeWindowPreview() {
    HomeWindow(null)
}

@Composable
fun HomeWindow(viewModel: HomeViewModel?) {
    var selectedDevice by remember { mutableStateOf("") }
    var textFieldValue by remember { mutableStateOf("") }
    var terminalOutput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    MaterialTheme(colors = darkColors()) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            DeviceDropdown(viewModel!!, selectedDevice) {
                selectedDevice = it.id
                viewModel.handleUserIntent(
                    HomeUserIntent.DeviceSelected(it)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Text to paste") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommandButtons(
                onExecute = { command ->
                    scope.launch {
                        val output = executeAdbCommand(command)
                        terminalOutput += "$command:\n$output\n"
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TerminalOutput(terminalOutput)
        }
    }
}

@Composable
fun DeviceDropdown(viewModel: HomeViewModel, selectedDevice: String, onDeviceSelected: (Device) -> Unit) {
    val devices by viewModel.connectedDevices.collectAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selectedDevice.ifEmpty { "Select a device" })
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = {
                devices.forEach { device ->
                    DropdownMenuItem(onClick = {
                        onDeviceSelected(device)
                        expanded = false
                    }) {
                        Text(device.id)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommandButtons(onExecute: (String) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(8.dp)
    ) {
        Button(onClick = { onExecute("adb devices") }) {
            Text("List Devices")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell settings put global http_proxy 127.0.0.1:8080") }) {
            Text("Set Proxy")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onExecute("adb shell input text 'Hello'") }) {
            Text("Paste Text")
        }
    }
}

@Composable
fun TerminalOutput(output: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface)
    ) {
        Text(
            text = output,
            modifier = Modifier.padding(8.dp)
        )
    }
}

suspend fun executeAdbCommand(command: String): String {
    return "derp $command"
}