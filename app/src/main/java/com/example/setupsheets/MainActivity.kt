package com.example.setupsheets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.setupsheets.data.NoteRepository
import com.example.setupsheets.db.NoteDatabase
import com.example.setupsheets.navigation.SetupNavGraph
import com.example.setupsheets.ui.theme.SetupSheetsTheme
import com.example.setupsheets.viewmodel.NoteViewModel
import com.example.setupsheets.viewmodel.NoteViewModelFactory

/**
 * MainActivity is the entry point of the Setup Sheets app.
 * It initializes the database, ViewModel, and sets up the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Room database by obtaining an instance of NoteDatabase
        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()

        // Create a repository using the DAO for data operations
        val repository = NoteRepository(dao)

        // Create a ViewModel factory to supply the repository to the ViewModel
        val viewModelFactory = NoteViewModelFactory(repository)

        // Obtain the ViewModel instance using the factory
        val noteViewModel: NoteViewModel =
            ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        // Set up the Compose UI
        setContent {
            // Remember a NavController for navigation between screens
            val navController = rememberNavController()

            // Apply the custom app theme
            SetupSheetsTheme {
                // Surface defines the container for the app UI with proper background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // SetupNavGraph defines the navigation graph with all app screens
                    SetupNavGraph(
                        navController = navController,
                        noteViewModel = noteViewModel
                    )
                }
            }
        }
    }
}
