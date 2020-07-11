package com.delight.whatsweather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delight.whatsweather.R
import com.delight.whatsweather.model.onecall.Daily
import com.delight.whatsweather.model.onecall.WeatherOneCall
import com.delight.whatsweather.utils.DateParser
import com.delight.whatsweather.utils.OnItemClickListener

class DailyWeatherAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<DailyWeatherAdapter.WeatherViewHolder>() {
    private lateinit var list: List<Daily>
    fun updateList(list: List<Daily>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder = WeatherViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false)
    )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(weatherOneCall: Daily,position: Int) {
            val day: TextView = itemView.findViewById(R.id.day_textView)
            val month: TextView = itemView.findViewById(R.id.month_textView)
            val date = DateParser.parse(weatherOneCall.dt)
            val dateParse = date.split(".")
            day.text = dateParse[0]
            month.text = dateParse[1]
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(position)
            }
        }

    }
}


