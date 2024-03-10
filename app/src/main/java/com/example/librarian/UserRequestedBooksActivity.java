/*/package com.example.librarian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UserRequestedBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> requestedBooks;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rentedBooksRef;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_requested_books);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            rentedBooksRef = FirebaseDatabase.getInstance().getReference("rentedBooks");

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            requestedBooks = new ArrayList<>();
            bookAdapter = new BookAdapter(requestedBooks);
            recyclerView.setAdapter(bookAdapter);
            textViewUserName = findViewById(R.id.myT);

            Log.d("UserRequestedBooks", "Current user: " + currentUser.getEmail());

            // Fetch user's name and concatenate
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Registration reg = snapshot.getValue(Registration.class);
                    if (reg != null) {
                        String fn = reg.getFirstname();
                        String ln = reg.getLastname();

                        if (fn != null && ln != null) {
                            textViewUserName.setText("All the books rented: " + fn + " " + ln);
                        } else {
                            textViewUserName.setText("All the books rented");
                        }
                    } else {
                        Log.e("UserRequestedBooks", "Registration object is null");
                        // Handle the case where Registration is null
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            fetchRequestedBooks(currentUser.getEmail());
        } else {
            Log.e("UserRequestedBooks", "Current user is null");
        }
    }

    private void fetchRequestedBooks(final String userEmail) {
        rentedBooksRef.orderByChild("borrowerEmail").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestedBooks.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null) {
                                requestedBooks.add(book);
                            }
                        }

                        bookAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });
    }
}
*/
package com.example.librarian;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class UserRequestedBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> requestedBooks;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rentedBooksRef;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_requested_books);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            rentedBooksRef = FirebaseDatabase.getInstance().getReference("rentedBooks");

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            requestedBooks = new ArrayList<>();
            bookAdapter = new BookAdapter(requestedBooks);
            recyclerView.setAdapter(bookAdapter);
            textViewUserName = findViewById(R.id.myT);

            Log.d("UserRequestedBooks", "Current user: " + currentUser.getEmail());

            // Fetch user's name and concatenate
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Registration reg = snapshot.getValue(Registration.class);
                        if (reg != null) {
                            String fn = reg.getFirstname();
                            String ln = reg.getLastname();

                            if (fn != null && ln != null && !fn.isEmpty() && !ln.isEmpty()) {
                                textViewUserName.setText("All the books rented: " + fn + " " + ln);
                            } else {
                                textViewUserName.setText("All the books rented");
                            }
                        }
                    }
                    else{
                        textViewUserName.setText("Books rented by Mmamonyai Modiba");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                    Log.e("UserRequestedBooks", "Database error: " + error.getMessage());
                    textViewUserName.setText("All the books rented");
                }
            });
            fetchRequestedBooks(currentUser.getEmail());
        } else {
            Log.e("UserRequestedBooks", "Current user is null");
            // Handle the case where the current user is null
            // For example, you might want to redirect to the login screen
        }
    }

    private void fetchRequestedBooks(final String userEmail) {
        rentedBooksRef.orderByChild("borrowerEmail").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestedBooks.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null) {
                                requestedBooks.add(book);
                            }
                        }

                        bookAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        Log.e("UserRequestedBooks", "Database error: " + databaseError.getMessage());
                    }
                });
    }
}