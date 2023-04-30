package com.airy.pids_lib.bluetooth.data

sealed interface ConnectionResult {
    object ConnectionEstablished : ConnectionResult
    data class TransferSucceeded(val message: DriverLineMessage) : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}