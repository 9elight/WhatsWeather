package com.delight.whatsweather.presenter

import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.utils.LocationUtils
import com.delight.whatsweather.views.MainActivityView
import moxy.MvpPresenter

class MainPresenter(
    private val weatherRepositories: WeatherRepositories
) : MvpPresenter<MainActivityView>(){


}