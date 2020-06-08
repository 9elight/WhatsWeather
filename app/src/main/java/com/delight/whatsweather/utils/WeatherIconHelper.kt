package com.delight.whatsweather.utils

import com.delight.whatsweather.R

class WeatherIconHelper {
    companion object {
         fun get(iconId: String): Int {
            var id: Int = 0
            when (iconId) {
                "01d" -> id = R.drawable.ic_sun
                "01n" -> id = R.drawable.ic_moon
                "02d" -> id = R.drawable.ic_fewcloudsday
                "02n" -> id = R.drawable.ic_fewcloudsnight
                "03d" -> id = R.drawable.ic_cloud
                "03n" -> id = R.drawable.ic_cloud
                "04d" -> id = R.drawable.ic_broken_clouds
                "04n" -> id = R.drawable.ic_broken_clouds
                "09d" -> id = R.drawable.ic_showerrain
                "09n" -> id = R.drawable.ic_showerrain
                "10d" -> id = R.drawable.ic_rainday
                "10n" -> id = R.drawable.ic_rainnight
                "11n",
                "11d" -> id = R.drawable.ic_thunderstorm
                "13d",
                "13n" -> id = R.drawable.ic_snow
                "50d",
                "50n" -> id = R.drawable.ic_mist
            }
            return id
        }
    }
}