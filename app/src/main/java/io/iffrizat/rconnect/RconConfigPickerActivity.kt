package io.iffrizat.rconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.iffrizat.rconnect.ui.theme.RCONnectTheme
import io.iffrizat.rconnect.view.RconConfigList
import io.iffrizat.rconnect.viewmodel.RconConfigPickerViewModel
import java.io.File

class RconConfigPickerActivity : ComponentActivity() {
    private val vm by viewModels<RconConfigPickerViewModel>()

    companion object {
        const val RCON_CONFIG_FILENAME = "rcon_configs.bin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configFile = File(filesDir, RCON_CONFIG_FILENAME)
        vm.initializeConfigs(configFile)

        val configEditCallbackFactory = { index: Int ->
            {
                val intent = Intent(this, RconConfigEditorActivity::class.java)
                intent.putExtra("configIndex", index)

                this.startActivity(intent)
            }
        }

        val configConnectCallbackFactory = { index: Int ->
            {
                val intent = Intent(this, RconConnectionActivity::class.java)
                intent.putExtra("configIndex", index)

                this.startActivity(intent)
            }
        }

        setContent {
            RCONnectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        FloatingActionButton(onClick = configEditCallbackFactory(-1)) {
                            Icon(modifier = Modifier.size(24.dp), painter = painterResource(R.drawable.profile_add), contentDescription = null)
                        }
                    }
                )
                { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        RconConfigList(vm.configsState, configEditCallbackFactory, configConnectCallbackFactory)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val configFile = File(filesDir, RCON_CONFIG_FILENAME)
        vm.saveConfigs(configFile)
    }
}
