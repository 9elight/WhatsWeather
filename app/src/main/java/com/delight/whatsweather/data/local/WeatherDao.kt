package com.delight.whatsweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.delight.whatsweather.model.onecall.WeatherOneCall

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weather: WeatherOneCall)

    @Query("DELETE FROM oneCallTable")
    suspend fun deleteAll()

    @Query("SELECT * FROM oneCallTable")
    suspend fun getAllWeather(): List<WeatherOneCall>

}