package com.delight.whatsweather.utils

import com.delight.whatsweather.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartHelper{
    companion object{
        fun getLineData(): LineData{
            val entries: ArrayList<Entry> = ArrayList()
            entries.add(Entry(1.0f,2.20f))
            entries.add(Entry(12.0f,5.0f))
            entries.add(Entry(11.0f,16.0f))
            entries.add(Entry(17.0f,10.0f))
            entries.add(Entry(6.0f,6.22f))
            entries.add(Entry(9.0f,8.0f))
            val dataSet: LineDataSet = LineDataSet(entries,"Label")
            dataSet.color = R.color.colorAccent
            dataSet.valueTextColor = R.color.colorPrimaryDark
            return LineData(dataSet)

        }
    }
}