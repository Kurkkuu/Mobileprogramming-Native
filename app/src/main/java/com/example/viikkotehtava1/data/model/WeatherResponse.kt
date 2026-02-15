package com.example.viikkotehtava1.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("main")
    val main: MainData,

    @SerializedName("weather")
    val weather: List<WeatherDescription>,

    @SerializedName("wind")
    val wind: WindData
)

data class MainData(
    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("pressure")
    val pressure: Int
)

data class WeatherDescription(
    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)

data class WindData(
    @SerializedName("speed")
    val speed: Double
)