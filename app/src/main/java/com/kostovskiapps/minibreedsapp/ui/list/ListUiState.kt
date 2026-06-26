package com.kostovskiapps.minibreedsapp.ui.list

sealed interface ListUiState {
    object Loading : ListUiState

    data class Success(
        val breeds: List<String>,
        val favorites: Set<String> = emptySet()
    ) : ListUiState

    data class Error(val message: String) : ListUiState
}