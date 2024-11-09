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

    EditText noteTitle;
    EditText noteContent;
    int noteId = -1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        ActionBar actionBar = getSupportActionBar();
        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE);

        // Initialize EditTexts
        noteTitle = findViewById(R.id.note_title);
        noteContent = findViewById(R.id.note_content);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId == -1 && actionBar != null) {
            actionBar.setTitle("New Note");
        } else if (actionBar != null) {
            actionBar.setTitle("Edit Note");
        }

        Button saveNoteButton = findViewById(R.id.save_note_button);
        saveNoteButton.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = noteTitle.getText().toString().trim();
        String content = noteContent.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(NoteEditorActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            Toast.makeText(NoteEditorActivity.this, "Please enter content", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            MainActivity.notes.add(title + ": " + content);
        } else {
            MainActivity.notes.set(noteId, title + ": " + content);
        }

        HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", noteSet).apply();

        Toast.makeText(NoteEditorActivity.this, "Note saved successfully!", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
