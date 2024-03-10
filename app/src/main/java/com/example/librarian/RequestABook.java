package com.example.librarian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestABook extends AppCompatActivity {

    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_abook);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        booksRef = database.getReference("books");

        EditText edtBookTitle = findViewById(R.id.edtBookTitle);
        Button btnRequest = findViewById(R.id.btnRequest);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the book title from the EditText
                String bookTitle = edtBookTitle.getText().toString().trim();

                // Query the database for the book with the entered title
                queryBook(bookTitle);
            }
        });
    }

    private void queryBook(final String bookTitle) {
        booksRef.orderByChild("title").equalTo(bookTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Book with the entered title found
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Book requestedBook = snapshot.getValue(Book.class);
                        if (requestedBook != null) {
                            // Check if the book is available
                            if (!requestedBook.isRented()) {
                                // Book is available for request
                                sendToast("Request sent");
                                // You can implement the logic to update the book status or send a request to the server here.
                            } else {
                                // Book is already rented out
                                sendToast("Book already rented out");
                            }
                            return; // No need to continue iterating if a match is found
                        }
                    }
                } else {
                    // Book with the entered title not found
                    sendToast("Book not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                sendToast("Error: " + databaseError.getMessage());
            }
        });
    }

    private void sendToast(String message) {
        Toast.makeText(RequestABook.this, message, Toast.LENGTH_SHORT).show();
    }
}
