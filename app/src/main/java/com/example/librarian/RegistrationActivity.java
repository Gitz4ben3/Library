package com.example.librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText name, surname, mail, contactNumber, password, c_password;
    String role = "User";  // Set the default role to "User"
    private Button registerButton, logButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        mail = findViewById(R.id.mail);
        contactNumber = findViewById(R.id.contactNumber);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = name.getText().toString().trim();
                String lastname = surname.getText().toString().trim();
                String email = mail.getText().toString().trim();
                String contacts = contactNumber.getText().toString().trim();
                String c_pswd = c_password.getText().toString().trim();
                String pswd = password.getText().toString().trim();

                boolean check = checkMethod(firstname, lastname, email, contacts, pswd, c_pswd);
                while (!check) {
                    check = checkMethod(firstname, lastname, email, contacts, pswd, c_pswd);
                    Toast.makeText(getApplicationContext(), "Ensure all fields are entered correctly.", Toast.LENGTH_SHORT).show();
                }

                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, pswd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                        Registration user = new Registration(role, firstname, lastname, email, contacts, pswd);
                        String userId = mAuth.getUid();
                        if (userId != null) {
                            mDatabase.child("Users").child(userId).setValue(user);
                            Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Detail registered.\nUser Details not captured.", Toast.LENGTH_LONG).show();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            userId = mAuth.getUid();

                            if (userId != null) {
                                mDatabase.child("Users").child(userId).setValue(user);
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "User Details Capturing failed\nMake sure you have a stable internet connection.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "User registration failed\nSeems as if Email already exists.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public boolean checkMethod(String f, String s, String e, String c, String p, String c_p) {
        boolean check = true;
        if (f.isEmpty()) {
            name.setError("Name cannot be empty");
            name.requestFocus();
            check = false;
        }
        if (s.isEmpty()) {
            surname.setError("Surname cannot be empty");
            surname.requestFocus();
            check = false;
        }

        if (e.isEmpty()) {
            mail.setError("Email cannot be empty");
            mail.requestFocus();
            check = false;
        }
        if (c.isEmpty()) {
            contactNumber.setError("Name cannot be empty");
            contactNumber.requestFocus();
            check = false;
        }
        if (p.isEmpty()) {
            password.setError("Name cannot be empty");
            password.requestFocus();
            check = false;
        }
        if (c_p.isEmpty()) {
            c_password.setError("Name cannot be empty");
            c_password.requestFocus();
            check = false;
        }
        if (!p.equals(c_p)) {
            c_password.setError("Password must match");
            c_password.requestFocus();
            check = false;
        }
        return check;
    }
}
