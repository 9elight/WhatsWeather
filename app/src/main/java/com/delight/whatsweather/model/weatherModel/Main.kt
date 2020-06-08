package com.delight.whatsweather.model.weatherModel

import com.google.gson.annotations.SerializedName


data class Main (

	@SerializedName("temp") val temp : Int,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("temp_min") val temp_min : Int,
	@SerializedName("temp_max") val temp_max : Int,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int
)