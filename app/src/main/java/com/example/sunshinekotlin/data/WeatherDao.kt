package com.example.sunshinekotlin.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY date")
    fun loadAllWeathers(): LiveData<List<WeatherEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntry: List<WeatherEntry>)

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Query("SELECT * FROM weather WHERE uid = :uid")
    fun loadWeatherById(uid: Int): LiveData<WeatherEntry>
}