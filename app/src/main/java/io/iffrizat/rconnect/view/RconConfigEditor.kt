package io.iffrizat.rconnect.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.iffrizat.rconnect.R

@Composable
fun RconConfigEditor(configNameState: MutableState<String>, connectionStringState: MutableState<String>, connectionPasswordState: MutableState<String>, configId: Int, savePressCallback: () -> Unit, deletePressCallback: () -> Unit) {
    Column {
        Spacer(modifier = Modifier.weight(0.1f))
        Text(modifier = Modifier.fillMaxWidth().padding(8.dp), fontSize = TextUnit(24f, TextUnitType.Sp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, text = stringResource(R.string.rcon_config_editor_edit), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(0.1f))
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            OutlinedTextField(configNameState.value,
                onValueChange = { v ->
                    configNameState.value = v
            },
                label = {Text(stringResource(R.string.rcon_config_editor_config_name))}, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(connectionStringState.value,
                onValueChange = { v ->
                    connectionStringState.value = v
            }, placeholder = {Text("192.168.1.1:25575")}, label = {Text(stringResource(R.string.rcon_config_editor_connection_string))}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(connectionPasswordState.value,
                onValueChange = { v ->
                    connectionPasswordState.value = v
                }, label = {Text(stringResource(R.string.rcon_config_editor_connection_password))}, modifier = Modifier.fillMaxWidth())
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = savePressCallback, modifier = Modifier.padding(8.dp)) { Text(
                stringResource(R.string.rcon_config_editor_save)) }
            if (configId >= 0) {
                Button(onClick = deletePressCallback, modifier = Modifier.padding(8.dp)) { Text(
                    stringResource(R.string.rcon_config_editor_delete)) }
            }
        }

        Spacer(modifier = Modifier.weight(0.8f))
    }
}

@Composable
@Preview
fun PreviewRconConfigEditorEmpty() {
    val configNameState = remember {mutableStateOf("")}
    val connectionStringState = remember {mutableStateOf("")}
    val connectionPasswordState = remember {mutableStateOf("")}

    RconConfigEditor(configNameState, connectionStringState, connectionPasswordState, -1, {}, {})
}