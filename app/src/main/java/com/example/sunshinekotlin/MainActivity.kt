package com.example.sunshinekotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshinekotlin.data.SunshinePreferences.getPreferredWeatherLocation
import com.example.sunshinekotlin.model.ForecastViewModel
import com.example.sunshinekotlin.sync.SunshineSyncIntentService
import com.example.sunshinekotlin.sync.SunshineSyncUtils

class MainActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnClickHandler {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mLoadingIndicator: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mForecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator)
        mRecyclerView = findViewById(R.id.recyclerview_forecast)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mForecastAdapter = ForecastAdapter(this, this)
        mRecyclerView.adapter = mForecastAdapter

        this.setupViewModel()
        SunshineSyncUtils.startImmediateSync(this)
    }

    private fun setupViewModel() {
        var forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastViewModel.getTasks().observe(this, Observer {
            mForecastAdapter.setWeatherData(it)
        })
    }

    override fun onClick(uid: Int?) {
        val intentToStartDetailActivity = Intent(this, DetailActivity::class.java)
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, uid)
        startActivity(intentToStartDetailActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

}
