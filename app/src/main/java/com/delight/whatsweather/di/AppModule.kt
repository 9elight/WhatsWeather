package com.delight.whatsweather.di

import com.delight.whatsweather.data.local.WeatherRoomDb
import com.delight.whatsweather.staticData.ApiConstants.BASE_URL
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.data.remote.RetrofitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { RetrofitFactory.instateRetrofit(BASE_URL) }

    single { WeatherRoomDb.getDataBase(context = androidContext()) }

    single { WeatherRepositories(get(), get()) }


}