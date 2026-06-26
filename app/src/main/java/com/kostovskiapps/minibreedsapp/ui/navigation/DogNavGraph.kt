package com.kostovskiapps.minibreedsapp.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kostovskiapps.minibreedsapp.ui.detail.DetailScreen
import com.kostovskiapps.minibreedsapp.ui.list.ListScreen
import com.kostovskiapps.minibreedsapp.ui.list.ListUiState
import com.kostovskiapps.minibreedsapp.ui.list.ListViewModel


fun NavGraphBuilder.dogNavGraph(navController: NavController) {

    composable("list") {
        val viewModel: ListViewModel = viewModel()
        ListScreen(
            viewModel = viewModel,
            onBreedClick = { breed ->
                navController.navigate("detail/$breed")
            }
        )
    }

    composable(
        route = "detail/{breedName}",
        arguments = listOf(navArgument("breedName") { type = NavType.StringType })
    ) { backStackEntry ->
        val breedName = backStackEntry.arguments?.getString("breedName") ?: ""
        val listBackStackEntry = remember(backStackEntry) {
            navController.getBackStackEntry("list")
        }
        val viewModel: ListViewModel = viewModel(listBackStackEntry)

        val uiState by viewModel.uiState.collectAsState()

        val isFavorited = (uiState as? ListUiState.Success)?.favorites?.contains(breedName) == true

        DetailScreen(
            breedName = breedName,
            isFavorited = isFavorited,
            onFavoriteToggle = { viewModel.toggleFavorite(breedName) },
            onBackClick = { navController.popBackStack() }
        )
    }

}