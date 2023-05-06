package com.airy.driver_pids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.driver_pids.repository.LoginRepository
import com.airy.pids_lib.bluetooth.BluetoothController
import com.airy.pids_lib.bluetooth.data.BluetoothDeviceDomain
import com.airy.pids_lib.bluetooth.data.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _state = MutableStateFlow(ControlUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)

        bluetoothController.startDiscovery()
    }

    private var deviceConnectionJob: Job? = null

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update { it.copy(
            isConnecting = false,
            isConnected = false
        ) }
    }

    private fun sendDriverNameMessage() {
        viewModelScope.launch {
            bluetoothController.trySendMessage(loginRepository.driverName ?: "测试")
        }
    }

    private fun release(){
        bluetoothController.stopDiscovery()
        bluetoothController.release()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectFromDevice()
        release()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    sendDriverNameMessage()
                    _state.update { it.copy(
                        errorMessage = null,
                        isConnected = true,
                        isConnecting = false,
                        onSendWaiting = true
                    ) }
                }
                is ConnectionResult.TransferSucceeded -> {
                    _state.update { it.copy(
                        onLineSearchDone = true
                    ) }
                    disconnectFromDevice()
                    release()
                }
                is ConnectionResult.Error -> {
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = result.message
                    ) }
                }
            }
        }
            .catch { _ ->
                bluetoothController.closeConnection()
                _state.update { it.copy(
                    isConnected = false,
                    isConnecting = false,
                ) }
            }
            .launchIn(viewModelScope)
    }
}