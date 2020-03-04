package com.example.sunshinekotlin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY date")
    fun loadAllWeathers(): LiveData<List<WeatherEntry>>

    @Query("SELECT * FROM weather WHERE uid = :uid")
    fun loadWeatherById(uid: Int): LiveData<WeatherEntry>

    @Query("SELECT * FROM weather WHERE date = :date")
    fun loadWeatherByDate(date: Date): WeatherEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntry: List<WeatherEntry>)

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Query("SELECT COUNT(uid) FROM weather")
    fun getCount(): Int
}