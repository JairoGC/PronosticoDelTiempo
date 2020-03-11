package com.example.sunshinekotlin.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.sunshinekotlin.R


object SunshinePreferences{

    val PREF_CITY_NAME = "city_name"
    val PREF_COORD_LAT = "coord_lat"
    val PREF_COORD_LONG = "coord_long"

    private val DEFAULT_WEATHER_LOCATION = "94043,USA"
    private val DEFAULT_WEATHER_COORDINATES = doubleArrayOf(37.4284, 122.0724)

    private val DEFAULT_MAP_LOCATION = "1600 Amphitheatre Parkway, Mountain View, CA 94043"

    fun setLocationDetails(context: Context, lat: Double, lon: Double) {
        var sp: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()

        editor.putLong(PREF_COORD_LAT, java.lang.Double.doubleToRawLongBits(lat))
        editor.putLong(PREF_COORD_LONG, java.lang.Double.doubleToRawLongBits(lon))
        editor.apply()
    }

    fun setLocation(c: Context, locationSetting: String, lat: Double, lon: Double) {
        /** This will be implemented in a future lesson  */
    }

    fun resetLocationCoordinates(context: Context) {
        val sp = PreferenceManager
            .getDefaultSharedPreferences(context)
        val editor = sp.edit()

        editor.remove(PREF_COORD_LAT)
        editor.remove(PREF_COORD_LONG)
        editor.apply()
    }

    fun getPreferredWeatherLocation(context: Context): String {
        var prefs = PreferenceManager
            .getDefaultSharedPreferences(context)
        val keyForLocation = context.getString(R.string.pref_location_key)
        val defaultLocation = context.getString(R.string.pref_location_default)
        return prefs?.getString(keyForLocation, defaultLocation)?:""
    }

    fun isMetric(context: Context): Boolean {
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(context)
        val keyForUnits = context.getString(R.string.pref_units_key)
        val defaultUnits = context.getString(R.string.pref_units_metric)
        val preferredUnits = prefs.getString(keyForUnits, defaultUnits)?:""
        val metric = context.getString(R.string.pref_units_metric)
        return metric == preferredUnits
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

    fun areNotificationsEnabled(context: Context): Boolean {
        val displayNotificationsKey =
            context.getString(R.string.pref_enable_notifications_key)

        val shouldDisplayNotificationsByDefault = context
            .resources
            .getBoolean(R.bool.show_notifications_by_default)

        val sp =
            PreferenceManager.getDefaultSharedPreferences(context)

        return sp
            .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault)
    }

    fun getDefaultWeatherCoordinates(): DoubleArray {
        /** This will be implemented in a future lesson  */
        return DEFAULT_WEATHER_COORDINATES
    }

    fun getLastNotificationTimeInMillis(context: Context): Long {
        val lastNotificationKey = context.getString(R.string.pref_last_notification)
        val sp =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getLong(lastNotificationKey, 0)
    }

    fun getEllapsedTimeSinceLastNotification(context: Context?): Long {
        val lastNotificationTimeMillis =
            getLastNotificationTimeInMillis(context!!)
        return System.currentTimeMillis() - lastNotificationTimeMillis
    }

    fun saveLastNotificationTime(context: Context, timeOfNotification: Long) {
        val sp = PreferenceManager
            .getDefaultSharedPreferences(context)
        val editor = sp.edit()
        val lastNotificationKey = context.getString(R.string.pref_last_notification)
        editor.putLong(lastNotificationKey, timeOfNotification)
        editor.apply()
    }
}