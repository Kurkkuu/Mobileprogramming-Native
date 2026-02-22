package com.example.viikkotehtava1.data.repository

import android.content.Context
import android.util.Log
import com.example.viikkotehtava1.data.local.AppDatabase
import com.example.viikkotehtava1.data.local.WeatherDao
import com.example.viikkotehtava1.data.model.WeatherEntity
import com.example.viikkotehtava1.data.model.WeatherResponse
import com.example.viikkotehtava1.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class WeatherRepository(context: Context) {

    private val dao: WeatherDao = AppDatabase.getDatabase(context).weatherDao()
    private val api = RetrofitInstance.api
    private val apiKey = RetrofitInstance.apiKey

    companion object {
        private const val TAG = "WeatherRepository"
        private const val CACHE_DURATION_MS = 30 * 60 * 1000 // 30 min
    }

    suspend fun getWeather(city: String): WeatherResult {
        Log.d(TAG, "Getting weather for: $city")

        val cached = dao.getWeather(city).first()

        if (cached != null) {
            val age = System.currentTimeMillis() - cached.timestamp
            Log.d(TAG, "Found cache, age: ${age / 1000}s")

            if (age < CACHE_DURATION_MS) {
                Log.d(TAG, "Using cached data")
                return WeatherResult.Success(cached.toResponse(), fromCache = true)
            }
            Log.d(TAG, "Cache too old, fetching from API")
        }

        return try {
            Log.d(TAG, "Fetching from API...")
            val response = api.getWeather(city, apiKey)
            dao.insertWeather(response.toEntity())
            Log.d(TAG, "Saved to database")
            WeatherResult.Success(response, fromCache = false)

        } catch (e: Exception) {
            Log.e(TAG, "API error: ${e.message}")
            if (cached != null) {
                return WeatherResult.Success(cached.toResponse(), fromCache = true, stale = true)
            }
            WeatherResult.Error(e.message ?: "Unknown error")
        }
    }

    fun getHistory(): Flow<List<WeatherEntity>> = dao.getAllWeather()

    private fun WeatherResponse.toEntity(): WeatherEntity {
        return WeatherEntity(
            cityName = this.cityName,
            temperature = this.main.temperature,
            feelsLike = this.main.feelsLike,
            humidity = this.main.humidity,
            description = this.weather.firstOrNull()?.description ?: "",
            windSpeed = this.wind.speed,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun WeatherEntity.toResponse(): WeatherResponse {
        return WeatherResponse(
            cityName = this.cityName,
            main = com.example.viikkotehtava1.data.model.MainData(
                temperature = this.temperature,
                feelsLike = this.feelsLike,
                humidity = this.humidity,
                pressure = 0
            ),
            weather = listOf(
                com.example.viikkotehtava1.data.model.WeatherDescription(
                    main = "",
                    description = this.description,
                    icon = ""
                )
            ),
            wind = com.example.viikkotehtava1.data.model.WindData(
                speed = this.windSpeed
            )
        )
    }
}

sealed class WeatherResult {
    data class Success(
        val data: WeatherResponse,
        val fromCache: Boolean,
        val stale: Boolean = false
    ) : WeatherResult()
    data class Error(val message: String) : WeatherResult()
}