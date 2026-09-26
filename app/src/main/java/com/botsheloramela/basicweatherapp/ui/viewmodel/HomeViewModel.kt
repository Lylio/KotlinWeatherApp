package com.botsheloramela.basicweatherapp.ui.viewmodel

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.botsheloramela.basicweatherapp.data.model.CurrentWeather
import com.botsheloramela.basicweatherapp.data.model.WeatherForecast
import com.botsheloramela.basicweatherapp.data.model.WeatherItem
import com.botsheloramela.basicweatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.botsheloramela.basicweatherapp.domain.usecase.GetDeviceLocationUseCase
import com.botsheloramela.basicweatherapp.domain.usecase.GetWeatherForecastUseCase
import com.botsheloramela.basicweatherapp.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import com.botsheloramela.basicweatherapp.domain.usecase.GetCurrentWeatherByCityUseCase


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val getDeviceLocationUseCase: GetDeviceLocationUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getCurrentWeatherByCityUseCase: GetCurrentWeatherByCityUseCase
): ViewModel() {

    val locationState = mutableStateOf<Location?>(null)
    private val weatherForecastState = mutableStateOf<WeatherForecast?>(null)
    val currentWeatherState = mutableStateOf<CurrentWeather?>(null)

    // City search state
    val searchQuery = mutableStateOf("")
    val searchError = mutableStateOf<String?>(null)
    val isSearching = mutableStateOf(false)

    /**
     * Fetch the device location.
     */
    fun getDeviceLocation() {
        viewModelScope.launch {
            val location = getDeviceLocationUseCase()
            locationState.value = location
        }
    }

    /**
     * Fetch the weather forecast based on the current location.
     */
    fun getWeatherForecast() {
        viewModelScope.launch {
            val location = locationState.value
            if (location != null) {
                val weatherForecast = getWeatherForecastUseCase(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                weatherForecastState.value = weatherForecast
            }
        }
    }

    /**
     * Fetch the current weather based on the current location.
     */
    fun getCurrentWeather() {
        viewModelScope.launch {
            val location = locationState.value
            if (location != null) {
                val currentWeather = getCurrentWeatherUseCase(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                currentWeatherState.value = currentWeather
            }
        }
    }

    /**
     * Fetch weather data for a city entered by the user.
     */
    fun searchCity(city: String) {

        if (city.isBlank()) {
            return
        }

        viewModelScope.launch {

            isSearching.value = true
            searchError.value = null

            try {
                val currentWeather =
                    getCurrentWeatherByCityUseCase(city.trim())

                currentWeatherState.value = currentWeather

                val weatherForecast = getWeatherForecastUseCase(
                    latitude = currentWeather.coord.lat,
                    longitude = currentWeather.coord.lon
                )

                weatherForecastState.value = weatherForecast

            } catch (e: Exception) {
                searchError.value = "Could not find \"$city\""
            } finally {
                isSearching.value = false
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentAndNextForecasts(): List<WeatherItem>? {
        val currentTime = Instant.now().epochSecond



        // Filter the list to get the forecast closest to the current time and the next 4 forecasts
        val sortedForecasts = weatherForecastState.value?.list?.sortedBy { DateTimeUtils.parseDtTxtToTimestamp(it.dt_txt) }

        val index = sortedForecasts?.indexOfFirst { DateTimeUtils.parseDtTxtToTimestamp(it.dt_txt) > currentTime }

        return if (index != -1) {
            sortedForecasts?.subList(index!!, index + 5)
        } else {
            emptyList() // No future forecasts found
        }
    }
}