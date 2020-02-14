package com.example.sunshinekotlin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry

class WeatherViewModel(database: AppDatabase, weatherId: Int) : ViewModel() {
    private val weather: LiveData<WeatherEntry> = database.weatherDao().loadWeatherById(weatherId)

    fun getWeather(): LiveData<WeatherEntry> {
        return weather
    }
}