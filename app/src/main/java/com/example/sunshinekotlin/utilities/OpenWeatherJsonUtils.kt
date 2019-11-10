package com.example.sunshinekotlin.utilities

import android.content.ContentValues
import android.content.Context
import org.json.JSONObject
import org.json.JSONException
import java.net.HttpURLConnection


object OpenWeatherJsonUtils{

    @Throws(JSONException::class)
    fun getSimpleWeatherStringsFromJson(context: Context, forecastJsonStr: String): List<String?> {

        val OWM_LIST = "list"
        val OWM_TEMPERATURE = "temp"
        val OWM_MAX = "max"
        val OWM_MIN = "min"
        val OWM_WEATHER = "weather"
        val OWM_DESCRIPTION = "main"
        val OWM_MESSAGE_CODE = "cod"

        val parsedWeatherData = mutableListOf<String?>()

        val forecastJson = JSONObject(forecastJsonStr)

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {

            when (forecastJson.getInt(OWM_MESSAGE_CODE)) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->
                    /* Location invalid */
                    return parsedWeatherData
                else ->
                    /* Server probably down */
                    return parsedWeatherData
            }
        }

        val weatherArray = forecastJson.getJSONArray(OWM_LIST)

        val localDate = System.currentTimeMillis()
        val utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate)
        val startDay = SunshineDateUtils.normalizeDate(utcDate)

        for (i in 0 until weatherArray.length()) {

            val dayForecast = weatherArray.getJSONObject(i)

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            val dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i
            val date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false)

            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            val description = weatherObject.getString(OWM_DESCRIPTION)

            val temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE)
            val high = temperatureObject.getDouble(OWM_MAX)
            val low = temperatureObject.getDouble(OWM_MIN)
            val highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low)

            parsedWeatherData.add("$date - $description - $highAndLow")
        }

        return parsedWeatherData
    }

    fun getFullWeatherDataFromJson(
        context: Context,
        forecastJsonStr: String
    ): List<ContentValues>? {
        /** This will be implemented in a future lesson  */
        return null
    }
}