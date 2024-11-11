package com.example.madt_lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    EditText noteTitle; // EditText for the note title
    EditText noteContent; // EditText for the note content
    int noteId = -1; // Initialize noteId to -1 to indicate a new note
    SharedPreferences sharedPreferences; // SharedPreferences for storing notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor); // Set the layout for this activity

        ActionBar actionBar = getSupportActionBar(); // Get the action bar
        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE); // Initialize SharedPreferences

        // Initialize EditTexts for title and content
        noteTitle = findViewById(R.id.note_title);
        noteContent = findViewById(R.id.note_content);

        Intent intent = getIntent(); // Get the intent that started this activity
        noteId = intent.getIntExtra("noteId", -1); // Retrieve the noteId from the intent

        // Set action bar title based on whether we are creating a new note or editing an existing one
        if (noteId == -1 && actionBar != null) {
            actionBar.setTitle("New Note"); // Title for new notes
        } else if (actionBar != null) {
            actionBar.setTitle("Edit Note"); // Title for editing existing notes
        }

        // Set up the save button and its click listener
        Button saveNoteButton = findViewById(R.id.save_note_button);
        saveNoteButton.setOnClickListener(v -> saveNote()); // Call saveNote() when clicked
    }

    private void saveNote() {
        String title = noteTitle.getText().toString().trim(); // Get and trim title input
        String content = noteContent.getText().toString().trim(); // Get and trim content input

        // Validate title input
        if (title.isEmpty()) {
            Toast.makeText(NoteEditorActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return; // Exit method if title is empty
        }

        // Validate content input
        if (content.isEmpty()) {
            Toast.makeText(NoteEditorActivity.this, "Please enter content", Toast.LENGTH_SHORT).show();
            return; // Exit method if content is empty
        }

        // Check if it's a new note or an existing one, and update the notes list accordingly
        if (noteId == -1) {
            MainActivity.notes.add(title + ": " + content); // Add new note to the list
        } else {
            MainActivity.notes.set(noteId, title + ": " + content); // Update existing note in the list
        }

        HashSet<String> noteSet = new HashSet<>(MainActivity.notes); // Convert notes list to HashSet for storage
        sharedPreferences.edit().putStringSet("notes", noteSet).apply(); // Save updated notes to SharedPreferences

        Toast.makeText(NoteEditorActivity.this, "Note saved successfully!", Toast.LENGTH_SHORT).show(); // Show success message

        Intent resultIntent = new Intent(); // Create an intent to return result to calling activity
        setResult(RESULT_OK, resultIntent); // Set result code to OK
        finish(); // Close this activity and return to previous one
    }
}