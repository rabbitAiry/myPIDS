package com.airy.buspids.repository

import android.content.Context
import android.util.Log
import com.airy.buspids.data.LineTagData
import com.airy.pids_lib.data.Post
import com.airy.pids_lib.utils.getAndroidId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ServerRepository"
@Singleton
class ServerRepository @Inject constructor(@ApplicationContext context: Context){
    private var posts: List<Post>? = null
    private var lineTag: LineTagData? = null
    private val client = OkHttpClient()
    private val gson = Gson()
    private val androidId = getAndroidId(context)
    private val type = "application/x-www-form-urlencoded".toMediaType()

    private var _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    suspend fun getPosts(host: String, port: String): List<Post>{
        Log.d(TAG, "getPosts: ")
        return posts ?: withContext(Dispatchers.IO){
            try {
                val url = "http://$host:$port/post"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute().body.string()
                posts = gson.fromJson(response, object : TypeToken<List<Post>>(){}.type)
                posts!!
            }catch (e: Exception){
                if (e is CancellationException)throw e
                _errorMessage.emit("getPost()出现异常：$e")
                Log.e(TAG, "getPosts: $e")
                emptyList()
            }
        }
    }

    suspend fun getLine(host: String, port: String, driverName: String): LineTagData?{
        Log.d(TAG, "getLine: ")
        return lineTag ?: withContext(Dispatchers.IO){
            try {
                val requestBody = "busId=$androidId&driverName=$driverName".toRequestBody(type)
                val request = Request.Builder().url("http://$host:$port/bus/checkin").post(requestBody).build()
                val response = client.newCall(request).execute().body.string()
                lineTag = gson.fromJson(response, LineTagData::class.java)
                lineTag!!
            }catch (e: Exception){
                if (e is CancellationException)throw e
                _errorMessage.emit("getLine()出现异常：$e")
                Log.e(TAG, "getPosts: $e")
                null
            }
        }
    }
    
    suspend fun postPosition(host: String, port: String, lineId: String, stationIdx: Int, endIdx: Int){
        Log.d(TAG, "postPosition: ")
        withContext(Dispatchers.IO){
            try {
                val requestBody = "busId=$androidId&lineId=$lineId&stationIdx=$stationIdx&endIdx=$endIdx".toRequestBody(type)
                val request = Request.Builder().url("http://$host:$port/busIdLine").post(requestBody).build()
                client.newCall(request).execute()
            }catch (e: Exception){
                if (e is CancellationException)throw e
                _errorMessage.emit("post()出现异常：$e")
                Log.e(TAG, "postPosition: ")
            }
        }
    }

    fun clearData(){
        posts = emptyList()
        lineTag = null
    }
}