package com.delight.whatsweather.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.utils.ForegroundOnlyLocationService
import com.delight.whatsweather.utils.toText
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