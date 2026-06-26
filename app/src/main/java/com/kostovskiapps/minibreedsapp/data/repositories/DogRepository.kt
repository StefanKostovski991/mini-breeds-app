package com.kostovskiapps.minibreedsapp.data.repositories

import com.kostovskiapps.minibreedsapp.data.api.DogApiService

import android.content.Context
import androidx.core.content.edit

class DogRepository(
    context: Context,
    private val apiService: DogApiService = DogApiService.instance
) {
    private val sharedPreferences = context.getSharedPreferences("breed_prefs", Context.MODE_PRIVATE)
    private val favoritesKey = "favorite_breeds"

    suspend fun getBreedNames(): List<String> {
        val response = apiService.getBreeds()
        return response.message.keys.toList().sorted()
    }

    fun getFavoriteBreeds(): Set<String> {
        return sharedPreferences.getStringSet(favoritesKey, emptySet()) ?: emptySet()
    }

    fun saveFavoriteBreeds(favorites: Set<String>) {
        sharedPreferences.edit { putStringSet(favoritesKey, favorites) }
    }
}