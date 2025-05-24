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
fun NavGraph(
    navController: NavHostController,
    noteViewModel: NoteViewModel
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController, noteViewModel)
        }
        composable("edit") {
            NoteEditorScreen(
                noteViewModel = noteViewModel,
                onSaveSuccess = { navController.navigate("main") }
            )
        }
    }
}

