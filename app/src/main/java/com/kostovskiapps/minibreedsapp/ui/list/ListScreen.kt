package com.kostovskiapps.minibreedsapp.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kostovskiapps.minibreedsapp.ui.components.BreedRow
import com.kostovskiapps.minibreedsapp.ui.components.ErrorScreen
import com.kostovskiapps.minibreedsapp.ui.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onBreedClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("The Mini-Breeds App") }) }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Search breeds") },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when (val state = uiState) {
                is ListUiState.Loading -> LoadingScreen()
                is ListUiState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.fetchBreeds() }
                )
                is ListUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.breeds) { breed ->
                            val isFavorited = state.favorites.contains(breed)

                            BreedRow(
                                breed = breed,
                                isFavorited = isFavorited,
                                onClick = { onBreedClick(breed) },
                                onFavoriteClick = { viewModel.toggleFavorite(breed) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}