package com.example.sunshinekotlin.utilities

import android.content.Context
import com.example.sunshinekotlin.data.SunshinePreferences.setLocationDetails
import com.example.sunshinekotlin.data.WeatherEntry
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.util.*

object OpenWeatherJsonUtils{

    /* Location information */
    private const val OWM_CITY = "city"
    private const val OWM_COORD = "coord"

    /* Location coordinate */
    private const val OWM_LATITUDE = "lat"
    private const val OWM_LONGITUDE = "lon"

    /* Weather information. Each day's forecast info is an element of the "list" array */
    private const val OWM_LIST = "list"

    private const val OWM_PRESSURE = "pressure"
    private const val OWM_HUMIDITY = "humidity"
    private const val OWM_WINDSPEED = "speed"
    private const val OWM_WIND_DIRECTION = "deg"

    /* All temperatures are children of the "temp" object */
    private const val OWM_TEMPERATURE = "temp"

    /* Max temperature for the day */
    private const val OWM_MAX = "max"
    private const val OWM_MIN = "min"

    private const val OWM_WEATHER = "weather"
    private const val OWM_WEATHER_ID = "id"

    private const val OWM_MESSAGE_CODE = "cod"

    @Throws(JSONException::class)
    fun getSimpleWeatherStringsFromJson(context: Context, forecastJsonStr: String): List<WeatherEntry> {

        val OWM_DESCRIPTION = "main"

        val parsedWeatherData = mutableListOf<WeatherEntry>()

        val forecastJson = JSONObject(forecastJsonStr)

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {

            when (forecastJson.getInt(OWM_MESSAGE_CODE)) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->
                    return listOf()
                else ->
                    return listOf()
            }
        }

        val weatherArray = forecastJson.getJSONArray(OWM_LIST)

        val localDate = System.currentTimeMillis()
        val utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate)
        val startDay = SunshineDateUtils.normalizeDate(utcDate)

        for (i in 0 until weatherArray.length()) {
            val dayForecast = weatherArray.getJSONObject(i)

            val dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i
            //val date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false)

            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            val description = weatherObject.getString(OWM_DESCRIPTION)

            val temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE)
            val high = temperatureObject.getDouble(OWM_MAX)
            val low = temperatureObject.getDouble(OWM_MIN)
            val highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low)

            val date = Date(dateTimeMillis)
            val weatherEntry = WeatherEntry(i,low, high, high,
                low, low, low, date)

            parsedWeatherData.add(weatherEntry)
        }
        return parsedWeatherData
    }

    @Throws(JSONException::class)
    fun getFullWeatherDataFromJson(context: Context, forecastJsonStr: String): List<WeatherEntry>{
        val forecastJson = JSONObject(forecastJsonStr)

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            when (forecastJson.getInt(OWM_MESSAGE_CODE)) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->
                    return listOf()
                else ->
                    return listOf()
            }
        }

        val jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST)

        val cityJson = forecastJson.getJSONObject(OWM_CITY)

        val cityCoord = cityJson.getJSONObject(OWM_COORD)
        val cityLatitude = cityCoord.getDouble(OWM_LATITUDE)
        val cityLongitude = cityCoord.getDouble(OWM_LONGITUDE)

        setLocationDetails(context, cityLatitude, cityLongitude)

        val weatherArray = mutableListOf<WeatherEntry>()

        val normalizedUtcStartDay: Long = SunshineDateUtils.getNormalizedUtcDateForToday()

        for (i in 0 until jsonWeatherArray.length()) {

            val dayForecast = jsonWeatherArray.getJSONObject(i)

            val dateTimeMillis: Long = normalizedUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * i

            val pressure = dayForecast.getDouble(OWM_PRESSURE)
            val humidity = dayForecast.getDouble(OWM_HUMIDITY)
            val windSpeed = dayForecast.getDouble(OWM_WINDSPEED)
            val windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION)

            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            val weatherId = weatherObject.getInt(OWM_WEATHER_ID)

            val temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE)
            val high = temperatureObject.getDouble(OWM_MAX)
            val low = temperatureObject.getDouble(OWM_MIN)

            val date = Date(dateTimeMillis)
            val weatherEntry = WeatherEntry(weatherId, low, high, windSpeed,
                humidity, pressure, windDirection, date)

            weatherArray.add(weatherEntry)
        }

        return weatherArray
    }
}