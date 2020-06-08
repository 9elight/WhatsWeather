package com.delight.whatsweather.model.onecall

import androidx.room.TypeConverters
import com.delight.whatsweather.converters.WeatherTypeConverter
import com.google.gson.annotations.SerializedName


@TypeConverters(WeatherTypeConverter::class)
data class Current (

	@SerializedName("dt") val dt : Int,
	@SerializedName("sunrise") val sunrise : Int,
	@SerializedName("sunset") val sunset : Int,
	@SerializedName("temp") val temp : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("pressure") val pressure : Double,
	@SerializedName("humidity") val humidity : Double,
	@SerializedName("dew_point") val dew_point : Double,
	@SerializedName("uvi") val uvi : Double,
	@SerializedName("clouds") val clouds : Int,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_deg") val wind_deg : Double,
	@SerializedName("weather") val weather : List<Weather>
)