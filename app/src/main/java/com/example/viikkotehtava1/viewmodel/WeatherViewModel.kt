package com.example.viikkotehtava1.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikkotehtava1.data.model.WeatherEntity
import com.example.viikkotehtava1.data.repository.WeatherRepository
import com.example.viikkotehtava1.data.repository.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weather: com.example.viikkotehtava1.data.model.WeatherResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val city: String = "",
    val fromCache: Boolean = false,
    val isStale: Boolean = false
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository(application)

    var uiState by mutableStateOf(WeatherUiState())
        private set

    val history: Flow<List<WeatherEntity>> = repository.getHistory()

    fun updateCity(city: String) {
        uiState = uiState.copy(city = city)
    }

    fun fetchWeather() {
        val city = uiState.city.trim()
        if (city.isEmpty()) {
            uiState = uiState.copy(error = "Syötä kaupungin nimi")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            when (val result = repository.getWeather(city)) {
                is WeatherResult.Success -> {
                    uiState = uiState.copy(
                        weather = result.data,
                        isLoading = false,
                        fromCache = result.fromCache,
                        isStale = result.stale
                    )
                }
                is WeatherResult.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}