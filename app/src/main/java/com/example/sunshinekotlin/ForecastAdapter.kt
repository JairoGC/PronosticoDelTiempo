package com.example.sunshinekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.SunshineDateUtils.getFriendlyDateString
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.formatTemperature
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getStringForWeatherCondition


class ForecastAdapter(private val mContext: Context, val mClickHandler: ForecastAdapterOnClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>(){

    private var mWeatherData: List<WeatherEntry> = listOf()

    interface ForecastAdapterOnClickHandler {
        fun onClick(uid: Int?)
    }

    inner class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val dateView: TextView = itemView.findViewById(R.id.date)
        val descriptionView: TextView = itemView.findViewById(R.id.weather_description)
        val highTempView: TextView = itemView.findViewById(R.id.high_temperature)
        val lowTempView: TextView = itemView.findViewById(R.id.low_temperature)
        val iconView: ImageView = itemView.findViewById(R.id.weather_icon);

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

        val weatherId = weatherEntry.weather_id

        val weatherImageId = SunshineWeatherUtils
            .getIconResourceForWeatherCondition(weatherId)

        val dateInMillis: Long = weatherEntry.date.time
        val dateString = getFriendlyDateString(mContext, dateInMillis, false)

        val description =  getStringForWeatherCondition(mContext, weatherId)
        val descriptionA11y = mContext.getString(R.string.a11y_forecast, description)

        val highInCelsius: Double = weatherEntry.max
        val highString = formatTemperature(mContext, highInCelsius)
        val highA11y = mContext.getString(R.string.a11y_high_temp, highString)

        val lowInCelsius: Double = weatherEntry.min
        val lowString = formatTemperature(mContext, lowInCelsius)
        val lowA11y = mContext.getString(R.string.a11y_low_temp, lowString)

        holder.iconView.setImageResource(weatherImageId)
        holder.dateView.text = dateString
        holder.descriptionView.text = description
        holder.descriptionView.contentDescription = descriptionA11y
        holder.highTempView.text = highString
        holder.highTempView.contentDescription = highA11y
        holder.lowTempView.text = lowString
        holder.lowTempView.contentDescription = lowA11y

    }

    override fun getItemCount(): Int {
        return mWeatherData.size
    }

    fun setWeatherData(weatherData: List<WeatherEntry>) {
        mWeatherData = weatherData
        notifyDataSetChanged()
    }
}