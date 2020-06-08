package com.delight.whatsweather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.delight.whatsweather.R
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.model.onecall.Current
import com.delight.whatsweather.presenter.WeatherDetailPresenter
import com.delight.whatsweather.views.WeatherDetailView
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class DetailFragment : MvpAppCompatFragment(),WeatherDetailView, CoroutineScope {
    private val weatherRepositories: WeatherRepositories by inject()

    @InjectPresenter
    lateinit var detailPresenter: WeatherDetailPresenter
    @ProvidePresenter
    fun provideDetailPresenter(): WeatherDetailPresenter{
        return WeatherDetailPresenter(weatherRepositories = weatherRepositories)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {  detailPresenter.fetchWeather() }
    }
    companion object{
        fun instance(): DetailFragment{
            return DetailFragment()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun loadDetail() {
        TODO("Not yet implemented")
    }

    override fun detailLoaded() {
        TODO("Not yet implemented")
    }

    override fun showDetail(current: Current) {
        detail_pressure_value.text = current.pressure.toString()
        detail_humidity_value.text = current.humidity.toString()
        detail_uvi_value.text = current.uvi.toString()
        detail_wind_speed_value.text = current.wind_speed.toString()
        detail_dew_point_value.text = current.dew_point.toString()
        detail_clouds_value.text = current.clouds.toString()
        detail_sunrise_value.text = current.sunrise.toString()
        detail_sunset_value.text = current.sunset.toString()
    }

    override fun showError() {
        TODO("Not yet implemented")
    }

}
