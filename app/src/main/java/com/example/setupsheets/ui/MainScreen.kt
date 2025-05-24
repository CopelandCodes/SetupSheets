package com.example.setupsheets.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.example.setupsheets.db.Note
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Main screen displaying search input and filtered note list.
 *
 * @param navController Navigation controller used to navigate between screens.
 * @param viewModel ViewModel that exposes the list of notes and search state.
 */
@Composable
fun MainScreen(navController: NavController, viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = viewModel::updateSearchQuery
        )

        Spacer(modifier = Modifier.height(12.dp))

        NoteList(
            notes = notes.filter { it.title.contains(searchQuery, ignoreCase = true) },
            onClick = { note ->
                // Navigate to a detail/edit screen with note ID
                navController.navigate("noteDetail/${note.id}")
            }
        )
    }
}

/**
 * Search bar input field to filter notes by title.
 */
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Search Notes") },
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Scrollable list of note cards.
 */
@Composable
fun NoteList(notes: List<Note>, onClick: (Note) -> Unit) {
    LazyColumn {
        items(notes) { note ->
            NoteCard(note = note, onClick = { onClick(note) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
