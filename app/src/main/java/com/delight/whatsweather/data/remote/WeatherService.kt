package com.delight.whatsweather.data.remote


import com.delight.whatsweather.staticData.Constants.API_KEY
import com.delight.whatsweather.staticData.Constants.ONE_CALL
import com.delight.whatsweather.model.onecall.WeatherOneCall
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET(ONE_CALL)
    suspend fun oneCallWeather(
        @Query(value = "lat") lat: Double?,
        @Query(value = "lon") lon: Double?,
        @Query(value = "units") format: String?,
        @Query(value = "appid") key: String = API_KEY
    ) : WeatherOneCall

}