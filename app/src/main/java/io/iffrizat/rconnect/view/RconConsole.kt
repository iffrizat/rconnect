package io.iffrizat.rconnect.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RconConsole(consoleTextState: MutableState<String>, inputTextState: MutableState<String>, commandSendCallback: (KeyboardActionScope) -> Unit, backPressCallback: () -> Unit) {
    val scrollState = rememberScrollState()

    LaunchedEffect(consoleTextState.value) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column {
        TextField(consoleTextState.value, {}, modifier = Modifier.fillMaxWidth().weight(0.6f).verticalScroll(
            scrollState), readOnly = true)
        TextField(inputTextState.value, {v -> inputTextState.value = v}, modifier = Modifier.fillMaxWidth().padding(8.dp), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send), singleLine = true, keyboardActions = KeyboardActions(onSend = commandSendCallback))
        Button(onClick = backPressCallback, modifier = Modifier.fillMaxWidth().padding(8.dp)) { Text("Back") }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
@Preview
fun PreviewRconConsole() {
    val consoleTextState = remember { mutableStateOf("") }
    val inputTextState = remember { mutableStateOf("") }
    RconConsole(consoleTextState, inputTextState, {}, {})
}