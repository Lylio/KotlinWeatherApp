package com.botsheloramela.basicweatherapp.domain.usecase

import com.botsheloramela.basicweatherapp.data.model.CurrentWeather
import com.botsheloramela.basicweatherapp.data.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(city: String): CurrentWeather {
        return weatherRepository.getCurrentWeatherByCity(city)
    }
}