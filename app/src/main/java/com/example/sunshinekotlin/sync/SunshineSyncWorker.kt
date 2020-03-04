package com.example.sunshinekotlin.sync

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.sunshinekotlin.sync.SunshineSyncTask.syncWeather

class SunshineSyncWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        return try {
            syncWeather(applicationContext)
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }

}