package com.sharath070.accuweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("Units") units: String,
    ): Call<WeatherApp>

}