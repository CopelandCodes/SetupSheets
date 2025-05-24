package com.example.setupsheets.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.setupsheets.db.Note
import com.example.setupsheets.viewmodel.NoteViewModel

/**
 * Composable screen for creating or editing a note.
 * It shows text fields for all note fields and Save/Delete buttons.
 *
 * @param note The note to edit (if null, a new note will be created)
 * @param viewModel The shared NoteViewModel instance
 * @param navController Used to navigate back to the note list after saving or deleting
 */
@Composable
fun NoteEditorScreen(
    note: Note? = null,
    viewModel: NoteViewModel = viewModel(),
    navController: NavController
) {
    // State variables for each field with pre-filled values if editing
    var title by remember { mutableStateOf(note?.title ?: "") }
    var coordinates by remember { mutableStateOf(note?.coordinates ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var toolList by remember { mutableStateOf(note?.toolList ?: "") }
    var projectionLength by remember { mutableStateOf(note?.projectionLength ?: "") }
    var barSize by remember { mutableStateOf(note?.barSize ?: "") }
    var subSpindleColletSize by remember { mutableStateOf(note?.subSpindleColletSize ?: "") }

    // UI layout using Column and spacing
    Column(modifier = Modifier.padding(16.dp)) {
        // Text fields for each property of the note
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = coordinates,
            onValueChange = { coordinates = it },
            label = { Text("Coordinates") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = toolList,
            onValueChange = { toolList = it },
            label = { Text("Tool List") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = projectionLength,
            onValueChange = { projectionLength = it },
            label = { Text("Projection Length") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = barSize,
            onValueChange = { barSize = it },
            label = { Text("Bar Size") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = subSpindleColletSize,
            onValueChange = { subSpindleColletSize = it },
            label = { Text("Sub Spindle Collet Size") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save button – calls addNote or updateNote depending on context
        Button(onClick = {
            val updatedNote = Note(
                id = note?.id ?: 0,
                title = title,
                coordinates = coordinates,
                content = content,
                toolList = toolList,
                projectionLength = projectionLength,
                barSize = barSize,
                subSpindleColletSize = subSpindleColletSize
            )

            if (note == null) {
                viewModel.addNote(updatedNote) // New note
            } else {
                viewModel.updateNote(updatedNote) // Existing note
            }
            navController.popBackStack() // Go back to note list
        }) {
            Text("Save")
        }

        // Show delete button only if editing an existing note
        if (note != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.deleteNote(note)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }
    }
}
