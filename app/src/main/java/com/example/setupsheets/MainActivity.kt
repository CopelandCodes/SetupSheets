package com.example.setupsheets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.setupsheets.data.NoteRepository
import com.example.setupsheets.db.NoteDatabase
import com.example.setupsheets.navigation.SetupNavGraph
import com.example.setupsheets.ui.theme.SetupSheetsTheme
import com.example.setupsheets.viewmodel.NoteViewModel
import com.example.setupsheets.viewmodel.NoteViewModelFactory

/**
 * The main entry point of the app.
 * Sets up the ViewModel and navigation graph using Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Room database and repository
        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)
        val viewModelFactory = NoteViewModelFactory(repository)

        // Create the ViewModel
        val noteViewModel: NoteViewModel =
            ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        // Set up the UI
        setContent {
            val navController = rememberNavController()
            SetupSheetsTheme {
                SetupNavGraph(
                    navController = navController,
                    noteViewModel = noteViewModel
                )
            }
        }
    }
}
