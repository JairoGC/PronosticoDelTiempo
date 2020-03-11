package com.example.sunshinekotlin.sync

import android.content.Context
import android.os.AsyncTask
import android.text.format.DateUtils
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.NetworkUtils
import com.example.sunshinekotlin.utilities.NetworkUtils.getResponseFromHttpUrl
import com.example.sunshinekotlin.utilities.NotificationUtils
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils
import com.example.sunshinekotlin.utilities.SunshineDateUtils

object SunshineSyncTask {

    @Synchronized
    fun syncWeather(context: Context) {
        try {
            val weatherRequestUrl = NetworkUtils.getUrl(context)
            val jsonWeatherResponse = getResponseFromHttpUrl(weatherRequestUrl)

            val weatherArray: List<WeatherEntry> = OpenWeatherJsonUtils
                .getFullWeatherDataFromJson(context, jsonWeatherResponse)

            val database = AppDatabase.getInstance(context)
            database.weatherDao().deleteAll()
            database.weatherDao().insertWeather(weatherArray)

            val notificationsEnabled: Boolean = SunshinePreferences.areNotificationsEnabled(context)
            if (notificationsEnabled) {
                val timeSinceLastNotification: Long = SunshinePreferences
                    .getEllapsedTimeSinceLastNotification(context)
                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    NotificationUtils.notifyUserOfNewWeather(context)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun refresh(context: Context) {
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit) {
                val database = AppDatabase.getInstance(context)
                val date = SunshineDateUtils.getNormalizedDateForToday()
                val weatherList = database.weatherDao().loadByDateGreaterEqual(date)
                database.weatherDao().deleteAll()
                database.weatherDao().insertWeather(weatherList)
            }
        }.execute()
    }

}