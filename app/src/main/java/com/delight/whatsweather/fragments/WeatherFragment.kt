package com.delight.whatsweather.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


import com.delight.whatsweather.R
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.delight.whatsweather.presenter.WeatherPresenter
import com.delight.whatsweather.utils.DateParser
import com.delight.whatsweather.utils.LocationUtils
import com.delight.whatsweather.utils.WeatherIconHelper
import com.delight.whatsweather.views.WeatherView
import kotlinx.android.synthetic.main.fragment_main_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class WeatherFragment : MvpAppCompatFragment(), WeatherView,CoroutineScope {
    private val weatherRepositories: WeatherRepositories by inject()
    @InjectPresenter
    lateinit var weatherPresenter: WeatherPresenter
    @ProvidePresenter
    fun provideWeatherPresenter(): WeatherPresenter{
        return WeatherPresenter(weatherRepositories = weatherRepositories)
    }
    companion object{
        fun instance(location: Location?): WeatherFragment = WeatherFragment().apply{
            this.arguments = Bundle().apply {
                putString("lat",location?.latitude.toString())
                putString("lat",location?.longitude.toString())
            }
        }
    }
    private lateinit var mView: View
    private var location: Location? = null
    private lateinit var locationUtils: LocationUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lat:Location? = this.arguments?.getParcelable("lat")
        launch {
            weatherPresenter.loadWeather("Bishkek","metric")

        }
        mView = inflater.inflate(R.layout.fragment_main_weather, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("tag","")

    }

//    fun loadWeather(location: Location){
//        Log.e("tag","")
//        launch {
//            weatherPresenter.loadWeather("Bishkek","metric")
//        }
//        this.location = location
//    }
    override fun startWeatherLoading() {
        txt_date.visibility = View.INVISIBLE
        txt_description.visibility = View.INVISIBLE
        txt_temp.visibility = View.INVISIBLE
        txt_city.visibility = View.INVISIBLE
        weather_icon.visibility = View.INVISIBLE
        progress_circular.visibility = View.VISIBLE
    }

    override fun endWeatherLoading() {
        txt_date.visibility = View.VISIBLE
        txt_description.visibility = View.VISIBLE
        txt_temp.visibility = View.VISIBLE
        txt_city.visibility = View.VISIBLE
        weather_icon.visibility = View.VISIBLE
        progress_circular.visibility = View.INVISIBLE
    }

    override fun showWeather(weather: WeatherOneCall) {
        val temp = weather.current.temp.toString() + "°С"
        txt_date.text = weather.current.dt.let { DateParser.parse(it) }
        txt_city.text = weather.timezone
        weather_icon.setImageResource(WeatherIconHelper.get(weather.current.weather[0].icon))
        txt_temp.text = temp
        txt_description.text = weather.current.weather[0].description

    }

    override fun showError(massage: String?) {
        Toast.makeText(context,"showError", Toast.LENGTH_LONG).show()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    }



