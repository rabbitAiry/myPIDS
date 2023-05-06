package com.airy.driver_pids.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val httpSite = ":8080/drivers/check"
private const val TAG = "LoginRepository"

@Singleton
class LoginRepository @Inject constructor() {
    var driverName: String? = null

    suspend fun login(driverName: String, password: String, host: String): Boolean{
        this.driverName = driverName
        return withContext(Dispatchers.IO){
            try {
                val client = OkHttpClient()
                val type = "application/x-www-form-urlencoded".toMediaType()
                val requestBody = "name=${driverName}&password=${password}".toRequestBody(type)
                val request = Request.Builder().url("http://${host}${httpSite}").post(requestBody).build()
                client.newCall(request).execute().code == 200
            }catch (e : IOException){
                Log.e(TAG, "login: $e")
                false
            }
        }
    }
}