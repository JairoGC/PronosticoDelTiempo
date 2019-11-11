package com.example.sunshinekotlin.utilities

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import android.net.Uri;
import android.util.Log
import java.net.MalformedURLException

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

    fun buildUrl(locationQuery: String): URL? {
        val builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAM, locationQuery)
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
            .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        Log.v(TAG, "Built URI " + url?.toString())
        return url
    }

    fun buildUrl(lat: Double?, lon: Double?): URL? {
        /** This will be implemented in a future lesson  */
        return null
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