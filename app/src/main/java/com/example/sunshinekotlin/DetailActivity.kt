package com.example.sunshinekotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.databinding.ActivityDetailBinding
import com.example.sunshinekotlin.model.WeatherViewModel
import com.example.sunshinekotlin.model.WeatherViewModelFactory
import com.example.sunshinekotlin.utilities.SunshineDateUtils.getFriendlyDateString
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.formatTemperature
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getFormattedWind
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getStringForWeatherCondition


class DetailActivity : AppCompatActivity() {

    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"
    private var mForecastSummary: String? = null

    private lateinit var mDetailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

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
        val weatherId: Int = weatherEntry.weather_id

        val weatherImageId: Int = SunshineWeatherUtils.getArtResourceForWeatherCondition(weatherId)

        val localDateMidnightGmt = weatherEntry.date.time
        val dateText = getFriendlyDateString(this, localDateMidnightGmt, true)

        val description = getStringForWeatherCondition(this, weatherId)
        val descriptionA11y = getString(R.string.a11y_forecast, description)

        val highInCelsius = weatherEntry.max
        val highString = formatTemperature(this, highInCelsius)
        val highA11y = getString(R.string.a11y_high_temp, highString)

        val lowInCelsius =  weatherEntry.min
        val lowString = formatTemperature(this, lowInCelsius)
        val lowA11y = getString(R.string.a11y_low_temp, lowString)

        val humidity = weatherEntry.humidity
        val humidityString = getString(R.string.format_humidity, humidity)
        val humidityA11y = getString(R.string.a11y_humidity, humidityString)

        val windSpeed = weatherEntry.wind
        val windDirection = weatherEntry.degrees
        val windString = getFormattedWind(this, windSpeed, windDirection)
        val windA11y = getString(R.string.a11y_wind, windString)

        val pressure = weatherEntry.pressure
        val pressureString = getString(R.string.format_pressure, pressure)
        val pressureA11y = getString(R.string.a11y_pressure, pressureString)

        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId)
        mDetailBinding.primaryInfo.date.text = dateText
        mDetailBinding.primaryInfo.weatherDescription.text = description
        mDetailBinding.primaryInfo.weatherDescription.contentDescription = descriptionA11y
        mDetailBinding.primaryInfo.weatherIcon.contentDescription = descriptionA11y
        mDetailBinding.primaryInfo.highTemperature.text = highString
        mDetailBinding.primaryInfo.highTemperature.contentDescription = highA11y
        mDetailBinding.primaryInfo.lowTemperature.text = lowString
        mDetailBinding.primaryInfo.lowTemperature.contentDescription = lowA11y
        mDetailBinding.extraDetails.humidity.text = humidityString
        mDetailBinding.extraDetails.humidity.contentDescription = humidityA11y
        mDetailBinding.extraDetails.humidityLabel.contentDescription = humidityA11y
        mDetailBinding.extraDetails.windMeasurement.text = windString
        mDetailBinding.extraDetails.windMeasurement.contentDescription = windA11y
        mDetailBinding.extraDetails.windLabel.contentDescription = windA11y
        mDetailBinding.extraDetails.pressure.text = pressureString;
        mDetailBinding.extraDetails.pressure.contentDescription = pressureA11y;
        mDetailBinding.extraDetails.pressureLabel.contentDescription = pressureA11y;

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
