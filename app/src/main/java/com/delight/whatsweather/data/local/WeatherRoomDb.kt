package com.delight.whatsweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.delight.whatsweather.converters.WeatherTypeConverter
import com.delight.whatsweather.model.onecall.WeatherOneCall

@Database(entities = [WeatherOneCall::class],version = 1,exportSchema = false)
abstract class WeatherRoomDb: RoomDatabase(){
    abstract fun weatherDao(): WeatherDao

    companion object{
        fun getDataBase(context: Context): WeatherRoomDb{
            return Room.databaseBuilder(context.applicationContext,WeatherRoomDb::class.java,"weather_database")
                .build()
        }
    }
}