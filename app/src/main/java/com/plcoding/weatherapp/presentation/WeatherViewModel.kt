package com.plcoding.weatherapp.presentation

import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val geocoder: Geocoder
): ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?. let { location ->

                when(val result = repository.gerWeatherData(
                    lat = location.latitude,
                    long = location.longitude
                )) {
                    is Resource.Success -> {

                        val address = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )[0].locality.toString()

                        val name = "wroclaw"

                        val loc = geocoder.getFromLocationName(name, 1)

                        Log.d("lokacja", "loadWeatherInfo: $loc")


                        state = state.copy(
                            weatherInfo = result.data,
                            address = address,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS"
                )
            }


        }
    }


}