package com.example.setupsheets.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.setupsheets.db.Note
import com.example.setupsheets.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@Composable
fun NoteEditorScreen(
    noteViewModel: NoteViewModel,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // State for each input field
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var xCoord by remember { mutableStateOf("") }
    var yCoord by remember { mutableStateOf("") }
    var zCoord by remember { mutableStateOf("") }

    var mainTools = remember { mutableStateListOf(Pair("", "")) }
    var subTools = remember { mutableStateListOf<Pair<String, String>>() }

    var projectionLength by remember { mutableStateOf("") }
    var barSize by remember { mutableStateOf("") }
    var subSpindleColletSize by remember { mutableStateOf("") }

    // UI layout
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()) {

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Part:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Notes:") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

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

            item {
                OutlinedTextField(
                    value = projectionLength,
                    onValueChange = { projectionLength = it },
                    label = { Text("Projection Length:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = barSize,
                    onValueChange = { barSize = it },
                    label = { Text("Bar Size:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = subSpindleColletSize,
                    onValueChange = { subSpindleColletSize = it },
                    label = { Text("Sub Spindle Collet Size:") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Button(onClick = {
                    if (title.isBlank() || xCoord.isBlank() || yCoord.isBlank() || zCoord.isBlank()
                        || mainTools.any { it.first.isBlank() && it.second.isBlank() }
                        || projectionLength.isBlank() || barSize.isBlank()
                    ) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill out all required fields")
                        }
                        return@Button
                    }

                    val toolList = (mainTools + subTools).joinToString("\n") { "${it.first}: ${it.second}" }

                    val note = Note(
                        title = title,
                        coordinates = "X:$xCoord Y:$yCoord Z:$zCoord",
                        content = content,
                        toolList = toolList,
                        projectionLength = projectionLength,
                        barSize = barSize,
                        subSpindleColletSize = subSpindleColletSize
                    )
                    noteViewModel.addNote(note)

                    scope.launch {
                        snackbarHostState.showSnackbar("Note saved successfully")
                    }
                    onSaveSuccess()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Save Note")
                }
            }
        }
    }
}
