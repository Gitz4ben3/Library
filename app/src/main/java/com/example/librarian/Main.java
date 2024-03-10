package com.example.librarian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {
    private CardView viewRented, bookRent, addBook, viewAllAvailableBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewRented = findViewById(R.id.viewRented);
        bookRent = findViewById(R.id.bookRent);
        addBook = findViewById(R.id.addBook);
        viewAllAvailableBooks = findViewById(R.id.viewAllAvailableBooks);
        bookRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Rent.class));
            }
        });
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
            }
        });
        viewRented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewAll.class));
            }
        });

        viewAllAvailableBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CheckAvailabilityActivity.class));
            }
        });
    }
}