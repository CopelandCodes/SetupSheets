package com.example.setupsheets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.setupsheets.ui.theme.SetupSheetsTheme
import androidx.lifecycle.lifecycleScope
import com.example.setupsheets.db.Note
import com.example.setupsheets.db.NoteDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            val db = NoteDatabase.getDatabase(this)
            val dao = db.noteDao()

            // Test Room operations
            lifecycleScope.launch {
                // Insert a note
                val note = Note(
                    title = "Test Note",
                    coordinates = "X:1 Y:2 Z:3",
                    content = "This is just a test",
                    toolList = "ToolA, ToolB",
                    projectionLength = "150mm",
                    barSize = "1.25\"",
                    subSpindleColletSize = "16C"
                )
                dao.insert(note)

                // Observe all notes
                dao.getAllNotes().collectLatest { notes ->
                    println("⚡ Notes in DB: ${notes.size}")
                    notes.forEach {
                        println("📝 ${it.title}: ${it.content}")
                    }
                }
            }

            // Launch your UI (can still be empty for now)
            setContent {
                // Placeholder UI
            }
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SetupSheetsTheme {
        Greeting("Jasper")
    }
}