package com.example.setupsheets.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.example.setupsheets.db.Part
import com.example.setupsheets.db.Tool
import com.example.setupsheets.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

/**
 * A composable screen for adding or editing a note (part).
 * Provides input fields for entering part details, including title, coordinates,
 * dimensions, notes, and associated tools.
 *
 * - Pre-populates fields when editing an existing part.
 * - Includes delete confirmation, validations, and snackbars for user feedback.
 * - Uses Compose Material3 components and theming.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteViewModel: NoteViewModel,
    onSaveSuccess: () -> Unit,
    // ID of the note to edit; -1 indicates creating a new note.
    noteId: Int = -1,
    navController: NavHostController
) {
    // Coroutine scope for launching background tasks.
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope for launching background tasks.
    val scope = rememberCoroutineScope()

    // Find the note in the list if editing.
    val editingNote = noteViewModel.notes.collectAsState().value.find { it.id == noteId }

    // State for delete confirmation dialog.
    var showDeleteDialog by remember { mutableStateOf(false) }

    // UI state for input fields, with pre-population support.
    var title by rememberSaveable { mutableStateOf(editingNote?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(editingNote?.content ?: "") }
    var xCoord by rememberSaveable { mutableStateOf("") }
    var yCoord by rememberSaveable { mutableStateOf("") }
    var zCoord by rememberSaveable { mutableStateOf("") }
    var projectionLength by rememberSaveable { mutableStateOf(editingNote?.projectionLength ?: "") }
    var barSize by rememberSaveable { mutableStateOf(editingNote?.barSize ?: "") }
    var subSpindleColletSize by rememberSaveable { mutableStateOf(editingNote?.subSpindleColletSize ?: "") }

    // Lists for managing main and sub-spindle tools.
    var mainTools = remember {
        mutableStateListOf<Pair<String, String>>().apply {
            if (editingNote != null) {
                addAll(editingNote.mainSpindleTools.map { it.name to it.description })
            } else {
                add("" to "")
            }
        }
    }

    var subTools = remember {
        mutableStateListOf<Pair<String, String>>().apply {
            if (editingNote != null) {
                addAll(editingNote.subSpindleTools.map { it.name to it.description })
            }
        }
    }
    // Pre-populate from database coordinate fields if editing.
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
    // Delete confirmation dialog box before deletion
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        editingNote?.let {
                            noteViewModel.deleteNote(it)
                            snackbarHostState.showSnackbar("Part deleted")
                            onSaveSuccess()
                        }
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this part?") }
        )
    }
    // Main screen scaffold with centered top app bar, snackbar, and background styling.
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (editingNote != null) "Edit Part" else "Add Part") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                // Delete button is visible if editing part
                actions = {
                    if (editingNote != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.secondary
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            // Required field for the part name
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Part:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
            // Text fields for the X, Y, and Z coordinates
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("X:" to xCoord, "Y:" to yCoord, "Z:" to zCoord).forEachIndexed { index, (label, value) ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = {
                                if (it.isEmpty() || it.matches(Regex("^-?\\d*\\.?\\d*\$"))) {
                                    when (index) {
                                        0 -> xCoord = it
                                        1 -> yCoord = it
                                        2 -> zCoord = it
                                    }
                                }
                            },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            }
            // Optional Main spindle tools section for tool number and tool description
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Main Spindle Tools:", style = MaterialTheme.typography.titleMedium)
                        mainTools.forEachIndexed { index, pair ->
                            Row(Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = pair.first,
                                    onValueChange = {
                                        mainTools[index] = pair.copy(first = it)
                                    },
                                    label = { Text("Tool:") },
                                    modifier = Modifier.weight(0.2f).padding(horizontal = 4.dp),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                OutlinedTextField(
                                    value = pair.second,
                                    onValueChange = {
                                        mainTools[index] = pair.copy(second = it)
                                    },
                                    label = { Text("Description:") },
                                    modifier = Modifier.weight(0.8f).padding(horizontal = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(onClick = { mainTools.add("" to "") }) {
                                Text("Add Tool")
                            }
                        }
                    }
                }
            }
            // Optional Sub-spindle tools section for tool number and tool description
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Sub-Spindle Tools:", style = MaterialTheme.typography.titleMedium)
                        subTools.forEachIndexed { index, pair ->
                            Row(Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = pair.first,
                                    onValueChange = {
                                        subTools[index] = pair.copy(first = it)
                                    },
                                    label = { Text("Tool:") },
                                    modifier = Modifier.weight(0.2f).padding(horizontal = 4.dp),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                OutlinedTextField(
                                    value = pair.second,
                                    onValueChange = {
                                        subTools[index] = pair.copy(second = it)
                                    },
                                    label = { Text("Description:") },
                                    modifier = Modifier.weight(0.8f).padding(horizontal = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(onClick = { subTools.add("" to "") }) {
                                Text("Add Tool")
                            }
                        }
                    }
                }
            }
            // Generates fields for bar size, sub-spindle collet size, and initial projection length
            item {
                listOf("Bar Size:" to barSize, "Collet Size:" to subSpindleColletSize, "Projection Length:" to projectionLength).forEachIndexed { index, (label, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                when (index) {
                                    0 -> barSize = it
                                    1 -> subSpindleColletSize = it
                                    2 -> projectionLength = it
                                }
                            }
                        },
                        label = { Text(label) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
            // Notes text box
            item {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Notes:") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
            // Save button
            item {
                Button(
                    onClick = {
                        // Validate required fields before saving
                        if (title.isBlank() || xCoord.isBlank() || yCoord.isBlank() || zCoord.isBlank()
                            || mainTools.any { it.first.isBlank() && it.second.isBlank() }
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fill out all required fields")
                            }
                            return@Button
                        }
                        // Create a Note object with user input
                        val part = Part(
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
                        // Save or update the note in the database
                        scope.launch {
                            if (editingNote != null) {
                                noteViewModel.updateNote(part)
                                snackbarHostState.showSnackbar("Part updated")
                            } else {
                                noteViewModel.addNote(part)
                                snackbarHostState.showSnackbar("Part added")
                            }
                            onSaveSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(if (editingNote != null) "Update Part" else "Save Part", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
