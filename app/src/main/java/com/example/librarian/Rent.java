package com.example.librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Rent extends AppCompatActivity {

    private EditText editTitle, editBorrowerEmail;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTitle = findViewById(R.id.editTitle);
        editBorrowerEmail = findViewById(R.id.editBorrowerEmail);

        Button btnRent = findViewById(R.id.btnRent);
        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentBook();
            }
        });
    }

    private void rentBook() {
        final String title = editTitle.getText().toString().trim();
        final String borrowerEmail = editBorrowerEmail.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(borrowerEmail)) {
            mDatabase.child("books").orderByChild("title").equalTo(title)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Book book = snapshot.getValue(Book.class);
                                    if (book != null && !book.isRented()) {
                                        // Check if the book is already rented
                                        // Update book status to rented
                                        book.setRented(true);
                                        book.setBorrowerEmail(borrowerEmail);

                                        // Update the book status in the database
                                        mDatabase.child("books").child(snapshot.getKey()).setValue(book);

                                        // Get the current date
                                        Date currentDate = Calendar.getInstance().getTime();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        final String rentedDate = dateFormat.format(currentDate);

                                        // Calculate the due date (14 days after the rented date)
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(currentDate);
                                        calendar.add(Calendar.DAY_OF_YEAR, 14);
                                        final String formattedDueDate = dateFormat.format(calendar.getTime());

                                        // Add the rented book entry to the rentedBooks node
                                        String key = mDatabase.child("rentedBooks").push().getKey();
                                        RentBook rentBook = new RentBook(title, borrowerEmail, rentedDate, formattedDueDate);
                                        mDatabase.child("rentedBooks").child(key).setValue(rentBook);

                                        Toast.makeText(getApplicationContext(), "Book rented successfully", Toast.LENGTH_SHORT).show();

                                        // Clear the input fields
                                        editTitle.setText("");
                                        editBorrowerEmail.setText("");
                                        return;
                                    } else {
                                        // Book is already rented
                                        Toast.makeText(getApplicationContext(), "Book is already rented", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Book not found in the database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter both book title and borrower's email", Toast.LENGTH_SHORT).show();
        }
    }
}
