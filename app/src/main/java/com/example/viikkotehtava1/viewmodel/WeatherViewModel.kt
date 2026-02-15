package com.example.viikkotehtava1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikkotehtava1.data.model.WeatherResponse
import com.example.viikkotehtava1.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weather: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val city: String = ""
)

class WeatherViewModel : ViewModel() {

    var uiState by mutableStateOf(WeatherUiState())
        private set

    fun updateCity(city: String) {
        uiState = uiState.copy(city = city)
    }

    fun fetchWeather() {
        val city = uiState.city.trim()
        if (city.isEmpty()) {
            uiState = uiState.copy(error = "Import city name")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            try {
                val response = RetrofitInstance.api.getWeather(
                    city = city,
                    apiKey = RetrofitInstance.apiKey
                )
                uiState = uiState.copy(
                    weather = response,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error: ${e.message ?: "City not found"}"
                )
            }
        }
    }
}