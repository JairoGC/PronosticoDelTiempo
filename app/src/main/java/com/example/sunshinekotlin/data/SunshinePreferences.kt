package com.example.sunshinekotlin.data

import android.content.Context

object SunshinePreferences{

    val PREF_CITY_NAME = "city_name"
    val PREF_COORD_LAT = "coord_lat"
    val PREF_COORD_LONG = "coord_long"

    private val DEFAULT_WEATHER_LOCATION = "94043,USA"
    private val DEFAULT_WEATHER_COORDINATES = doubleArrayOf(37.4284, 122.0724)

    private val DEFAULT_MAP_LOCATION = "1600 Amphitheatre Parkway, Mountain View, CA 94043"

    fun setLocationDetails(c: Context, cityName: String, lat: Double, lon: Double) {
        /** This will be implemented in a future lesson  */
    }

    fun setLocation(c: Context, locationSetting: String, lat: Double, lon: Double) {
        /** This will be implemented in a future lesson  */
    }

    fun resetLocationCoordinates(c: Context) {
        /** This will be implemented in a future lesson  */
    }

    fun getPreferredWeatherLocation(context: Context): String {
        /** This will be implemented in a future lesson  */
        return getDefaultWeatherLocation()
    }

    fun isMetric(context: Context): Boolean {
        /** This will be implemented in a future lesson  */
        return true
    }

    fun getLocationCoordinates(context: Context): DoubleArray {
        return getDefaultWeatherCoordinates()
    }

    fun isLocationLatLonAvailable(context: Context): Boolean {
        /** This will be implemented in a future lesson  */
        return false
    }

    private fun getDefaultWeatherLocation(): String {
        /** This will be implemented in a future lesson  */
        return DEFAULT_WEATHER_LOCATION
    }

    fun getDefaultWeatherCoordinates(): DoubleArray {
        /** This will be implemented in a future lesson  */
        return DEFAULT_WEATHER_COORDINATES
    }
}