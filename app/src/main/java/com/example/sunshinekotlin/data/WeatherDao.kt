package com.example.sunshinekotlin.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE date >= :date ORDER BY date")
    fun loadByDateGreaterEqualLiveData(date: Date): LiveData<List<WeatherEntry>>

    @Query("SELECT * FROM weather WHERE date >= :date ORDER BY date")
    fun loadByDateGreaterEqual(date: Date): List<WeatherEntry>

/*    @Query("SELECT * FROM weather WHERE date >= :date ORDER BY date")
    fun loadWeathersByDateGreaterEqual(date: Date): List<WeatherEntry>*/

    @Query("SELECT * FROM weather WHERE uid = :uid")
    fun loadByIdLiveData(uid: Int): LiveData<WeatherEntry>

    @Query("SELECT * FROM weather WHERE date = :date")
    fun loadWeatherByDate(date: Date): WeatherEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntry: List<WeatherEntry>)

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Query("SELECT COUNT(uid) FROM weather")
    fun getCount(): Int
}