package com.example.librarian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserMain extends AppCompatActivity {
    private CardView requestBook,  allBoksRented;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        requestBook  = findViewById(R.id.requestBook);
        allBoksRented = findViewById(R.id.allBooksRented);
        allBoksRented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserRequestedBooksActivity.class));
            }
        });
        requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RequestABook.class));
            }
        });

    }
}