package com.example.sunshinekotlin.sync

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.NetworkUtils
import com.example.sunshinekotlin.utilities.NetworkUtils.getResponseFromHttpUrl
import com.example.sunshinekotlin.utilities.NotificationUtils
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils
import com.example.sunshinekotlin.utilities.SunshineDateUtils
import java.util.*


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

}