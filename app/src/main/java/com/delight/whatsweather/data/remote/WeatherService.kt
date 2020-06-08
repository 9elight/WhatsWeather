package com.delight.whatsweather.data.remote


import com.delight.whatsweather.model.weatherModel.CurrentWeather
import com.delight.whatsweather.staticData.ApiConstants.API_KEY
import com.delight.whatsweather.staticData.ApiConstants.CURRENT_WEATHER
import com.delight.whatsweather.staticData.ApiConstants.ONE_CALL
import com.delight.whatsweather.model.onecall.WeatherOneCall
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(CURRENT_WEATHER)
    suspend fun currentWeatherAsync(
        @Query("q") city: String?,
        @Query("units") format: String?,
        @Query("appid") key: String = API_KEY
    ): CurrentWeather

    @GET(ONE_CALL)
    suspend fun oneCallWeather(
        @Query(value = "lat") lat: Double?,
        @Query(value = "lon") lon: Double?,
        @Query(value = "units") format: String?,
        @Query(value = "lang") lang: String?,
        @Query(value = "appid") key: String = API_KEY
    ) : WeatherOneCall

}