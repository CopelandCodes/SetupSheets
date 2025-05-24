package com.example.setupsheets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.setupsheets.ui.*
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Defines all navigation routes in the app and maps them to their corresponding UI composables.
 *
 * @param navController The navigation controller used to handle navigation between screens.
 * @param viewModel The ViewModel shared across screens for accessing and modifying notes.
 */
@Composable
fun NavGraph(navController: NavHostController, viewModel: NoteViewModel) {
    NavHost(
        navController = navController,
        startDestination = "main" // Initial screen displayed when the app launches
    ) {
        /**
         * Main screen route - displays a list of notes.
         */
        composable("main") {
            MainScreen(viewModel = viewModel, navController = navController)
        }

        /**
         * Note editor screen route - used for adding or editing a note.
         */
        composable("edit") {
            NoteEditorScreen(viewModel = viewModel, navController = navController)
        }
    }
}
