package com.airy.buspids.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.buspids.data.LineTagData
import com.airy.buspids.repository.LineRepository
import com.airy.buspids.repository.ServerRepository
import com.airy.pids_lib.bluetooth.BluetoothController
import com.airy.pids_lib.bluetooth.data.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val lineRepository: LineRepository,
    private val serverRepository: ServerRepository
) : ViewModel() {
    private var deviceConnectionJob: Job? = null
    var host: String = "192.168.5.6"
    var port: String = "8080"

    private val _state = MutableStateFlow(BluetoothUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(message = if (isConnected) "已连接" else "未连接") }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(
                    message = error
                )
            }
        }.launchIn(viewModelScope)

        lineRepository.isSearchLineDone.onEach { isDone ->
            _state.update {
                it.copy(isLineSearchDone = isDone)
            }
        }.launchIn(viewModelScope)

        lineRepository.errors.onEach { error ->
            _state.update {
                it.copy(
                    message = error
                )
            }
        }.launchIn(viewModelScope)

        serverRepository.errorMessage.onEach { error ->
            _state.update {
                it.copy(
                    message = error
                )
            }
        }.launchIn(viewModelScope)

        startBluetoothServer()
    }

    private fun startBluetoothServer() {
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                message = "断开连接",
                isLineSearchDone = false
            )
        }
    }

    fun searchLine(data: LineTagData){
        viewModelScope.launch {
            lineRepository.searchLine(data.city, data.lineId, IntRange(data.startIdx, data.endIdx))
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            message = "已连接",
                        )
                    }
                }
                is ConnectionResult.TransferSucceeded -> {
                    val driverName = result.message
                    _state.update {
                        it.copy(
                            message = "开始搜索线路，司机：$driverName"
                        )
                    }
                    getLineAndPost(driverName)
                }
                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            message = result.message
                        )
                    }
                }
            }
        }.catch {
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    message = "已断开"
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        disconnectFromDevice()
        bluetoothController.release()
    }

    private fun getLineAndPost(driverName: String) {
        viewModelScope.launch {
            launch {
                serverRepository.getLine(host, port, driverName)?.let { tag ->
                    searchLine(tag)
                }
            }
            launch {
                serverRepository.getPosts(host, port)
            }
        }
    }
}