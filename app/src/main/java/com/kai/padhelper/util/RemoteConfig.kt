package com.kai.padhelper.util

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.kai.padhelper.R

object RemoteConfig {
    private var initialized = false
    var MAX_CHARACTER_INDEX: Long? = null
        private set

    fun initialize() {
        if (initialized) return

        val firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                MAX_CHARACTER_INDEX = firebaseRemoteConfig.getLong("max_character_index")
            }
            // 可以在这里通知其他部分配置已更新
        }

        initialized = true
    }
}