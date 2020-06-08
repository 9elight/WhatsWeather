package com.delight.whatsweather.data

import com.delight.whatsweather.data.local.WeatherRoomDb
import com.delight.whatsweather.data.remote.WeatherService
import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.delight.whatsweather.model.weatherModel.CurrentWeather


class WeatherRepositories(private val weatherService: WeatherService, private val weatherDatabase: WeatherRoomDb) {
    suspend fun getWeather(city:String,format: String) : CurrentWeather {
        return weatherService.currentWeatherAsync(city, format)
    }
    suspend fun getOneCallWeather(lat: Double,lon:Double,lang: String,format: String): WeatherOneCall {
        return weatherService.oneCallWeather(lat, lon, format, lang)
    }

    suspend fun saveWeatherInDatabase(weather: WeatherOneCall){
        weatherDatabase.weatherDao().saveWeather(weather)
    }

    suspend fun getWeatherFromDb(): List<WeatherOneCall>{
        return weatherDatabase.weatherDao().getAllWeather()
    }
}