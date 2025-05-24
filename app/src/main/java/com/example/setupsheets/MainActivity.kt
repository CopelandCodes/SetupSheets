package com.example.setupsheets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.setupsheets.data.NoteRepository
import com.example.setupsheets.db.NoteDatabase
import com.example.setupsheets.navigation.NavGraph
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

        // Step 1: Initialize the Room database and DAO
        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()

        // Step 2: Create the repository and ViewModel factory
        val repository = NoteRepository(dao)
        val viewModelFactory = NoteViewModelFactory(repository)

        // Step 3: Initialize the ViewModel
        val noteViewModel: NoteViewModel =
            ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        // Step 4: Launch the Jetpack Compose UI and set up navigation
        setContent {
            SetupSheetsTheme {
                val navController = rememberNavController() // Create NavController for screen navigation
                NavGraph(navController = navController, viewModel = noteViewModel)
            }
        }
    }
}
