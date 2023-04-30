package com.airy.driver_pids.viewmodel

import com.airy.pids_lib.bluetooth.data.BluetoothDevice
import com.airy.pids_lib.bluetooth.data.DriverLineMessage

data class ControlUiState(
    val accessibleLineMessages: List<DriverLineMessage> = listOf(DriverLineMessage("1", "广州", "B8", "e45505067cf66173911fc954", IntRange(17, 24))),
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val onSendWaiting: Boolean = false,
    val onLineSearchDone: Boolean = false
)
