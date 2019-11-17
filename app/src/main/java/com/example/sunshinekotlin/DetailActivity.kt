package com.example.sunshinekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mWeatherDisplay: TextView = findViewById(R.id.tv_display_weather)

        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                val mForecast = intent.getStringExtra(Intent.EXTRA_TEXT)
                mWeatherDisplay.text = mForecast
            }
        }
    }
}
