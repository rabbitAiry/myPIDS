package com.airy.buspids.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.buspids.data.LineState
import com.airy.buspids.data.LocationInfo
import com.airy.buspids.repository.*
import com.airy.pids_lib.data.*
import com.baidu.geofence.GeoFence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PidsViewModel @Inject constructor(
    private val lineRepository: LineRepository,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val serverRepository: ServerRepository
) : ViewModel() {
    var host: String = "192.168.5.6"
    var port: String = "8080"

    var shouldFinish by mutableStateOf(false)
    var locationInfo: LocationInfo? by mutableStateOf(null)
    var forecastTimeList: List<Int>? by mutableStateOf(null)

    private val lineInfo get() = lineRepository.line
    val line: LineState = LineState(lineInfo)

    var postList: List<Post> = emptyList()

    init {
        locationRepository.startLocationService()
        locationRepository.startGeoFenceService(
            lineInfo.stations.subList(
                lineInfo.startStationIdx,
                lineInfo.endStationIdx + 1
            )
        )
        viewModelScope.launch {
            launch {
                locationRepository.location.collect {
                    locationInfo = it
                }
            }
            launch {
                locationRepository.geofencePost.collect { post ->
                    post?.let {
                        when (it.stationName) {
                            line.currStation.name -> {
                                if (it.status == GeoFence.STATUS_IN) stationArrived()
                                else if (
                                    it.status == GeoFence.STATUS_OUT && line.pidsStatus == PidsStatus.BUS_STATION_ARRIVED
                                ) stationLeave()
                            }
                            line.nextStation?.name -> {
                                if (it.status == GeoFence.STATUS_IN)
                                    line.goNextStation().stationArrived()
                            }
                        }
                        logLine("${it.stationName}#${it.status}#${line.currStation.name}")
                    }
                }
            }
            launch {
                postList = serverRepository.getPosts(host, port)
            }
            launch(Dispatchers.IO) {
                queryForecast()
                while (true){
                    delay(30*1000)
                    forecastTimeList?.let{
                        if (line.pidsStatus == PidsStatus.BUS_RUNNING && it[line.currIdx]-30 <= 60){
                            queryForecastOfPosition()
                        } else {
                            val list = it.toMutableList()
                            for (i in line.currIdx until it.size){
                                list[i]-=30
                            }
                            forecastTimeList = list
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationRepository.stopGeofenceService()
        locationRepository.stopLocationService()
        viewModelScope.launch {
            lineRepository.clearLine()
            serverRepository.clearData()
        }
    }

    fun stationArrived(){
        line.stationArrived()
        viewModelScope.launch {
            if (line.isLastStation()){
                delay(30000)
                shouldFinish = true
            }
            launch {
                queryForecast()
            }
            launch {
                serverRepository.postPosition(host, port, lineRepository.lineId, line.currIdx, line.lineInfo.endStationIdx)
            }
        }
    }

    fun stationLeave(){
        line.goNextStation().busRun()
    }

    private suspend fun queryForecast() {
        forecastTimeList = forecastRepository.queryOfStations(
            if (line.pidsStatus == PidsStatus.BUS_RUNNING)line.currIdx-1 else line.currIdx,
            line.lineInfo.endStationIdx,
            line.stations
        )
    }

    private suspend fun queryForecastOfPosition() {
        forecastTimeList = forecastRepository.queryOfStationsOfPosition(
            locationInfo?.latitude,
            locationInfo?.longitude,
            line.currIdx,
            line.lineInfo.endStationIdx,
            line.stations
        )
    }
}