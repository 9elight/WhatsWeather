package com.delight.whatsweather.views

import android.location.Location
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainActivityView: MvpView {
    fun showFirstScreenState(location: Location?)
    fun showSecondScreenState()
    fun showError(error: String)
    fun hideLoadingScreen()
}