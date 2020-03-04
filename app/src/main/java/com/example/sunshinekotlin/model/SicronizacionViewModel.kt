package com.example.sunshinekotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import com.example.sunshinekotlin.sync.SunshineSyncWorker
import java.util.concurrent.TimeUnit

class SicronizacionViewModel (application: Application) : AndroidViewModel(application){

    private val SYNC_INTERVAL_HOURS: Long = 3
    private val SYNC_FLEXTIME_HOURS = SYNC_INTERVAL_HOURS / 3

    private val SUNSHINE_SYNC_TAG = "sunshine-sync"
    private val workManager = WorkManager.getInstance(application)

    fun sicronizar(){
        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val blurRequest = PeriodicWorkRequestBuilder<SunshineSyncWorker>(
            SYNC_INTERVAL_HOURS,
            TimeUnit.HOURS,
            SYNC_FLEXTIME_HOURS,
            TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            SUNSHINE_SYNC_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            blurRequest)
    }
}
