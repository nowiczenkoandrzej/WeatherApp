package com.plcoding.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "temperature_2m") val temperature: Double,
    @ColumnInfo(name = "weather_desc") val weatherDescription: String,
    @ColumnInfo(name = "pressure_msl") val pressure: Double,
    @ColumnInfo(name = "windspeed_10m") val windSpeed: Double,
    @ColumnInfo(name = "relativehumidity_2m") val humidity: Double
)