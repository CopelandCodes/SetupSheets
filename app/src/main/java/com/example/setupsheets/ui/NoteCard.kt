package com.example.setupsheets.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.example.setupsheets.db.Note

@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // clickable card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleLarge)
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

