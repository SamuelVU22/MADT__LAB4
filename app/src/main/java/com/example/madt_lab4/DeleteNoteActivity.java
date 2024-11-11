package com.example.madt_lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class DeleteNoteActivity extends AppCompatActivity {

    private TextView emptyTextView; // TextView to display when there are no notes
    private ArrayAdapter<String> adapter; // Adapter for the ListView
    private ArrayList<String> notes; // Local copy of notes
    private SharedPreferences sharedPreferences; // SharedPreferences to store notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note); // Set the layout for this activity

        ListView noteListView = findViewById(R.id.noteList); // Find the ListView in the layout
        emptyTextView = findViewById(R.id.emptyTV); // Find the TextView for empty state

        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE); // Initialize SharedPreferences

        loadNotes(); // Load notes from SharedPreferences

        // Initialize the adapter with the notes and set it to the ListView
        adapter = new ArrayAdapter<>(this, R.layout.custom_notes_row, R.id.notesTV, notes);
        noteListView.setAdapter(adapter);

        // Set an item click listener on the ListView to handle note deletion
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmationDialog(position); // Show confirmation dialog for deletion
            }
        });

        // Set up a back button to return to the main activity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish()); // Close this activity and return to previous one
    }

    private void loadNotes() {
        // Retrieve stored notes from SharedPreferences as a HashSet
        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes", new HashSet<>());

        if (noteSet.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE); // Show empty message if no notes are available
            notes = new ArrayList<>(); // Initialize an empty list for notes
        } else {
            emptyTextView.setVisibility(View.GONE); // Hide empty message if there are notes
            notes = new ArrayList<>(noteSet); // Load notes into the list from the HashSet
        }
    }

    private void showConfirmationDialog(int position) {
        // Create an AlertDialog to confirm deletion of a selected note
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion") // Set dialog title
                .setMessage("Are you sure you want to delete this note?") // Set dialog message
                .setPositiveButton("Yes", (dialog, which) -> deleteNote(position)) // If "Yes", call deleteNote method
                .setNegativeButton("No", null) // If "No", dismiss dialog without action
                .show(); // Show the dialog
    }

    private void deleteNote(int position) {
        notes.remove(position); // Remove the selected note from the list
        HashSet<String> noteSet = new HashSet<>(notes); // Convert updated list back to HashSet

        sharedPreferences.edit().putStringSet("notes", noteSet).apply(); // Save updated notes to SharedPreferences

        adapter.notifyDataSetChanged(); // Notify adapter that data has changed

        if (notes.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE); // Show empty view if no notes left after deletion
        }
    }
}