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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;
    TextView emptyTv;

    static List<String> notes = new ArrayList<>();
    static ArrayAdapter<String> adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addNoteButton = findViewById(R.id.addNoteBTN);
        Button deleteNoteButton = findViewById(R.id.deleteNoteBTN);

        addNoteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
            startActivity(intent);
        });


        deleteNoteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DeleteNoteActivity.class);
            startActivity(intent);
        });



        sharedPreferences = this.getSharedPreferences("com.example.madt_lab4", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.noteList);
        emptyTv = findViewById(R.id.emptyTV);

        loadNotes(); // Load notes from SharedPreferences
        adapter = new ArrayAdapter<>(this, R.layout.custom_notes_row, R.id.notesTV, notes);
        notesListView.setAdapter(adapter);
/*
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < notes.size()) { // Ensure position is valid
                Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    private void loadNotes() {
        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes", new HashSet<>());

        if (noteSet.isEmpty()) {
            emptyTv.setVisibility(View.VISIBLE);
        } else {
            emptyTv.setVisibility(View.GONE);
            notes.clear();
            notes.addAll(noteSet); // Load notes into the list
        }
    }

    private void saveNotes() {
        HashSet<String> noteSet = new HashSet<>(notes);
        sharedPreferences.edit().putStringSet("notes", noteSet).apply();
    }

    private void updateNotesDisplay() {
        adapter.notifyDataSetChanged(); // Notify adapter of data change
        emptyTv.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE); // Show/hide empty view
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_note){
            startActivity(new Intent(getApplicationContext(), NoteEditorActivity.class));
            return true;
        }
        return false;
    }
    */


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Reload notes when returning from NoteEditorActivity
    }
}