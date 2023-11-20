package com.kai.padhelper

import android.app.Application
import com.kai.padhelper.util.RemoteConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PadHelperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RemoteConfig.initialize()
    }
}