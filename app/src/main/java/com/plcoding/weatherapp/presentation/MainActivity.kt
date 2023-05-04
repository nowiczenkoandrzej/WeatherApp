package com.plcoding.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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



        setContent {
            WeatherAppTheme {
                val focusManager = LocalFocusManager.current


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBlue)
                        //.clickable { focusManager.clearFocus() }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        val customColors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = DeepBlue,
                            unfocusedBorderColor = DarkBlue,
                            backgroundColor = DarkBlue,
                            textColor = Color.White
                        )
                        val focusManager  = LocalFocusManager.current
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            colors = customColors,
                            label = { Text(text = "Find weather in your city") },
                            modifier = Modifier
                                .weight(1F)
                                .clickable { focusManager.clearFocus() }
                        )
                        IconButton(
                            onClick = {},
                            content = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DarkBlue)
                        ) {

                            WeatherCard(
                                state = viewModel.state,
                                backgroundColor = DeepBlue
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            WeatherForecast(state = viewModel.state)

                        }
                        if(viewModel.state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        viewModel.state.error?.let { error ->
                            Text(
                                text = error,
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)

                            )
                        }
                    }

                }


            }
        }
    }
}