package com.delight.whatsweather.presenter

import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.views.WeatherView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class WeatherPresenter(private val weatherRepositories: WeatherRepositories) :
    MvpPresenter<WeatherView>() {

    suspend fun loadWeather(city: String, format: String) {
        viewState.startWeatherLoading()
        val currentWeather = weatherRepositories.getWeather(city, format)
        val oneCall = weatherRepositories.getOneCallWeather(60.9,30.9,"ru","metric")
        weatherRepositories.saveWeatherInDatabase(oneCall)
        viewState.endWeatherLoading()
        viewState.showWeather(oneCall)
    }

}