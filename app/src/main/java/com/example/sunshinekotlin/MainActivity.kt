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
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshinekotlin.data.SunshinePreferences
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnClickHandler {

    var mErrorMessageDisplay: TextView? = null
    var mLoadingIndicator: ProgressBar? = null

    private var mRecyclerView: RecyclerView? = null
    private var mForecastAdapter: ForecastAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display)
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator)

        mRecyclerView = findViewById(R.id.recyclerview_forecast)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.setHasFixedSize(true)
        mForecastAdapter = ForecastAdapter(this)
        mRecyclerView?.adapter = mForecastAdapter

        loadWeatherData()
    }

    private fun loadWeatherData() {
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(location)
    }

    private fun showWeatherDataView(){
        mErrorMessageDisplay?.visibility = View.INVISIBLE
        mRecyclerView?.visibility = View.VISIBLE
    }

    private fun  showErrorMessage(){
        mErrorMessageDisplay?.visibility = View.VISIBLE
        mRecyclerView?.visibility = View.INVISIBLE
    }

    override fun onClick(weatherForDay: String) {
        val intentToStartDetailActivity = Intent(this, DetailActivity::class.java)
        startActivity(intentToStartDetailActivity)
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
                mForecastAdapter?.setWeatherData(weatherData)
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
            mForecastAdapter?.setWeatherData(listOf())
            loadWeatherData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
