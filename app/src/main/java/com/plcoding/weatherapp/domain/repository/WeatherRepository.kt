package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {

    suspend fun gerWeatherData(
        lat: Double,
        long: Double
    ): Resource<WeatherInfo>

}