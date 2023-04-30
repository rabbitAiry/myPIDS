package com.airy.pids_lib.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.airy.pids_lib.bluetooth.data.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}