package com.delight.whatsweather.converters

import androidx.room.TypeConverter
import com.delight.whatsweather.model.onecall.FeelsLike
import com.google.gson.Gson

class FeelsLikeTypeConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun feelsLikeToJson(feelsLike: FeelsLike): String{
            return Gson().toJson(feelsLike)
        }

        @TypeConverter
        @JvmStatic
        fun feelsLikeFromJson(json: String): FeelsLike{
            return Gson().fromJson(json, FeelsLike::class.java)
        }
    }


}