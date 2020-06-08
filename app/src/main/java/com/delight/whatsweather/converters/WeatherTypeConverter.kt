package com.delight.whatsweather.converters

import androidx.room.TypeConverter
import com.delight.whatsweather.model.onecall.Hourly
import com.delight.whatsweather.model.onecall.Weather
import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherTypeConverter{
    companion object {
        @TypeConverter
        @JvmStatic
        fun weatherToJson(weatherList: List<Weather>): String {
            return Gson().toJson(weatherList)
        }

        @TypeConverter
        @JvmStatic
        fun weatherFromJson(weatherJson: String): List<Weather> {
            val listType = object : TypeToken<List<Weather>>() {}.type
            return Gson().fromJson(weatherJson, listType)
        }
    }
}