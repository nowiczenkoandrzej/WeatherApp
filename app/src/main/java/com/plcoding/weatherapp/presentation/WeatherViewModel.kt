package com.plcoding.weatherapp.presentation

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.data.local.WeatherDao
import com.plcoding.weatherapp.data.local.WeatherEntity
import com.plcoding.weatherapp.data.mappers.toWeatherEntity
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
): ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState = _weatherState.asStateFlow()

    private val _weatherList = MutableStateFlow<List<WeatherEntity>>(emptyList())
    val weatherList = _weatherList.asStateFlow()


    fun loadWeatherInfo() {
        viewModelScope.launch {

            _weatherState.value = WeatherState(
                isLoading = true,
                error = null
            )

            locationTracker.getCurrentLocation()?. let { location ->

                val result = repository.gerWeatherData(
                    lat = location.latitude,
                    long = location.longitude
                )

                when(result) {
                    is Resource.Success -> {


                        _weatherState.value = WeatherState(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _weatherState.value = WeatherState(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                _weatherState.value = WeatherState(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS"
                )
            }
        }

    }

    fun saveWeatherInfo() {

        viewModelScope.launch {
            val weather = weatherState.value.weatherInfo
            if(weather == null)
                return@launch

            repository.insertWeatherData(weather.currentWeatherData!!)
            getSavedWeatherInfo()
        }




    }

    fun getSavedWeatherInfo() {

        viewModelScope.launch {
            _weatherList.value = repository.getWeatherDataFromDataBase()
        }


    }




}