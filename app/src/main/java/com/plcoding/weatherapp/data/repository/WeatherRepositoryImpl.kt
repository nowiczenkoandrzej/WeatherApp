package com.plcoding.weatherapp.data.repository

import android.util.Log
import com.plcoding.weatherapp.data.mappers.toWeatherInfo
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun gerWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            val result = api.getWeatherData(
                lat = lat,
                long = long
            ).toWeatherInfo()

            Resource.Success(
                data = result
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

}