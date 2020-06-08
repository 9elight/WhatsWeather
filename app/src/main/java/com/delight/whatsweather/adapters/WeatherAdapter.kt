package com.delight.whatsweather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delight.whatsweather.R
import com.delight.whatsweather.model.onecall.WeatherOneCall
//
//class WeatherAdapter: RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
//    lateinit var list: List<WeatherOneCall>
//
//    fun updateList(list: List<WeatherOneCall>){
//        this.list = list
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder = WeatherViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false)
//    )
//
//    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
//        holder.bind(list[position])
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//    inner class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        fun bind(weatherOneCall: WeatherOneCall) {
//            val temp: TextView = itemView.findViewById(R.id.item_temp)
//            val icon: ImageView = itemView.findViewById(R.id.item_icon)
//        }
//
//    }
//}


