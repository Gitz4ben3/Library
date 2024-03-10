package com.example.librarian;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ViewAll extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private AllBooksAdapter allBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button btnViewAll = findViewById(R.id.btnViewAll);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveAndFilterBooks();
            }
        });

        // Set up RecyclerView
        RecyclerView recyclerViewAllBooks = findViewById(R.id.recyclerViewAllBooks);
        allBooksAdapter = new AllBooksAdapter();
        recyclerViewAllBooks.setAdapter(allBooksAdapter);
        recyclerViewAllBooks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void retrieveAndFilterBooks() {
        DatabaseReference booksRef = mDatabase.child("books");

        booksRef.orderByChild("rented").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Book> rentedBooks = new ArrayList<>();

                        // Filter and collect books where rented is true
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null && book.isRented()) {
                                rentedBooks.add(book);
                            }
                        }

                        // Sort rented books by faculty
                        Collections.sort(rentedBooks, new Comparator<Book>() {
                            @Override
                            public int compare(Book book1, Book book2) {
                                return book1.getFaculty().compareTo(book2.getFaculty());
                            }
                        });

                        // Group books by faculty
                        Map<String, List<Book>> booksByFaculty = new LinkedHashMap<>();
                        for (Book book : rentedBooks) {
                            String faculty = book.getFaculty();
                            if (!booksByFaculty.containsKey(faculty)) {
                                booksByFaculty.put(faculty, new ArrayList<>());
                            }
                            booksByFaculty.get(faculty).add(book);
                        }

                        // Sort books within each faculty by yearOfStudy
                        for (Map.Entry<String, List<Book>> entry : booksByFaculty.entrySet()) {
                            List<Book> booksInFaculty = entry.getValue();
                            Collections.sort(booksInFaculty, new Comparator<Book>() {
                                @Override
                                public int compare(Book book1, Book book2) {
                                    // Assuming yearOfStudy is a String, adjust the logic if it's an integer
                                    return book1.getYearOfStudy().compareTo(book2.getYearOfStudy());
                                }
                            });
                        }

                        // Display or process the sorted books as needed
                        // Update your RecyclerView adapter here
                        allBooksAdapter.setAllBooks(booksByFaculty);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
    }
}
