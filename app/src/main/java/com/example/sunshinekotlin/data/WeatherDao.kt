package com.example.sunshinekotlin.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY date")
    fun loadAllWeathers(): LiveData<List<WeatherEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntry: List<WeatherEntry>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWeather(weathersEntry: WeatherEntry)

    @Delete
    fun deleteWeather(weatherEntry: WeatherEntry)

    @Query("SELECT * FROM weather WHERE weather_id = :uid")
    fun loadWeatherById(uid: Int): LiveData<WeatherEntry?>
}