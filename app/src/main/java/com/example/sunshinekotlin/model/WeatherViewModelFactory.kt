package com.example.sunshinekotlin.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sunshinekotlin.data.AppDatabase

class WeatherViewModelFactory (private val mDb: AppDatabase, private val uid: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(mDb, uid) as T
    }
}