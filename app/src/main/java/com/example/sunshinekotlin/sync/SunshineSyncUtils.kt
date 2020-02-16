package com.example.sunshinekotlin.sync

import android.content.Context
import android.content.Intent


object SunshineSyncUtils {
    fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, SunshineSyncIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }
}