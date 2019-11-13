package com.example.sunshinekotlin

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

class ForecastAdapter(val mClickHandler: ForecastAdapterOnClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>(){

    private var mWeatherData: List<String>? = null

    interface ForecastAdapterOnClickHandler {
        fun onClick(weatherForDay: String)
    }

    inner class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var mWeatherTextView: TextView = itemView.findViewById(R.id.tv_weather_data)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val adapterPosition = adapterPosition
            val weatherForDay = mWeatherData?.get(adapterPosition)?:""
            mClickHandler.onClick(weatherForDay)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapterViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.forecast_list_item
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)
        return ForecastAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastAdapterViewHolder, position: Int) {
        val weatherForThisDay = mWeatherData?.get(position)
        holder.mWeatherTextView.text = weatherForThisDay
    }

    override fun getItemCount(): Int {
        return mWeatherData?.size?:0
    }

    fun setWeatherData(weatherData: List<String>) {
        mWeatherData = weatherData
        notifyDataSetChanged()
    }
}