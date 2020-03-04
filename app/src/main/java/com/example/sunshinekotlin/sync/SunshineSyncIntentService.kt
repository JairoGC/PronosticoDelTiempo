package com.example.sunshinekotlin.sync

import android.app.IntentService
import android.content.Intent
import com.example.sunshinekotlin.sync.SunshineSyncTask.syncWeather

class SunshineSyncIntentService : IntentService("WaterIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        syncWeather(this)
    }
}