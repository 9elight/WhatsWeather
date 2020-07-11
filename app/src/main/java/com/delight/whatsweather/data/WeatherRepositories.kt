package com.delight.whatsweather.data

import com.delight.whatsweather.data.local.WeatherRoomDb
import com.delight.whatsweather.data.remote.WeatherService
import com.delight.whatsweather.model.onecall.WeatherOneCall


class WeatherRepositories(private val weatherService: WeatherService, private val weatherDatabase: WeatherRoomDb) {

    suspend fun getOneCallWeather(lat: Double,lon:Double,format: String): WeatherOneCall {
        return weatherService.oneCallWeather(lat, lon, format)
    }

    suspend fun saveWeatherInDatabase(weather: WeatherOneCall){
        if (weatherDatabase.weatherDao().getAllWeather().size > 10){
            weatherDatabase.weatherDao().deleteAll()
        }
        weatherDatabase.weatherDao().saveWeather(weather)
    }

    suspend fun getWeatherFromDb(): List<WeatherOneCall>{
        return weatherDatabase.weatherDao().getAllWeather()
    }
}