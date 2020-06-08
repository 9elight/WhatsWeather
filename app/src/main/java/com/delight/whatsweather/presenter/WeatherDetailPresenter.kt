package com.delight.whatsweather.presenter

import android.util.Log
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.views.WeatherDetailView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class WeatherDetailPresenter(private val weatherRepositories: WeatherRepositories):
    MvpPresenter<WeatherDetailView>(){

    suspend fun fetchWeather(){
        val oneCall = weatherRepositories.getWeatherFromDb().last()
        viewState.showDetail(oneCall.current)
        Log.e("tag","weather")
    }
}