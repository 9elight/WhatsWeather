package com.delight.whatsweather.utils

import java.util.*

class DateParser {
    companion object{
        fun parse(uDate: Int): String{
            val sdf = java.text.SimpleDateFormat("dd.MMM", Locale.getDefault())
            val date = java.util.Date(uDate.toLong() * 1000)
            return sdf.format(date)
        }
    }
}