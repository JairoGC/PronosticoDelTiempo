package com.example.sunshinekotlin.utilities

import android.content.Context
import android.text.format.DateUtils
import com.example.sunshinekotlin.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object SunshineDateUtils{

    const val SECOND_IN_MILLIS: Long = 1000
    const val MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60
    const val HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60
    const val DAY_IN_MILLIS = HOUR_IN_MILLIS * 24


    fun getNormalizedUtcDateForToday(): Long {
        val utcNowMillis = System.currentTimeMillis()
        val currentTimeZone = TimeZone.getDefault()
        val gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis).toLong()

        val timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis
        val daysSinceEpochLocal: Long = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis)

        return TimeUnit.DAYS.toMillis(daysSinceEpochLocal)
    }

    fun getDayNumber(date: Long): Long {
        val tz = TimeZone.getDefault()
        val gmtOffset = tz.getOffset(date)
        return (date.plus(gmtOffset)).div(DAY_IN_MILLIS)
    }

    fun normalizeDate(date: Long): Long {
        return date.div(DAY_IN_MILLIS).times(DAY_IN_MILLIS)
    }

    fun getLocalDateFromUTC(utcDate: Long): Long {
        val tz = TimeZone.getDefault()
        val gmtOffset = tz.getOffset(utcDate)
        return utcDate.minus(gmtOffset)
    }

    fun getUTCDateFromLocal(localDate: Long): Long {
        val tz = TimeZone.getDefault()
        val gmtOffset = tz.getOffset(localDate)
        return localDate.plus(gmtOffset)
    }

    fun getFriendlyDateString(context: Context, dateInMillis: Long, showFullDate: Boolean): String {

        val localDate = getLocalDateFromUTC(dateInMillis)
        val dayNumber = getDayNumber(localDate)
        val currentDayNumber = getDayNumber(System.currentTimeMillis())

        if (dayNumber == currentDayNumber || showFullDate) {
            val dayName = getDayName(context, localDate)
            val readableDate = getReadableDateString(context, localDate)
            if (dayNumber.minus(currentDayNumber) < 2) {

                val localizedDayName = SimpleDateFormat("EEEE").format(localDate)
                return readableDate.replace(localizedDayName, dayName)
            } else {
                return readableDate
            }
        } else if (dayNumber < currentDayNumber + 7) {
            return getDayName(context, localDate)
        } else {
            val flags = (DateUtils.FORMAT_SHOW_DATE
                    or DateUtils.FORMAT_NO_YEAR
                    or DateUtils.FORMAT_ABBREV_ALL
                    or DateUtils.FORMAT_SHOW_WEEKDAY)

            return DateUtils.formatDateTime(context, localDate, flags)
        }
    }

    private fun getReadableDateString(context: Context, timeInMillis: Long): String {
        val flags = (DateUtils.FORMAT_SHOW_DATE
                or DateUtils.FORMAT_NO_YEAR
                or DateUtils.FORMAT_SHOW_WEEKDAY)

        return DateUtils.formatDateTime(context, timeInMillis, flags)
    }

    private fun getDayName(context: Context, dateInMillis: Long): String {
        val dayNumber = getDayNumber(dateInMillis)
        val currentDayNumber = getDayNumber(System.currentTimeMillis())
        if (dayNumber == currentDayNumber) {
            return context.getString(R.string.today)
        } else if (dayNumber == currentDayNumber.plus(1)) {
            return context.getString(R.string.tomorrow)
        } else {
            val dayFormat = SimpleDateFormat("EEEE")
            return dayFormat.format(dateInMillis)
        }
    }
}