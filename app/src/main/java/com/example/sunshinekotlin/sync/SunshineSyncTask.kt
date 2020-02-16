package com.example.sunshinekotlin.sync

import android.content.Context
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.NetworkUtils
import com.example.sunshinekotlin.utilities.NetworkUtils.getResponseFromHttpUrl
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils

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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}