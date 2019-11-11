package com.example.sunshinekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils
import com.example.sunshinekotlin.utilities.NetworkUtils
import android.os.AsyncTask
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.example.sunshinekotlin.data.SunshinePreferences

class MainActivity : AppCompatActivity() {

    var mWeatherTextView: TextView? = null
    var mErrorMessageDisplay: TextView? = null
    var mLoadingIndicator: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        mWeatherTextView = findViewById(R.id.tv_weather_data)
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display)
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator)
        loadWeatherData()
    }

    private fun loadWeatherData() {
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(location)
    }

    private fun showWeatherDataView(){
        mErrorMessageDisplay?.visibility = View.INVISIBLE
        mWeatherTextView?.visibility = View.VISIBLE
    }

    private fun  showErrorMessage(){
        mErrorMessageDisplay?.visibility = View.VISIBLE
        mWeatherTextView?.visibility = View.INVISIBLE
    }

    inner class FetchWeatherTask : AsyncTask<String, Void, List<String>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mLoadingIndicator?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): List<String>? {

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
                null
            }
        }

        override fun onPostExecute(weatherData: List<String>?) {
            mLoadingIndicator?.visibility = View.INVISIBLE
            if (weatherData != null) {
                showWeatherDataView()
                for (weatherString in weatherData) {
                    mWeatherTextView?.append(weatherString + "\n\n\n")
                }
            }else{
                showErrorMessage()
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
