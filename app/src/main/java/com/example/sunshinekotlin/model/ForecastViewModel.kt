package com.example.sunshinekotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry

class ForecastViewModel(application: Application) : AndroidViewModel(application)  {

    private val weathers: LiveData<List<WeatherEntry>>

    init {
        val database = AppDatabase.getInstance(application)
        weathers = database.weatherDao().loadAllWeathers()
    }

    fun getTasks(): LiveData<List<WeatherEntry>> {
        return weathers
    }

}