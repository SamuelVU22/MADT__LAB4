package com.example.madt_lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView notesListView; // ListView to display the notes
    TextView emptyTv; // TextView to show a message when there are no notes

    static List<String> notes = new ArrayList<>(); // Static list to hold notes
    static ArrayAdapter<String> adapter; // Adapter for the ListView

    SharedPreferences sharedPreferences; // SharedPreferences to store notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display (full-screen)
        setContentView(R.layout.activity_main); // Set the layout for this activity

        // Apply insets for system bars (like status bar and navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons for adding and deleting notes
        Button addNoteButton = findViewById(R.id.addNoteBTN);
        Button deleteNoteButton = findViewById(R.id.deleteNoteBTN);

        // Set click listener for the add note button
        addNoteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class); // Create intent to start NoteEditorActivity
            startActivity(intent); // Start the NoteEditorActivity
        });

        // Set click listener for the delete note button
        deleteNoteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DeleteNoteActivity.class); // Create intent to start DeleteNoteActivity
            startActivity(intent); // Start the DeleteNoteActivity
        });

        // Initialize SharedPreferences to store notes
        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.noteList); // Find the ListView in the layout
        emptyTv = findViewById(R.id.emptyTV); // Find the TextView for empty state

        loadNotes(); // Load notes from SharedPreferences into the list
        adapter = new ArrayAdapter<>(this, R.layout.custom_notes_row, R.id.notesTV, notes); // Create an adapter for the ListView
        notesListView.setAdapter(adapter); // Set the adapter to the ListView
    }

    private void loadNotes() {
        // Retrieve stored notes from SharedPreferences as a HashSet
        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes", new HashSet<>());

        notes.clear(); // Clear existing notes from the list

        if (noteSet.isEmpty()) {
            emptyTv.setVisibility(View.VISIBLE); // Show empty message if no notes are available
        } else {
            emptyTv.setVisibility(View.GONE); // Hide empty message if there are notes
            notes.addAll(noteSet); // Load updated notes into the list from HashSet
        }
    }

    private void saveNotes() {
        HashSet<String> noteSet = new HashSet<>(notes); // Convert list of notes into a HashSet for storage
        sharedPreferences.edit().putStringSet("notes", noteSet).apply(); // Save updated notes to SharedPreferences
    }

    private void updateNotesDisplay() {
        adapter.notifyDataSetChanged(); // Notify adapter that data has changed (to refresh ListView)

        // Show or hide empty view based on whether there are any notes left
        emptyTv.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Reload notes from SharedPreferences when returning to this activity
        updateNotesDisplay(); // Update the display after loading new notes
    }
}