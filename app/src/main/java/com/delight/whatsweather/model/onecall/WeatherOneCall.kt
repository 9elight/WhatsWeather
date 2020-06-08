package com.delight.whatsweather.model.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.delight.whatsweather.converters.CurrentTypeConverter
import com.delight.whatsweather.converters.DailyTypeConverter
import com.delight.whatsweather.converters.HourlyTypeConverter
import com.delight.whatsweather.converters.WeatherTypeConverter
import com.google.gson.annotations.SerializedName
@TypeConverters(CurrentTypeConverter::class,HourlyTypeConverter::class,DailyTypeConverter::class)
@Entity(tableName = "oneCallTable")
data class WeatherOneCall (
	@PrimaryKey(autoGenerate = true) val id: Int,

	@SerializedName("lat") val lat : Double,

	@SerializedName("lon") val lon : Double,

	@SerializedName("timezone") val timezone : String,

	@SerializedName("current") val current : Current,

	@SerializedName("hourly") val hourly : List<Hourly>,

	@SerializedName("daily") val daily : List<Daily>
)