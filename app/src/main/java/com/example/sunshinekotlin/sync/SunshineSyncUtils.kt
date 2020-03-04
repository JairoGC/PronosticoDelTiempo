package com.example.sunshinekotlin.sync

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.model.SicronizacionViewModel

object SunshineSyncUtils {

    private var sInitialized = false

    @Synchronized
    fun initialize(context: Context, viewModelOwner: ViewModelStoreOwner) {

        if (sInitialized) return

        sInitialized = true

        val viewModel = ViewModelProvider(viewModelOwner).get(SicronizacionViewModel::class.java)
        viewModel.sicronizar()

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