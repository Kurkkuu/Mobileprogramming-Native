package com.example.viikkotehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikkotehtava1.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val history by viewModel.history.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("WeatherApp (Room)") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = { viewModel.updateCity(it) },
                    label = { Text("City") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { viewModel.fetchWeather() }
                    )
                )

                Button(
                    onClick = { viewModel.fetchWeather() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Search")
                    }
                }
            }

            if (uiState.error != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            uiState.weather?.let { weather ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = weather.cityName,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            if (uiState.fromCache) {
                                val text = if (uiState.isStale) {
                                    "Vanhaa dataa (API ei vastannu)"
                                } else {
                                    "Välimuistista (Room)"
                                }
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "${weather.main.temperature.toInt()}°C",
                                style = MaterialTheme.typography.displayLarge
                            )

                            Text(
                                text = weather.weather.firstOrNull()?.description ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                WeatherInfoItem("Feels like", "${weather.main.feelsLike.toInt()}°C")
                                WeatherInfoItem("Humidity", "${weather.main.humidity}%")
                                WeatherInfoItem("Wind", "${weather.wind.speed} m/s")
                            }
                        }
                    }
                }
            }

            if (history.isNotEmpty()) {
                item {
                    Text(
                        text = "Searchhistory (Room)",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                items(history) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = item.cityName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("${item.temperature.toInt()}°C, ${item.description}")
                            }
                            Text(
                                text = formatTimeAgo(item.timestamp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / (1000 * 60)
    return when {
        minutes < 1 -> "Now"
        minutes < 60 -> "$minutes min"
        else -> "${minutes / 60} t"
    }
}