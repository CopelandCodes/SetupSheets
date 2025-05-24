package com.example.setupsheets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.setupsheets.ui.MainScreen
import com.example.setupsheets.ui.NoteEditorScreen
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Defines navigation routes and screens for the application.
 * Sets up the navigation graph used by NavHost.
 */
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    noteViewModel: NoteViewModel
) {
    NavHost(navController = navController, startDestination = "main") {

        // Main screen displaying the list of notes
        composable("main") {
            MainScreen(
                viewModel = noteViewModel,
                navController = navController,
                onEditNote = { noteId ->
                    // Navigate to the editor screen with the note ID for editing
                    navController.navigate("editor?noteId=$noteId")
                },
                onAddNote = {
                    // Navigate to the editor screen with no note ID to add a new note
                    navController.navigate("editor")
                }
            )
        }

        // Editor screen for creating or editing notes
        composable(
            route = "editor?noteId={noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
            NoteEditorScreen(
                noteViewModel = noteViewModel,
                onSaveSuccess = {
                    // Return to the main screen after saving
                    navController.popBackStack()
                },
                noteId = noteId
            )
        }
    }
}
