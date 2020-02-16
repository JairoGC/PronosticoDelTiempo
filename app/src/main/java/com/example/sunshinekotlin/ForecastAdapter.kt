package com.example.sunshinekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.SunshineDateUtils.getFriendlyDateString
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.formatHighLows
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getStringForWeatherCondition


class ForecastAdapter(private val mContext: Context, val mClickHandler: ForecastAdapterOnClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>(){

    private var mWeatherData: List<WeatherEntry> = listOf()

    interface ForecastAdapterOnClickHandler {
        fun onClick(uid: Int?)
    }

    inner class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var mWeatherTextView: TextView = itemView.findViewById(R.id.tv_weather_data)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val weatherEntry = mWeatherData[adapterPosition]
            mClickHandler.onClick(weatherEntry.uid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(R.layout.forecast_list_item, parent, shouldAttachToParentImmediately)
        return ForecastAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastAdapterViewHolder, position: Int) {
        val weatherEntry = mWeatherData[position]
        holder.mWeatherTextView.text = weatherEntry.date.toString()

        val weatherId = weatherEntry.weather_id?:0
        val dateInMillis: Long = weatherEntry.date.time
        val dateString = getFriendlyDateString(mContext, dateInMillis, false)
        val description = getStringForWeatherCondition(mContext, weatherId)
        val highInCelsius: Double = weatherEntry.max
        val lowInCelsius: Double = weatherEntry.min

        val highAndLowTemperature = formatHighLows(mContext, highInCelsius, lowInCelsius)
        val weatherSummary = "$dateString - $description - $highAndLowTemperature"

        holder.mWeatherTextView.text = weatherSummary
    }

    override fun getItemCount(): Int {
        return mWeatherData.size
    }

    fun setWeatherData(weatherData: List<WeatherEntry>) {
        mWeatherData = weatherData
        notifyDataSetChanged()
    }
}