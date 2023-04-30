package com.airy.buspids.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.buspids.data.LineTagData
import com.airy.buspids.repository.LineRepository
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
    private val lineRepository: LineRepository
) : ViewModel() {
    private var deviceConnectionJob: Job? = null

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
    }

    fun startBluetoothServer() {
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
                    val message = result.message
                    _state.update {
                        it.copy(
                            message = "开始搜索线路"
                        )
                    }
                    lineRepository.searchLine(message.cityName, message.uid, message.range)
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
        bluetoothController.release()
    }

    fun searchLine(data: LineTagData){
        viewModelScope.launch {
            lineRepository.searchLine(data.city, data.uid, IntRange(data.startIdx, data.endIdx))
        }
    }

    fun clearLine(){
        viewModelScope.launch {
            lineRepository.clearLine()
        }
    }
}