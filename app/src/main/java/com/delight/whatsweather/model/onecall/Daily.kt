package com.delight.whatsweather.model.onecall

import androidx.room.TypeConverters
import com.delight.whatsweather.converters.FeelsLikeTypeConverter
import com.delight.whatsweather.converters.WeatherTypeConverter
import com.delight.whatsweather.model.onecall.Temp
import com.delight.whatsweather.model.onecall.Weather
import com.google.gson.annotations.SerializedName

@TypeConverters(FeelsLikeTypeConverter::class,WeatherTypeConverter::class)
data class Daily (

	@SerializedName("dt") val dt : Int,
	@SerializedName("sunrise") val sunrise : Int,
	@SerializedName("sunset") val sunset : Int,
	@SerializedName("temp") val temp : Temp,
	@SerializedName("feels_like") val feels_like: FeelsLike,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int,
	@SerializedName("dew_point") val dew_point : Double,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_deg") val wind_deg : Int,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("clouds") val clouds : Int,
	@SerializedName("uvi") val uvi : Double
)