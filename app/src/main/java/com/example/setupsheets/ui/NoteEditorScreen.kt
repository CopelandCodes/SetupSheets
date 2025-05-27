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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment
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

    val editingNote = noteViewModel.notes.collectAsState().value.find { it.id == noteId }

    var title by rememberSaveable { mutableStateOf(editingNote?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(editingNote?.content ?: "") }
    var xCoord by rememberSaveable { mutableStateOf("") }
    var yCoord by rememberSaveable { mutableStateOf("") }
    var zCoord by rememberSaveable { mutableStateOf("") }
    var projectionLength by rememberSaveable { mutableStateOf(editingNote?.projectionLength ?: "") }
    var barSize by rememberSaveable { mutableStateOf(editingNote?.barSize ?: "") }
    var subSpindleColletSize by rememberSaveable { mutableStateOf(editingNote?.subSpindleColletSize ?: "") }

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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.secondary
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
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

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("X:" to xCoord, "Y:" to yCoord, "Z:" to zCoord).forEachIndexed { index, (label, value) ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = {
                                when (index) {
                                    0 -> xCoord = it
                                    1 -> yCoord = it
                                    2 -> zCoord = it
                                }
                            },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
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
                }
            }

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
                                listOf("Tools:" to pair.first, "Description:" to pair.second).forEachIndexed { i, (label, value) ->
                                    OutlinedTextField(
                                        value = value,
                                        onValueChange = {
                                            mainTools[index] = if (i == 0) pair.copy(first = it) else pair.copy(second = it)
                                        },
                                        label = { Text(label) },
                                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
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
                                listOf("Tool:" to pair.first, "Description:" to pair.second).forEachIndexed { i, (label, value) ->
                                    OutlinedTextField(
                                        value = value,
                                        onValueChange = {
                                            subTools[index] = if (i == 0) pair.copy(first = it) else pair.copy(second = it)
                                        },
                                        label = { Text(label) },
                                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
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

            listOf(
                "Bar Size:" to barSize,
                "Collet Size:" to subSpindleColletSize,
                "Projection Length:" to projectionLength
            ).forEachIndexed { index, (label, value) ->
                item {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            when (index) {
                                0 -> barSize = it
                                1 -> subSpindleColletSize = it
                                2 -> projectionLength = it
                            }
                        },
                        label = { Text(label) },
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
            }

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
                                snackbarHostState.showSnackbar("Part updated")
                            } else {
                                noteViewModel.addNote(note)
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
