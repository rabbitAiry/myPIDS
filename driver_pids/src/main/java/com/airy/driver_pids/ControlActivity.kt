package com.airy.driver_pids

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airy.pids_lib.ui.components.ConfigRowOfTextField
import com.airy.pids_lib.ui.components.ScalableTopBar
import com.airy.pids_lib.ui.components.TitleCard
import com.airy.driver_pids.ui.theme.MyPIDSTheme
import com.airy.driver_pids.viewmodel.ControlUiState
import com.airy.driver_pids.viewmodel.ControlViewModel
import com.airy.pids_lib.bluetooth.data.BluetoothDevice
import com.airy.pids_lib.bluetooth.data.DriverLineMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ControlActivity : ComponentActivity() {
    private val vm: ControlViewModel by viewModels()

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else {
                perms[Manifest.permission.BLUETOOTH] == true && perms[Manifest.permission.BLUETOOTH_ADMIN] == true
            }

            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        setContent {
            MyPIDSTheme {
                val state by vm.state.collectAsState()

                LaunchedEffect(key1 = state.errorMessage) {
                    state.errorMessage?.let {
                        Toast.makeText(this@ControlActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scrollState = rememberScrollState()

                    Scaffold(topBar = {
                        ScalableTopBar(
                            title = "选择",
                            scrollState = scrollState,
                            context = applicationContext
                        )
                    }) { innerPadding ->
                        Column(Modifier.padding(innerPadding)) {
                            TitleCard(title = "选择车辆", Modifier.padding(horizontal = 10.dp)) {
                                if (state.onLineSearchDone) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Default.Done, "", Modifier.padding(end = 10.dp))
                                        Text(text = "已完成，可退出")
                                    }
                                } else if (state.onSendWaiting) {
                                    CircularProgressIndicator(
                                        Modifier
                                            .padding(10.dp)
                                            .size(40.dp)
                                    )
                                    Text(text = "正在发送")
                                } else if (state.isConnected) {
                                    CircularProgressIndicator(
                                        Modifier
                                            .padding(10.dp)
                                            .size(40.dp)
                                    )
                                    Text(text = "正在连接")
                                } else {
                                    BluetoothDeviceBar(state.pairedDevices, state.scannedDevices) {
                                        vm.connectToDevice(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun BluetoothDeviceBar(
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    onDeviceSelect: (BluetoothDevice) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = ">> 已配对",
                modifier = Modifier.padding(16.dp)
            )
        }
        items(pairedDevices) { device ->
            Text(
                text = device.name ?: "(未命名设备)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeviceSelect(device) }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = ">> 扫描结果",
                modifier = Modifier.padding(16.dp)
            )
        }
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: "(未命名设备)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeviceSelect(device) }
                    .padding(16.dp)
            )
        }
    }

}
