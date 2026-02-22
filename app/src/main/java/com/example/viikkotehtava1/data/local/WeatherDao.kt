package com.example.viikkotehtava1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.viikkotehtava1.data.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_cache WHERE cityName = :city LIMIT 1")
    fun getWeather(city: String): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather_cache ORDER BY timestamp DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_cache WHERE timestamp < :cutoffTime")
    suspend fun deleteOldData(cutoffTime: Long)
}