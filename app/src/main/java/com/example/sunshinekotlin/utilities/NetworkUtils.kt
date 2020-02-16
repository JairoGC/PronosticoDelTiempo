package com.example.sunshinekotlin.utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.sunshinekotlin.data.SunshinePreferences.getLocationCoordinates
import com.example.sunshinekotlin.data.SunshinePreferences.getPreferredWeatherLocation
import com.example.sunshinekotlin.data.SunshinePreferences.isLocationLatLonAvailable
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


object NetworkUtils{

    private val TAG = NetworkUtils::class.java.simpleName

    private val DYNAMIC_WEATHER_URL = "https://andfun-weather.udacity.com/weather"
    private val STATIC_WEATHER_URL = "https://andfun-weather.udacity.com/staticweather"
    private val FORECAST_BASE_URL = STATIC_WEATHER_URL

    private val format = "json"
    private val units = "metric"
    private val numDays = 14

    val QUERY_PARAM = "q"
    val LAT_PARAM = "lat"
    val LON_PARAM = "lon"
    val FORMAT_PARAM = "mode"
    val UNITS_PARAM = "units"
    val DAYS_PARAM = "cnt"

    fun getUrl(context: Context): URL? {
        return if (isLocationLatLonAvailable(context)) {
            val preferredCoordinates = getLocationCoordinates(context)
            val latitude = preferredCoordinates[0]
            val longitude = preferredCoordinates[1]
            buildUrl(latitude, longitude)
        } else {
            val locationQuery =
                getPreferredWeatherLocation(context)
            buildUrl(locationQuery)
        }
    }

    fun buildUrl(locationQuery: String): URL? {
        val builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAM, locationQuery)
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
            .build()

        return try {
            val url = URL(builtUri.toString())
            Log.v(TAG, "Built URL: $url")
            url
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
    }

    fun buildUrl(latitude: Double?, longitude: Double?): URL? {
        val weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(LAT_PARAM, java.lang.String.valueOf(latitude))
            .appendQueryParameter(LON_PARAM, java.lang.String.valueOf(longitude))
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
            .build()

        return try {
            val weatherQueryUrl = URL(weatherQueryUri.toString())
            Log.v(TAG, "Built URL: $weatherQueryUrl")
            weatherQueryUrl
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL?): String {
        val urlConnection = url?.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream

            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            return if (hasInput) {
                scanner.next()
            } else {
                ""
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}