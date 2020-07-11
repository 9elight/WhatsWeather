package com.delight.whatsweather.presenter

import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.model.onecall.Current
import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.delight.whatsweather.views.MainActivityView
import moxy.InjectViewState
import moxy.MvpPresenter
import java.text.FieldPosition

@InjectViewState
class MainPresenter(
    private val weatherRepositories: WeatherRepositories
) : MvpPresenter<MainActivityView>() {

    suspend fun loadWeather(lat: Double, lon: Double, format: String) {
        viewState.startWeatherLoading()
        val remoteWeather = weatherRepositories.getOneCallWeather(lat, lon, format)
        weatherRepositories.saveWeatherInDatabase(remoteWeather)
        viewState.endWeatherLoading()
    }


    suspend fun showLastWeather(position: Int?) {
        val localWeather = weatherRepositories.getWeatherFromDb().last()
        if (position == null){
            viewState.showWeather(localWeather.daily[0],localWeather.timezone.split("/")[1])
        }else{
            viewState.showWeather(localWeather.daily[position],localWeather.timezone.split("/")[1])
        }
    }

    suspend fun setDailyRecycler() {
        viewState.setDailyRecycler(weatherRepositories.getWeatherFromDb().last().daily)
    }



}