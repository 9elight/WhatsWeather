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
        val oneCall = weatherRepositories.getOneCallWeather(40.0,79.0,"ru",format)
        weatherRepositories.saveWeatherInDatabase(oneCall)
        viewState.endWeatherLoading()
        viewState.showWeather(weather = oneCall)
    }





}