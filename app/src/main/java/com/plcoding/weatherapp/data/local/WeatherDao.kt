package com.plcoding.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weatherentity")
    fun getAll(): List<WeatherEntity>

    @Insert
    fun insertWeatherInfo(weatherEntity: WeatherEntity)

}