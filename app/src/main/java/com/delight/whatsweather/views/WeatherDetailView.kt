package com.delight.whatsweather.views

import com.delight.whatsweather.model.onecall.Current
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface WeatherDetailView: MvpView {
    fun loadDetail()
    fun detailLoaded()
    fun showError()
    fun showDetail(current: Current)
}