package io.github.mattshoe.pasty.home.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intellij.openapi.components.service
import com.intellij.util.io.awaitExit
import io.github.mattshoe.pasty.bus.Message
import io.github.mattshoe.pasty.bus.MessageBusService
import io.github.mattshoe.pasty.bus.MessageHistory
import io.github.mattshoe.pasty.device.Device
import io.github.mattshoe.pasty.home.viewmodel.HomeUserIntent
import io.github.mattshoe.pasty.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@Preview
//@Composable
//fun HomeWindowPreview() {
//    HomeWindow(null)
//}

@Composable
fun HomeWindow(viewModel: HomeViewModel) {
    var textFieldValue by remember { mutableStateOf("") }
    var terminalOutput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    MaterialTheme(colors = darkColors()) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            DeviceDropdown(viewModel, viewModel.selectedDevice) {
                viewModel.handleUserIntent(
                    HomeUserIntent.DeviceSelected(it)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("Text to paste") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Button(
                    onClick = {
                        viewModel.handleUserIntent(HomeUserIntent.Paste(textFieldValue))
                    },
                    content = {
                        Text("Paste")
                    }
                )
            }
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
            TerminalOutput()
        }
    }
}

@Composable
fun DeviceDropdown(viewModel: HomeViewModel, selectedDevice: Flow<Device>, onDeviceSelected: (Device) -> Unit) {
    val selectedDeviceState by selectedDevice.collectAsState(Device.NONE)
    val devices by viewModel.connectedDevices.collectAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selectedDeviceState.id)
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
fun TerminalOutput() {
//    var text = remember { mutableStateOf("") }
//    var textState by remember { mutableStateOf(TextFieldValue("")) }
//    val verticalScrollState = rememberScrollState()
//    val output by service<MessageBusService>().stream.collectAsState(Message("derp"))
//    // Define the line height and calculate the box height
    val lineHeight = 10.dp
    val boxHeight = lineHeight * 30

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
//            BasicTextField(
//                value = text.value.plus("\n$output"),
//                onValueChange = { textState = TextFieldValue(it) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Black),
//                textStyle = androidx.compose.ui.text.TextStyle(
//                    color = Color.Green,
//                    fontSize = 10.sp,
//                    fontFamily = FontFamily.Monospace
//                )
//            )
        }
    }
}

suspend fun executeAdbCommand(command: String): String {
    return "derp $command"
}