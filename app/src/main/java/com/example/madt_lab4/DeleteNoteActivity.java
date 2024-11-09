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

    private TextView emptyTextView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes; // Local copy of notes
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        ListView noteListView = findViewById(R.id.noteList);
        emptyTextView = findViewById(R.id.emptyTV);

        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE);

        loadNotes();

        adapter = new ArrayAdapter<>(this, R.layout.custom_notes_row, R.id.notesTV, notes);
        noteListView.setAdapter(adapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmationDialog(position);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish()); // Go back to main activity
    }

    private void loadNotes() {
        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes", new HashSet<>());

        if (noteSet.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            notes = new ArrayList<>();
        } else {
            emptyTextView.setVisibility(View.GONE);
            notes = new ArrayList<>(noteSet); // Load notes into the list
        }
    }

    private void showConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> deleteNote(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteNote(int position) {
        notes.remove(position); // Remove the selected note
        HashSet<String> noteSet = new HashSet<>(notes);

        sharedPreferences.edit().putStringSet("notes", noteSet).apply(); // Save updated notes

        adapter.notifyDataSetChanged(); // Notify adapter of data change

        if (notes.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE); // Show empty view if no notes left
        }
    }
}