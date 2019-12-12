package com.example.sunshinekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ShareCompat

class DetailActivity : AppCompatActivity() {

    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"
    private var mForecast: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mWeatherDisplay: TextView = findViewById(R.id.tv_display_weather)

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            mForecast = intent.getStringExtra(Intent.EXTRA_TEXT)
            mWeatherDisplay.text = mForecast
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        menuItem?.intent = createShareForecastIntent()
        return true
    }

    private fun createShareForecastIntent(): Intent {
        return ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(mForecast?:"" + FORECAST_SHARE_HASHTAG)
            .intent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val startSettingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingsActivity)
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}
