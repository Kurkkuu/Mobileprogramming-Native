package com.example.viikkotehtava1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val description: String,
    val windSpeed: Double,
    val timestamp: Long = System.currentTimeMillis()
)