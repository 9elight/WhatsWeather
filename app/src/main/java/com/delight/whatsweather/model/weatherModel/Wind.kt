package com.delight.whatsweather.model.weatherModel

import com.google.gson.annotations.SerializedName

data class Wind (

	@SerializedName("speed") val speed : Int,
	@SerializedName("deg") val deg : Int
)