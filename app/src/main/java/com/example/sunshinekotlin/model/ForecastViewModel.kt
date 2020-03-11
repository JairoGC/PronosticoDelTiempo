package com.example.sunshinekotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.SunshineDateUtils

class ForecastViewModel(application: Application) : AndroidViewModel(application)  {

    private val weathers: LiveData<List<WeatherEntry>>

    init {
        val database = AppDatabase.getInstance(application)
        val date = SunshineDateUtils.getNormalizedDateForToday()
        weathers = database.weatherDao().loadByDateGreaterEqualLiveData(date)
    }

    fun getWeathers(): LiveData<List<WeatherEntry>> {
        return weathers
    }

}