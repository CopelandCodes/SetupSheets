package com.example.setupsheets.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Composable function that displays the main screen of the app.
 * This screen shows a list of parts (setup sheets) along with a search bar and an add button.
 *
 * viewModel- The ViewModel that provides the parts and manages state.
 * navController- The NavHostController for navigation between screens.
 * onEditNote- A callback function triggered when a part is selected for editing.
 * onAddNote- A callback function triggered when the user taps the add button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NoteViewModel,
    navController: NavHostController,
    onEditNote: (Int) -> Unit,
    onAddNote: () -> Unit
) {
    // Collects the list of parts from the ViewModel and re-renders on changes.
    val notes by viewModel.notes.collectAsState()

    // Collects the current search query from the ViewModel.
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Scaffold provides a consistent layout structure with a top app bar and a floating action button.
    Scaffold(
        topBar = {
            // Top app bar with a centered title and consistent colors.
            CenterAlignedTopAppBar(
                title = { Text("Setup Sheets") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            // Floating action button for adding new parts.
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("  Add Part  ", style = MaterialTheme.typography.titleMedium)
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary
    ) { paddingValues ->
        // Column containing the search bar and the list of notes.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            // Search bar for filtering parts by title.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search Parts") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // LazyColumn displaying the list of notes.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                // Each note is displayed using a NoteCard composable.
                items(notes) { note ->
                    NoteCard(part = note, onClick = { onEditNote(note.id) })
                }
            }
        }
    }
}
