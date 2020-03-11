package com.example.sunshinekotlin.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sunshinekotlin.DetailActivity
import com.example.sunshinekotlin.R
import com.example.sunshinekotlin.data.AppDatabase
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.data.WeatherEntry
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.formatTemperature
import com.example.sunshinekotlin.utilities.SunshineWeatherUtils.getStringForWeatherCondition
import java.util.*


object NotificationUtils {

    const val WEATHER_REMINDER_NOTIFICATION_CHANNEL_ID = "weather_notification_channel"
    const val WEATHER_REMINDER_PENDING_INTENT_ID = 3417
    const val WEATHER_NOTIFICATION_ID: Int = 3004

    fun notifyUserOfNewWeather(context: Context){

        val weatherEntry : WeatherEntry? = this.weatherToday(context)
        if(weatherEntry != null){
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    WEATHER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(mChannel)
            }

            val weatherId = weatherEntry.weather_id
            val notificationText: String = this.getNotificationText(context, weatherId, weatherEntry.max, weatherEntry.min)

            val notificationBuilder =
                NotificationCompat.Builder(context,WEATHER_REMINDER_NOTIFICATION_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(this.getSmallIcon(weatherId))
                    .setLargeIcon(this.getLargeIcon(context, weatherId))
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(notificationText)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent(context,weatherEntry.uid))
                    .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ) {
                notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
            }
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build())
            SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis())
        }else{
            Log.w("NotificationUtils","pronostico de hoy no encontrado")
        }
    }

    private fun weatherToday(context: Context): WeatherEntry? {
        val date = SunshineDateUtils.getNormalizedDateForToday()
        val database = AppDatabase.getInstance(context)
        return database.weatherDao().loadWeatherByDate(date)
    }

    private fun contentIntent(context: Context, weatherId: Int?): PendingIntent? {
        val detailIntentForToday = Intent(context, DetailActivity::class.java)
        detailIntentForToday.putExtra(Intent.EXTRA_TEXT, weatherId)

        return PendingIntent.getActivity(
            context,
            WEATHER_REMINDER_PENDING_INTENT_ID,
            detailIntentForToday,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getLargeIcon(context: Context, weatherId: Int): Bitmap? {
        val largeArtResourceId: Int = SunshineWeatherUtils
            .getArtResourceForWeatherCondition(weatherId)
        return BitmapFactory.decodeResource(
            context.resources,
            largeArtResourceId
        )
    }

    private fun getSmallIcon(weatherId: Int): Int {
        return SunshineWeatherUtils.getIconResourceForWeatherCondition(weatherId)
    }

    private fun getNotificationText(context: Context, weatherId: Int, high: Double, low: Double): String {
        val shortDescription = getStringForWeatherCondition(context, weatherId)
        val notificationFormat = context.getString(R.string.format_notification)

        return String.format(
            notificationFormat,
            shortDescription,
            formatTemperature(context, high),
            formatTemperature(context, low)
        )
    }
}