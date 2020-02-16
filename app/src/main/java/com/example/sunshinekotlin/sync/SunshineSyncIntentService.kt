package com.example.sunshinekotlin.sync

import android.app.IntentService
import android.content.Intent

class SunshineSyncIntentService : IntentService("WaterIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        SunshineSyncTask.syncWeather(this)
    }
}