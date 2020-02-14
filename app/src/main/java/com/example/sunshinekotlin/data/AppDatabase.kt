package com.example.sunshinekotlin.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [WeatherEntry::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase(){

    companion object {

        private val LOG_TAG = AppDatabase::class.java.simpleName
        private const val DATABASE_NAME = "weather"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS $DATABASE_NAME")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                Log.d(LOG_TAG, "Creating new database instance")

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                Log.d(LOG_TAG, "Getting the database instance")
                return instance
            }
        }
    }

    abstract fun weatherDao(): WeatherDao

}