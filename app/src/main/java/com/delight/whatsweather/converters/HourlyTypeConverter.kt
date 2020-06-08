package com.delight.whatsweather.converters

import androidx.room.TypeConverter
import com.delight.whatsweather.model.onecall.Daily
import com.delight.whatsweather.model.onecall.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyTypeConverter{
    companion object{
        @TypeConverter
        @JvmStatic
        fun hourlyToJson(listHourly: List<Hourly>): String{
            return Gson().toJson(listHourly)
        }

        @TypeConverter
        @JvmStatic
        fun hourlyFromJson(hourlyJson: String): List<Hourly>{
            val listType = object : TypeToken<List<Hourly>>(){}.type

            return Gson().fromJson(hourlyJson,listType)
        }
    }
}