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
import androidx.compose.ui.Alignment
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
            TextInputWithPasteButton(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            CommandButtons(viewModel)
            Spacer(modifier = Modifier.height(10.dp))
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

@Composable
@Preview
fun TextInputWithPasteButton(viewModel: HomeViewModel) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                placeholder = {
                    Text(
                        text = "Text to paste",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            color = Color.Gray
                        )
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .verticalScroll(rememberScrollState()),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.Monospace
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    println("Pasting text: ${textState.text}")
                    viewModel.handleUserIntent(
                        HomeUserIntent.Paste(textState.text)
                    )
                },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Text("Paste")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommandButtons(viewModel: HomeViewModel) {
    FlowRow(
        modifier = Modifier.padding(8.dp)
    ) {
        Button(
            onClick = {
                viewModel.handleUserIntent(
                    HomeUserIntent.SetProxy
                )
            }
        ) {
            Text("Set Proxy")
        }
    }
}

suspend fun executeAdbCommand(command: String): String {
    return "derp $command"
}