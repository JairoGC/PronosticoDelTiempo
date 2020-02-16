package com.example.sunshinekotlin.sync

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import com.example.sunshinekotlin.data.AppDatabase


object SunshineSyncUtils {

    private var sInitialized = false

    @Synchronized
    fun initialize(context: Context) {

        if (sInitialized) return

        sInitialized = true

        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit) {
                val database = AppDatabase.getInstance(context)
                val count = database.weatherDao().getCount()
                if (count <= 0) {
                    startImmediateSync(context)
                }
            }
        }.execute()
    }

    fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, SunshineSyncIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }
}