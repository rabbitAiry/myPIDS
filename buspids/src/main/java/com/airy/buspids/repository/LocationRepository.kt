package com.airy.buspids.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.airy.buspids.data.GeoPost
import com.airy.buspids.data.LocationInfo
import com.airy.pids_lib.data.Station
import com.baidu.geofence.GeoFence
import com.baidu.geofence.GeoFenceClient
import com.baidu.geofence.GeoFenceClient.GEOFENCE_IN_OUT
import com.baidu.geofence.model.DPoint
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
private const val GEOFENCE_SERVICE = "baiduGeofenceService"
private const val SCAN_DURATION = 5000
private const val GEO_FENCE_SIZE = 50F
private const val TAG = "Location"

@Singleton
class LocationRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var locationClient: LocationClient? = null
    private var geoFenceClient: GeoFenceClient? = null
    private var geofenceReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == GEOFENCE_SERVICE){
                intent.extras?.let {
                    val status = it.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS)
                    val station = it.getString(GeoFence.BUNDLE_KEY_CUSTOMID)
                    _geofencePost.update { station?.let { s -> GeoPost(s, status) } }
                }
            }
        }
    }
    private val locationOption = LocationClientOption().apply {
        locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        coorType = "bd09ll"
        openGps = true
        isOpenGnss = true
        setEnableSimulateGnss(true)
        setScanSpan(SCAN_DURATION)
    }

    private val _location = MutableSharedFlow<LocationInfo>()
    val location: SharedFlow<LocationInfo>
        get() = _location.asSharedFlow()

    private val _geofencePost = MutableStateFlow<GeoPost?>(null)
    val geofencePost get() = _geofencePost.asStateFlow()

    init {
        locationClient = LocationClient(context).apply {
            locOption = locationOption
            registerLocationListener(object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    serviceScope.launch {
                        location?.let {
                            _location.emit(
                                LocationInfo(
                                    it.latitude,
                                    it.longitude,
                                    it.radius,
                                    it.direction
                                )
                            )
                            logPosition("lat: ${it.latitude}, lon: ${it.longitude}, dir: ${it.direction}, spd: ${it.speed}")
                        }
                    }
                }
            })
        }
        geoFenceClient = GeoFenceClient(context).apply {
            setActivateAction(GEOFENCE_IN_OUT)
            setTriggerCount(3,3,0)
        }
    }

    fun startLocationService() {
        locationClient?.start()
    }

    fun stopLocationService() {
        locationClient?.stop()
    }

    fun startGeoFenceService(stations: List<Station>){
        for (station in stations){
            val lat = station.latitude
            val lon = station.longitude
            geoFenceClient!!.addGeoFence(DPoint(lat, lon), GeoFenceClient.BD09LL, GEO_FENCE_SIZE, station.name)
        }
        geoFenceClient!!.createPendingIntent(GEOFENCE_SERVICE)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(GEOFENCE_SERVICE)
        }
        context.registerReceiver(geofenceReceiver, intentFilter)
    }

    fun stopGeofenceService(){
        geoFenceClient?.removeGeoFence()
        context.unregisterReceiver(geofenceReceiver)
    }
}