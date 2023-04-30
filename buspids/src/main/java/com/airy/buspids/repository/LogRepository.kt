package com.airy.buspids.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedList

object LogRepository {
    val logLineList = LinkedList<String>().toMutableStateList()
    val logPositionList = LinkedList<String>().toMutableStateList()
}

@SuppressLint("SimpleDateFormat")
val formatter = SimpleDateFormat("HH:mm")

fun logLine(txt: String){
    Log.d("Line", txt)
    LogRepository.logLineList.add(formatter.format(Date().time)+txt)
}

fun logPosition(txt: String){
    Log.d("Position", txt)
    LogRepository.logPositionList.add(formatter.format(Date().time)+txt)
}