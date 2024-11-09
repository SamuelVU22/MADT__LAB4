package com.example.madt_lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    EditText noteTitle;
    EditText noteContent;
    int noteId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_editor);

        ActionBar actionBar = getSupportActionBar();



        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE);


        // Initialize EditTexts
        noteTitle = findViewById(R.id.note_title); // Change to match XML
        noteContent = findViewById(R.id.note_content); // Change to match XML

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if(noteId != -1){
            String[] noteParts = MainActivity.notes.get(noteId).split(": ", 2); // Assuming title and content are separated by ": "
            noteTitle.setText(noteParts[0]); // Set title
            noteContent.setText(noteParts[1]); // Set content
            assert actionBar != null;
            actionBar.setTitle("Edit Note");
        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
        }

        noteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (noteId != -1) {
                    MainActivity.notes.set(noteId, noteTitle.getText().toString() + ": " + s.toString());
                    MainActivity.adapter.notifyDataSetChanged();

                    HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", noteSet).apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Save button functionality
        Button saveNoteButton = findViewById(R.id.save_note_button);
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString();
                String content = noteContent.getText().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(NoteEditorActivity.this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity.notes.set(noteId, title + ": " + content);
                    HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", noteSet).apply();
                    Toast.makeText(NoteEditorActivity.this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after saving
                }
            }
        });
    }
}