package com.example.librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// ViewAvailableBooksActivity.java
public class ViewAvailableBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvailableBooksAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_available_books);

        mDatabase = FirebaseDatabase.getInstance().getReference("books");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AvailableBooksAdapter();
        recyclerView.setAdapter(adapter);

        // Retrieve and display available books
        retrieveAvailableBooks();
    }

    private void retrieveAvailableBooks() {
        mDatabase.orderByChild("isRented").equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Book> availableBooks = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null && !book.isRented()) {
                                availableBooks.add(book);
                            }
                        }

                        adapter.setAvailableBooks(availableBooks);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
    }
}
