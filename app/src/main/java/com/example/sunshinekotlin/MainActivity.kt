package com.example.sunshinekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils
import com.example.sunshinekotlin.utilities.NetworkUtils
import android.os.AsyncTask
import android.view.Menu
import android.view.MenuItem
import com.example.sunshinekotlin.data.SunshinePreferences

class MainActivity : AppCompatActivity() {

    var mWeatherTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        mWeatherTextView = findViewById(R.id.tv_weather_data)
        loadWeatherData()
    }

    private fun loadWeatherData() {
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(location)
    }

    inner class FetchWeatherTask : AsyncTask<String, Void, List<String>>() {

        override fun doInBackground(vararg params: String): List<String> {

            if (params.isEmpty()) {
                return listOf()
            }

            val location = params[0]
            val weatherRequestUrl = NetworkUtils.buildUrl(location)

            return try {
                val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl)

                OpenWeatherJsonUtils
                    .getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse)

            } catch (e: Exception) {
                e.printStackTrace()
                listOf()
            }
        }

        override fun onPostExecute(weatherData: List<String>) {
            for (weatherString in weatherData) {
                mWeatherTextView?.append(weatherString + "\n\n\n")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_refresh) {
            mWeatherTextView?.text = ""
            loadWeatherData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
