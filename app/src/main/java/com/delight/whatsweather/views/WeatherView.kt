package com.delight.whatsweather.views

import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.delight.whatsweather.model.weatherModel.CurrentWeather
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface WeatherView: MvpView{
    fun startWeatherLoading()
    fun endWeatherLoading()
    fun showWeather(weather: WeatherOneCall?)
    fun showError(massage: String?)
}