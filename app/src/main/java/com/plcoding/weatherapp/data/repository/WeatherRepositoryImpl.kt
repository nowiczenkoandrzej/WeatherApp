package com.plcoding.weatherapp.data.repository

import android.util.Log
import com.plcoding.weatherapp.data.local.WeatherDao
import com.plcoding.weatherapp.data.local.WeatherEntity
import com.plcoding.weatherapp.data.mappers.toWeatherEntity
import com.plcoding.weatherapp.data.mappers.toWeatherInfo
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
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

    override suspend fun getWeatherDataFromDataBase(): List<WeatherEntity> {
        return try {
            dao.getAll()
        } catch (e: Exception) {
            emptyList<WeatherEntity>()
        }
    }

    override suspend fun insertWeatherData(data: WeatherData) {

        dao.insertWeatherInfo(data.toWeatherEntity())
    }

}