package com.delight.whatsweather.views

import com.delight.whatsweather.model.onecall.WeatherOneCall
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainActivityView: MvpView {
    fun openWeatherFragment()
    fun openDetailFragment()
    fun closeDetailFragment()
}