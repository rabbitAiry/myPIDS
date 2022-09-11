package com.airy.mypids

import android.app.Application
import com.baidu.mapapi.SDKInitializer

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        SDKInitializer.setAgreePrivacy(this, true)
        SDKInitializer.initialize(this)
    }
}