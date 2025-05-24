package com.example.setupsheets.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.unit.dp
import com.example.setupsheets.viewmodel.NoteViewModel

@Composable
fun MainScreen(viewModel: NoteViewModel) {
    // Collect the filtered notes and search query from the ViewModel
    val notes by viewModel.notes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Update ViewModel search query on text change
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 🔍 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.updateSearchQuery(it)
            },
            label = { Text("Search Notes") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 📜 Note List
        LazyColumn {
            itemsIndexed(notes) { _, note ->
                NoteCard(note = note, onClick = {})
            }
        }
    }
}
