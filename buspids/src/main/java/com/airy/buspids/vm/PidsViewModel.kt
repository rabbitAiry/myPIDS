package com.airy.buspids.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.buspids.data.LineState
import com.airy.buspids.data.LocationInfo
import com.airy.buspids.repository.ForecastRepository
import com.airy.buspids.repository.LineRepository
import com.airy.buspids.repository.LocationRepository
import com.airy.buspids.repository.logLine
import com.airy.pids_lib.data.*
import com.baidu.geofence.GeoFence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PidsViewModel @Inject constructor(
    private val lineRepository: LineRepository,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    var locationInfo: LocationInfo? by mutableStateOf(null)
    var forecastTimeList: List<Int> by mutableStateOf(emptyList())

    private val lineInfo get() = lineRepository.line
    val line: LineState = LineState(lineInfo)

    val postList: List<Post> = listOf(
        NormalPost("尊老爱幼是中华民族的传统美德，请您为有需要的乘客让座，谢谢！", 10000),
//        StationPost("灯光节展会", 10000, 15)
    )

    init {
        locationRepository.startLocationService()
        viewModelScope.launch {
            locationRepository.location.collect {
                locationInfo = it
            }
        }
        locationRepository.startGeoFenceService(
            lineInfo.stations.subList(
                lineInfo.startStationIdx,
                lineInfo.endStationIdx + 1
            )
        )
        viewModelScope.launch {
            locationRepository.geofencePost.collect { post ->
                post?.let {
                    when (it.stationName) {
                        line.currStation.name -> {
                            if (it.status == GeoFence.STATUS_IN) stationArrived()
                            else if (
                                it.status == GeoFence.STATUS_OUT && line.pidsStatus.value == PidsStatus.BUS_STATION_ARRIVED
                            ) stationLeave()
                        }
                        line.nextStation.name -> {
                            if (it.status == GeoFence.STATUS_IN)
                                line.goNextStation().stationArrived()
                        }
                    }
                    logLine("${it.stationName}#${it.status}#${line.currStation.name}")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationRepository.stopGeofenceService()
        locationRepository.stopLocationService()
    }

    fun stationArrived(){
        line.stationArrived()
        viewModelScope.launch {
            forecastTimeList = forecastRepository.queryOfStations(line.currIdx.value, line.lineInfo.endStationIdx, line.stations)
        }
    }

    fun stationLeave(){
        line.goNextStation().busRun()
    }
}