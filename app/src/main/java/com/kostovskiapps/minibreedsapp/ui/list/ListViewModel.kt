package com.kostovskiapps.minibreedsapp.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kostovskiapps.minibreedsapp.data.repositories.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DogRepository(application)
    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)

    val searchQuery = MutableStateFlow("")

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<ListUiState> = combine(_uiState, searchQuery, _favorites) { state, query, favs ->
        if (state is ListUiState.Success) {
            val filteredList = if (query.isNotBlank()) {
                state.breeds.filter { it.contains(query, ignoreCase = true) }
            } else {
                state.breeds
            }
            ListUiState.Success(breeds = filteredList, favorites = favs)
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ListUiState.Loading
    )

    init {
        _favorites.value = repository.getFavoriteBreeds()
        fetchBreeds()
    }

    fun fetchBreeds() {
        viewModelScope.launch {
            _uiState.value = ListUiState.Loading
            try {
                val breedList = repository.getBreedNames()
                _uiState.value = ListUiState.Success(breeds = breedList, favorites = _favorites.value)
            } catch (_: UnknownHostException) {
                _uiState.value = ListUiState.Error("No internet connection. Please check your Wi-Fi or mobile data and try again.")
            } catch (e: HttpException) {
                _uiState.value = ListUiState.Error("Server error (${e.code()}). Please try again later.")
            } catch (e: Exception) {
                _uiState.value = ListUiState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun toggleFavorite(breed: String) {
        val currentFavs = _favorites.value.toMutableSet()
        if (currentFavs.contains(breed)) {
            currentFavs.remove(breed)
        } else {
            currentFavs.add(breed)
        }
        _favorites.value = currentFavs
        repository.saveFavoriteBreeds(currentFavs)
    }
}