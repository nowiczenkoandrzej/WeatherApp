package com.plcoding.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.data.mappers.toWeatherData
import com.plcoding.weatherapp.presentation.components.WeatherCard
import com.plcoding.weatherapp.presentation.components.WeatherForecast
import com.plcoding.weatherapp.presentation.ui.theme.DarkBlue
import com.plcoding.weatherapp.presentation.ui.theme.DeepBlue
import com.plcoding.weatherapp.presentation.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo()
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))


        viewModel.getSavedWeatherInfo()

        setContent {
            WeatherAppTheme {

                val state by viewModel.weatherState.collectAsState()

                val savedWeather = viewModel.weatherList.collectAsState().value

                Column(
                    modifier = Modifier
                        .background(DarkBlue)
                        //.clickable { focusManager.clearFocus() }
                ) {

                    LazyColumn {
                        item {

                            state.weatherInfo?.currentWeatherData?.let {
                                WeatherCard(
                                    state = state.weatherInfo?.currentWeatherData!!,
                                    backgroundColor = DeepBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            WeatherForecast(state = state)

                            if(state.isLoading) {
                                CircularProgressIndicator(
                                    //modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            state.error?.let { error ->
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    //modifier = Modifier.align(Alignment.Center)

                                )
                            }
                            Button(
                                onClick = { viewModel.saveWeatherInfo() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(text = "Save Weather Info")
                            }

                            Text(
                                text = "Saved Weather: ",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )


                        }



                        items(savedWeather) {
                            val weatherInfo = it.toWeatherData()


                            WeatherCard(
                                state = weatherInfo,
                                backgroundColor = DeepBlue
                            )
                        }
                    }

                    /*viewModel.weatherList.value.forEach {
                        val weatherInfo = it.toWeatherData()

                        WeatherCard(
                            state = weatherInfo,
                            backgroundColor = DeepBlue
                        )
                    }*/

                }







            }
        }
    }
}

