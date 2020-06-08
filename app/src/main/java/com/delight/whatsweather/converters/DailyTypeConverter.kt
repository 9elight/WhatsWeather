package com.delight.whatsweather.converters

import androidx.room.TypeConverter
import com.delight.whatsweather.model.onecall.Daily
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DailyTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun dailyToJson(listDaily: List<Daily>): String{
            return Gson().toJson(listDaily)
        }

        @TypeConverter
        @JvmStatic
        fun dailyFromJson(dailyJson: String): List<Daily>{
            val listType = object : TypeToken<List<Daily>>(){}.type
            return Gson().fromJson(dailyJson, listType)
        }
    }
}