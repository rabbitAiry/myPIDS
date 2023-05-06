package com.airy.driver_pids.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.airy.driver_pids.repository.LoginRepository
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel(){
    var isLogin by mutableStateOf(false)
    var isErrorLogin by mutableStateOf(false)

    fun login(driverName: String, password: String, host: String){
        viewModelScope.launch {
            isErrorLogin = false
            isLogin = loginRepository.login(driverName, password, host)
            isErrorLogin = !isLogin
        }
    }
}