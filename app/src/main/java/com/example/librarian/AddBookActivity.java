package com.example.librarian;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etShelfNo, etYear, etIsbn;

    private ImageView ivBookImage;
    private Button btnAddBook;
    private String selectedFaculty;

    private DatabaseReference booksRef;
    private StorageReference storageRef;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize Firebase Database and Storage references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        booksRef = database.getReference("books");
        storageRef = storage.getReference("book_images");

        // Initialize UI components
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etShelfNo = findViewById(R.id.etShelfNo);
        ivBookImage = findViewById(R.id.ivBookImage);
        btnAddBook = findViewById(R.id.btnAddBook);
        etYear = findViewById(R.id.edtYear);
        etIsbn = findViewById(R.id.edtIsbn);

        //Set spinner
        Spinner spinnerFaculties = findViewById(R.id.spinnerFaculties);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.faculty_options, // Add a string array resource for faculty options in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculties.setAdapter(adapter);
        spinnerFaculties.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected faculty from the Spinner
                selectedFaculty = parentView.getItemAtPosition(position).toString();

                // You can use the selectedFaculty value as needed
                // For example, you can store it in a variable or use it directly in addBookToLibrary method.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (optional)
            }
        });

        // Set click listener for image view to choose an image
        ivBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Set click listener for "Add Book" button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookToLibrary();
            }
        });
    }

    private void openFileChooser() {
        // Intent to choose an image from the device
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivBookImage.setImageURI(imageUri);
        }
    }

    private void addBookToLibrary() {
        // Get values from EditText fields
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String shelfNo = etShelfNo.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        String year = etYear.getText().toString().trim();

        // Check if any field is empty
        if (title.isEmpty() || author.isEmpty() || shelfNo.isEmpty() || imageUri == null ||isbn.isEmpty()||year.isEmpty()) {
            // Show an error message or handle accordingly
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique filename for the image
        String imageFileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

        // Upload image to Firebase Storage
        StorageReference fileReference = storageRef.child(imageFileName);
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create a Book object
                        Book book = new Book();
                        book.setTitle(title);
                        book.setAuthor(author);
                        book.setShelfNo(shelfNo);
                        book.setRented(false);
                        book.setDueDate("");
                        book.setYearOfStudy(year);
                        book.setBorrowerName("");
                        book.setIsbn(isbn);
                        book.setFaculty(selectedFaculty);
                        book.setImageUrl(uri.toString());

                        // Save the book to the database
                        String bookId = booksRef.push().getKey();
                        booksRef.child(bookId).setValue(book);

                        // Clear the EditText fields and image view for the next entry
                        etTitle.getText().clear();
                        etAuthor.getText().clear();
                        etShelfNo.getText().clear();
                        etIsbn.getText().clear();
                        etYear.getText().clear();
                        ivBookImage.setImageResource(android.R.drawable.ic_menu_gallery);

                        // Show a success message or navigate to another activity
                        Toast.makeText(AddBookActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(AddBookActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        // Get the file extension of the selected image
        // This might not be foolproof, but it's a simple example
        return uri != null ? uri.getPathSegments().get(uri.getPathSegments().size() - 1) : null;
    }
}
