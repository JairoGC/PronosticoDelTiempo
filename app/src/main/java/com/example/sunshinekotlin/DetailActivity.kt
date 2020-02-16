package com.example.sunshinekotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.model.WeatherViewModel
import com.example.sunshinekotlin.model.WeatherViewModelFactory
import com.example.sunshinekotlin.utilities.SunshineDateUtils.getFriendlyDateString
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.formatTemperature
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getFormattedWind
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getStringForWeatherCondition


class DetailActivity : AppCompatActivity() {

    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"
    private var mForecastSummary: String? = null

    private lateinit var mDateView: TextView
    private lateinit var mDescriptionView: TextView
    private lateinit var mHighTemperatureView: TextView
    private lateinit var mLowTemperatureView: TextView
    private lateinit var mHumidityView: TextView
    private lateinit var mWindView: TextView
    private lateinit var mPressureView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mDateView = findViewById(R.id.date)
        mDescriptionView = findViewById(R.id.weather_description)
        mHighTemperatureView = findViewById(R.id.high_temperature)
        mLowTemperatureView = findViewById(R.id.low_temperature)
        mHumidityView = findViewById(R.id.humidity)
        mWindView = findViewById(R.id.wind)
        mPressureView = findViewById(R.id.pressure)

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            val uid = intent.getIntExtra(Intent.EXTRA_TEXT,0)
            this.loaderWeather(uid)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        menuItem?.intent = createShareForecastIntent()
        return true
    }

    private fun loaderWeather(uid: Int){
        val factory = WeatherViewModelFactory(AppDatabase.getInstance(applicationContext), uid)
        val viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        viewModel.getWeather().observe(this, object : Observer<WeatherEntry?> {
            override fun onChanged(taskEntry: WeatherEntry?) {
                if (taskEntry != null) {
                    viewModel.getWeather().removeObserver(this)
                    populateUI(taskEntry)
                }
            }
        })
    }

    fun populateUI(weatherEntry: WeatherEntry){
        val localDateMidnightGmt = weatherEntry.date.time
        val dateText = getFriendlyDateString(this, localDateMidnightGmt, true)
        mDateView.text = dateText

        val weatherId = weatherEntry.weather_id?:0
        val description = getStringForWeatherCondition(this, weatherId)
        mDescriptionView.text = description

        val highInCelsius = weatherEntry.max
        val highString = formatTemperature(this, highInCelsius)
        mHighTemperatureView.text = highString

        val lowInCelsius =  weatherEntry.min
        val lowString = formatTemperature(this, lowInCelsius)
        mLowTemperatureView.text = lowString

        val humidity = weatherEntry.humidity
        val humidityString = getString(R.string.format_humidity, humidity)
        mHumidityView.text = humidityString

        val windSpeed = weatherEntry.wind
        val windDirection = weatherEntry.degrees
        val windString = getFormattedWind(this, windSpeed, windDirection)
        mWindView.text = windString

        val pressure = weatherEntry.pressure
        val pressureString = getString(R.string.format_pressure, pressure)
        mPressureView.text = pressureString

        mForecastSummary = String.format("%s - %s - %s/%s", dateText, description, highString, lowString)
    }

    private fun createShareForecastIntent(): Intent {
        return ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(mForecastSummary?:"" + FORECAST_SHARE_HASHTAG)
            .intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
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
