package com.example.setupsheets.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Displays a list of notes with a search bar and buttons to add or edit notes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NoteViewModel,
    navController: NavHostController,
    onEditNote: (Int) -> Unit,
    onAddNote: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Setup Sheets") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Text("Add Part")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search Parts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notes) { note ->
                    NoteCard(note = note, onClick = { onEditNote(note.id) })
                }
            }
        }
    }
}
