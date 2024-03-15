package com.example.labs2024_1_gr04.lab1.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class CitiesViewModel(private val apiService: ColombiaCitiesService): ViewModel() {
    private val _data = MutableStateFlow<List<City>>(emptyList())
    val data: StateFlow<List<City>> = _data

    fun fetchCities(city: String) {
        Log.d("API_CALL", city)
        viewModelScope.launch {
            if (city.isNotEmpty()) {
                try {
                    val response = apiService.fetchCities(city)
                    Log.d("API_CALL", response.body().toString())
                    if (response.isSuccessful) {
                        _data.value = response.body() ?: emptyList()
                    } else {
                        Log.e("API_CALL", "Unsuccessful response: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("API_CALL", "There was an error calling the API: ${e.message}")
                }
            }
        }
    }
}