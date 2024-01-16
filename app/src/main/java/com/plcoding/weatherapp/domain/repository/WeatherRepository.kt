package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.data.local.WeatherEntity
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {

    suspend fun gerWeatherData(
        lat: Double,
        long: Double
    ): Resource<WeatherInfo>

    suspend fun getWeatherDataFromDataBase() : List<WeatherEntity>

    suspend fun insertWeatherData(data: WeatherData)

}