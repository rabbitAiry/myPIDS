package com.airy.driver_pids.viewmodel

import com.airy.pids_lib.bluetooth.data.BluetoothDevice
import com.airy.pids_lib.bluetooth.data.DriverLineMessage

data class ControlUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val onSendWaiting: Boolean = false,
    val onLineSearchDone: Boolean = false
)
