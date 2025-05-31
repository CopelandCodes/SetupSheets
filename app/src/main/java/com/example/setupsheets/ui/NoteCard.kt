package com.example.setupsheets.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.example.setupsheets.db.Note

/**
 * Composable function that displays a single Note item as a card in the list.
 * When clicked, triggers the provided onClick callback, typically navigating to the edit screen.
 *
 * note- The note object containing the data to display.
 * onClick- Callback function invoked when the card is clicked.
 */
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    // Card is a material container for displaying information with elevation and background styling.
    Card(
        modifier = Modifier
            .fillMaxWidth() // Makes the card fill the horizontal space of its parent.
            .padding(8.dp)  // Adds padding around the card for spacing between other elements.
            .clickable { onClick() }, // Makes the card respond to click events to navigate or perform actions.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevation adds a subtle shadow for depth.
    ) {
        // Column is a vertical layout that stacks its children vertically.
        Column(modifier = Modifier.padding(16.dp)) {
            // Display the note's title using the theme's titleMedium text style.
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
