package com.example.sunshinekotlin.sync

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.NetworkUtils
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils

class WeatherIntentService : IntentService("WaterIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        var mWeatherData: List<WeatherEntry>
        mWeatherData = try{
            val locationQuery = SunshinePreferences.getPreferredWeatherLocation(this)
            val weatherRequestUrl = NetworkUtils.buildUrl(locationQuery)
            val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl)
            OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(this, jsonWeatherResponse)
        }catch (e: Exception){
            e.printStackTrace()
            listOf()
        }
        val database = AppDatabase.getInstance(application)
        database.weatherDao().insertWeather(mWeatherData)
    }
}