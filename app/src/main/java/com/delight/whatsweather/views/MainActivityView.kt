package com.delight.whatsweather.views

import android.location.Location
import com.delight.whatsweather.model.onecall.Current
import com.delight.whatsweather.model.onecall.Daily
import com.delight.whatsweather.model.onecall.WeatherOneCall
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainActivityView: MvpView {
    fun showDetailFragment()
    fun startWeatherLoading()
    fun endWeatherLoading()
    fun  showWeather(daily: Daily,city:String)
    fun showError(massage: String?)
    fun setDailyRecycler(list:List<Daily>)
}