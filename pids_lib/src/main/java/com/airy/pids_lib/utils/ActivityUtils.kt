package com.airy.pids_lib.utils

import android.app.Activity
import android.view.WindowManager

fun Activity.setFullScreen(){
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}