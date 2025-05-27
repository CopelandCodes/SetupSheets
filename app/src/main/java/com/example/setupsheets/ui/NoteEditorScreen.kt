package com.example.setupsheets.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.setupsheets.db.Note
import com.example.setupsheets.db.Tool
import com.example.setupsheets.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

/**
 * A composable screen for adding/editing a note with multiple fields and validations.
 * Includes snackbars for user feedback and form validation before saving.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteViewModel: NoteViewModel,
    onSaveSuccess: () -> Unit,
    noteId: Int = -1,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var navigatedBack by remember { mutableStateOf(false) }

    // Fetch the note if editing (noteId != -1)
    val editingNote = noteViewModel.notes.collectAsState().value.find { it.id == noteId }

    // Input state for form fields (initialized once when editingNote is loaded)
    var title by rememberSaveable { mutableStateOf(editingNote?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(editingNote?.content ?: "") }
    var xCoord by rememberSaveable { mutableStateOf("") }
    var yCoord by rememberSaveable { mutableStateOf("") }
    var zCoord by rememberSaveable { mutableStateOf("") }
    var projectionLength by rememberSaveable { mutableStateOf(editingNote?.projectionLength ?: "") }
    var barSize by rememberSaveable { mutableStateOf(editingNote?.barSize ?: "") }
    var subSpindleColletSize by rememberSaveable { mutableStateOf(editingNote?.subSpindleColletSize ?: "") }

    var mainTools = remember { mutableStateListOf<Pair<String, String>>().apply {
        if (editingNote != null) {
            addAll(editingNote.mainSpindleTools.map { it.name to it.description })
        } else {
            add(Pair("", ""))
        }
    }}

    var subTools = remember { mutableStateListOf<Pair<String, String>>().apply {
        if (editingNote != null) {
            addAll(editingNote.subSpindleTools.map { it.name to it.description })
        }
    }}

    // Split coordinates if editing
    LaunchedEffect(editingNote) {
        editingNote?.coordinates?.let { coords ->
            val regex = """X:(\S+)\s+Y:(\S+)\s+Z:(\S+)""".toRegex()
            regex.find(coords)?.destructured?.let { (x, y, z) ->
                xCoord = x
                yCoord = y
                zCoord = z
            }
        }
    }

    // Layout structure using Scaffold for snackbar support
    Scaffold(topBar = {TopAppBar(title = {
            Text(text = if (editingNote != null) "Edit Note" else "Add Note")},
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    },snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
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
                        if (title.isBlank() || xCoord.isBlank() || yCoord.isBlank() || zCoord.isBlank()
                            || mainTools.any { it.first.isBlank() && it.second.isBlank() }
                            || projectionLength.isBlank() || barSize.isBlank()
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fill out all required fields")
                            }
                            return@Button
                        }

                        val note = Note(
                            id = editingNote?.id ?: 0,
                            title = title,
                            coordinates = "X:$xCoord Y:$yCoord Z:$zCoord",
                            content = content,
                            mainSpindleTools = mainTools.map { Tool(it.first, it.second) },
                            subSpindleTools = subTools.map { Tool(it.first, it.second) },
                            projectionLength = projectionLength,
                            barSize = barSize,
                            subSpindleColletSize = subSpindleColletSize
                        )

                        scope.launch {
                            if (editingNote != null) {
                                noteViewModel.updateNote(note)
                                snackbarHostState.showSnackbar("Note updated")
                            } else {
                                noteViewModel.addNote(note)
                                snackbarHostState.showSnackbar("Note added")
                            }
                            if (!navigatedBack) {
                                navigatedBack = true
                                onSaveSuccess() // usually triggers navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (editingNote != null) "Update Note" else "Save Note")
                }
            }
        }
    }
}
