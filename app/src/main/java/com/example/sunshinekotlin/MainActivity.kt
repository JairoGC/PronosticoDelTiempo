package com.example.sunshinekotlin

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.data.SunshinePreferences.getPreferredWeatherLocation
import com.example.sunshinekotlin.utilities.NetworkUtils
import com.example.sunshinekotlin.utilities.OpenWeatherJsonUtils


class MainActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnClickHandler,
    LoaderManager.LoaderCallbacks<List<String>>, SharedPreferences.OnSharedPreferenceChangeListener  {

    private val TAG = MainActivity::class.java.simpleName
    private var PREFERENCES_HAVE_BEEN_UPDATED = false

    private val FORECAST_LOADER_ID = 0

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

        supportLoaderManager.initLoader(FORECAST_LOADER_ID, null, this@MainActivity)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<String>> {
        return object : AsyncTaskLoader<List<String>>(this) {

            var mWeatherData: List<String>? = null

            override fun onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData)
                } else {
                    mLoadingIndicator?.visibility = View.VISIBLE
                    forceLoad()
                }
            }

            override fun loadInBackground(): List<String> {
                val locationQuery = SunshinePreferences
                    .getPreferredWeatherLocation(this@MainActivity)
                val weatherRequestUrl = NetworkUtils.buildUrl(locationQuery)

                return try {
                    val jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl)

                    OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                    showErrorMessage()
                    listOf()
                }
            }

            override fun deliverResult(data: List<String>?) {
                mWeatherData = data
                super.deliverResult(data)
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<String>>, data: List<String>) {
        mLoadingIndicator?.visibility = View.INVISIBLE
        mForecastAdapter?.setWeatherData(data)
        if (data.isNotEmpty()) {
            showWeatherDataView()
        }
    }

    override fun onLoaderReset(loader: Loader<List<String>>) {
    }

    private fun invalidateData() {
        mForecastAdapter?.setWeatherData(listOf())
    }

    private fun showWeatherDataView(){
        mErrorMessageDisplay?.visibility = View.INVISIBLE
        mRecyclerView?.visibility = View.VISIBLE
    }

    private fun  showErrorMessage(){
        mErrorMessageDisplay?.visibility = View.VISIBLE
        mRecyclerView?.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this)
            PREFERENCES_HAVE_BEEN_UPDATED = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onClick(weatherForDay: String) {
        val intentToStartDetailActivity = Intent(this, DetailActivity::class.java)
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay)
        startActivity(intentToStartDetailActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                invalidateData()
                supportLoaderManager.restartLoader(FORECAST_LOADER_ID, null, this)
                true
            }
            R.id.action_map -> {
                openLocationInMap()
                true
            }
            R.id.action_settings -> {
                val startSettingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingsActivity)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openLocationInMap() {
        val addressString = getPreferredWeatherLocation(this)
        val geoLocation = Uri.parse("geo:0,0?q=$addressString")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = geoLocation

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Log.d(
                TAG,
                "Couldn't call $geoLocation, no receiving apps installed!"
            )
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        PREFERENCES_HAVE_BEEN_UPDATED = true
    }
}
