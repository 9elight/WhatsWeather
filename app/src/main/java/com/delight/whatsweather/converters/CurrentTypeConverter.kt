package com.delight.whatsweather.converters

import androidx.room.TypeConverter
import com.delight.whatsweather.model.onecall.Current
import com.google.gson.Gson

class CurrentTypeConverter{
    companion object{
        @TypeConverter
        @JvmStatic
        fun currentToJson(current: Current): String{
            return Gson().toJson(current)
        }

        @TypeConverter
        @JvmStatic
        fun currentFromJson(currentJson: String): Current{
            return Gson().fromJson(currentJson, Current::class.java)
        }
    }
}
