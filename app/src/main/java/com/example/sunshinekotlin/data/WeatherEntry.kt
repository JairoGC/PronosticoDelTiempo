package com.example.sunshinekotlin.data

import androidx.room.*
import java.util.*

@Entity(tableName = "weather")
data class WeatherEntry(
    @PrimaryKey(autoGenerate = true) var weather_id: Int?,
    @ColumnInfo(name = "min") val min: Double,
    @ColumnInfo(name = "max") val max: Double,
    @ColumnInfo(name = "wind") val wind: Double,
    @ColumnInfo(name = "humidity") val humidity: Double,
    @ColumnInfo(name = "pressure") val pressure: Double,
    @ColumnInfo(name = "degrees") val degrees: Double,
    @ColumnInfo(name = "date", index = true) val date: Date
) {
    @Ignore
    constructor(min: Double, max: Double, wind: Double, humidity: Double, pressure:Double, degrees:Double, date: Date) : this(
        null,
        min,
        max,
        wind,
        humidity,
        pressure,
        degrees,
        date
    ) { }
}