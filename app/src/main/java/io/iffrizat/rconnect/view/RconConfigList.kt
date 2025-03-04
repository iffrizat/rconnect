package io.iffrizat.rconnect.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.iffrizat.rconnect.R
import io.iffrizat.rconnect.model.RconConfig

@Composable
fun RconConfigList(configs: List<RconConfig>, callbackEditFactory: (Int) -> () -> Unit, callbackConnectFactory: (Int) -> () -> Unit) {
    Column {
        if (configs.isEmpty()) {
            Text(modifier = Modifier.fillMaxWidth().padding(8.dp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 2, text = stringResource(R.string.rcon_config_list_no_configs), textAlign = TextAlign.Center)
        } else {
            for ((index, config) in configs.withIndex()) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp).clip(RoundedCornerShape(8.dp)).background(
                    MaterialTheme.colorScheme.onBackground
                ), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(if (config.name.isEmpty()) stringResource(R.string.rcon_config_list_unnamed_connection) else config.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(config.connectionString, color = MaterialTheme.colorScheme.secondary)
                    }

                    TextButton(onClick = callbackEditFactory(index), modifier = Modifier.padding(4.dp)) {
                        Icon(painter = painterResource(R.drawable.profile_edit), null, modifier = Modifier.size(48.dp))
                    }

                    TextButton(onClick = callbackConnectFactory(index), modifier = Modifier.padding(4.dp)) {
                        Icon(painter = painterResource(R.drawable.profile_connect), null, modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewConfigList() {
    val mockData = listOf(
        RconConfig("", "127.0.0.1:2525", ""),
        RconConfig("My Other Server", "123.213.88.11:25575", ""),
        RconConfig("My Very Longly Named Server", "this.is.an.ip:3333", ""),
    )

    RconConfigList(mockData, {{}}, {{}})
}

@Preview
@Composable
fun PreviewEmptyConfigList() {
    RconConfigList(listOf(), {{}}, {{}})
}