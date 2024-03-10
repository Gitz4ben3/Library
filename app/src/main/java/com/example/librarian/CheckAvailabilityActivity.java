package com.example.librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckAvailabilityActivity extends AppCompatActivity {

    private EditText editTextBookTitle;
    private Button buttonCheckAvailability;
    private TextView textViewResult;

    private DatabaseReference mDatabase,mDatabase2;
    private boolean isBookAvailable=false;
    private boolean isRented = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability);

        mDatabase = FirebaseDatabase.getInstance().getReference("books");

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        buttonCheckAvailability = findViewById(R.id.buttonCheckAvailability);
        textViewResult = findViewById(R.id.textViewResult);

        buttonCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBookAvailability();
            }
        });
    }
    private void checkBookAvailability() {
        String enteredTitle = editTextBookTitle.getText().toString().trim();

        mDatabase.orderByChild("title").equalTo(enteredTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Book book = snapshot.getValue(Book.class);
                        if (book != null) {
                            if (book.isRented()) {
                                textViewResult.setText("The book has already been rented by another student");
                            } else {
                                textViewResult.setText("The book is available for rental");
                            }
                            return;
                        }
                    }
                }

                textViewResult.setText("The book you just requested does not exist");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}

