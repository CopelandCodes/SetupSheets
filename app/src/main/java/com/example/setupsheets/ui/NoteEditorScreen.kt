package com.example.setupsheets.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.setupsheets.db.Note
import com.example.setupsheets.db.Tool
import com.example.setupsheets.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

/**
 * A composable screen for adding/editing a note with multiple fields and validations.
 * Includes snackbars for user feedback and form validation before saving.
 */
@Composable
fun NoteEditorScreen(
    noteViewModel: NoteViewModel,
    onSaveSuccess: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Input state for form fields
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var xCoord by remember { mutableStateOf("") }
    var yCoord by remember { mutableStateOf("") }
    var zCoord by remember { mutableStateOf("") }
    var projectionLength by remember { mutableStateOf("") }
    var barSize by remember { mutableStateOf("") }
    var subSpindleColletSize by remember { mutableStateOf("") }

    // Lists for dynamic tool inputs
    var mainTools = remember { mutableStateListOf(Pair("", "")) }
    var subTools = remember { mutableStateListOf<Pair<String, String>>() }

    // Layout structure using Scaffold for snackbar support
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Required title field
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Part:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Optional content field
            item {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Notes:") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Required coordinate fields (X, Y, Z)
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        value = xCoord,
                        onValueChange = { xCoord = it },
                        label = { Text("X:") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = yCoord,
                        onValueChange = { yCoord = it },
                        label = { Text("Y:") },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = zCoord,
                        onValueChange = { zCoord = it },
                        label = { Text("Z:") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        singleLine = true
                    )
                }
            }

            // Required main spindle tools input (expandable list)
            item {
                Text("Main Spindle Tools:", style = MaterialTheme.typography.titleMedium)
                mainTools.forEachIndexed { index, pair ->
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = pair.first,
                            onValueChange = { mainTools[index] = pair.copy(first = it) },
                            label = { Text("Tools:") },
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        )
                        OutlinedTextField(
                            value = pair.second,
                            onValueChange = { mainTools[index] = pair.copy(second = it) },
                            label = { Text("Description:") },
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        )
                    }
                }
                TextButton(onClick = { mainTools.add(Pair("", "")) }) {
                    Text("Add Tool")
                }
            }

            // Optional sub spindle tools input (expandable list)
            item {
                Text("Sub-Spindle Tools:", style = MaterialTheme.typography.titleMedium)
                subTools.forEachIndexed { index, pair ->
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = pair.first,
                            onValueChange = { subTools[index] = pair.copy(first = it) },
                            label = { Text("Tools:") },
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        )
                        OutlinedTextField(
                            value = pair.second,
                            onValueChange = { subTools[index] = pair.copy(second = it) },
                            label = { Text("Description:") },
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        )
                    }
                }
                TextButton(onClick = { subTools.add(Pair("", "")) }) {
                    Text("Add Tool")
                }
            }

            // Required projection length (decimal)
            item {
                OutlinedTextField(
                    value = projectionLength,
                    onValueChange = { projectionLength = it },
                    label = { Text("Projection Length:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Required bar size (decimal)
            item {
                OutlinedTextField(
                    value = barSize,
                    onValueChange = { barSize = it },
                    label = { Text("Bar Size:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Optional sub spindle collet size (decimal)
            item {
                OutlinedTextField(
                    value = subSpindleColletSize,
                    onValueChange = { subSpindleColletSize = it },
                    label = { Text("Sub Spindle Collet Size:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Save button with validation and snackbar confirmation
            item {
                Button(
                    onClick = {
                        // Form validation for required fields
                        if (title.isBlank() || xCoord.isBlank() || yCoord.isBlank() || zCoord.isBlank()
                            || mainTools.any { it.first.isBlank() && it.second.isBlank() }
                            || projectionLength.isBlank() || barSize.isBlank()
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fill out all required fields")
                            }
                            return@Button
                        }

                        // Build Note entity using field values
                        val note = Note(
                            title = title,
                            coordinates = "X:$xCoord Y:$yCoord Z:$zCoord",
                            content = content,
                            mainSpindleTools = mainTools.map { Tool(it.first, it.second) },
                            subSpindleTools = subTools.map { Tool(it.first, it.second) },
                            projectionLength = projectionLength,
                            barSize = barSize,
                            subSpindleColletSize = subSpindleColletSize
                        )

                        // Insert note via ViewModel
                        noteViewModel.addNote(note)

                        // Confirm save and navigate away
                        scope.launch {
                            snackbarHostState.showSnackbar("Note saved successfully")
                        }
                        onSaveSuccess()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Note")
                }
            }
        }
    }
}
