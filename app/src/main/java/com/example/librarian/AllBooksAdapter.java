package com.example.librarian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllBooksAdapter extends RecyclerView.Adapter<AllBooksAdapter.ViewHolder> {

    private List<Book> allBooks;
    private List<String> faculties;

    public AllBooksAdapter() {
        this.allBooks = new ArrayList<>();
        this.faculties = new ArrayList<>();
    }

    public void setAllBooks(Map<String, List<Book>> booksByFaculty) {
        allBooks.clear();
        faculties.clear();

        for (Map.Entry<String, List<Book>> entry : booksByFaculty.entrySet()) {
            faculties.add(entry.getKey());
            allBooks.addAll(entry.getValue());
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = allBooks.get(position);
        holder.textTitle.setText(holder.itemView.getContext().getString(R.string.title_placeholder, book.getTitle()));
        holder.textBorrowerEmail.setText(holder.itemView.getContext().getString(R.string.borrower_email_placeholder, book.getBorrowerEmail()));
        holder.textFaculty.setText(holder.itemView.getContext().getString(R.string.faculty_placeholder, book.getFaculty()));
        holder.textYearOfStudy.setText(holder.itemView.getContext().getString(R.string.year_of_study_placeholder, book.getYearOfStudy()));
        // Add other bindings for additional book information if needed
    }

    @Override
    public int getItemCount() {
        return allBooks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textBorrowerEmail;
        TextView textFaculty;
        TextView textYearOfStudy;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textBorrowerEmail = itemView.findViewById(R.id.textBorrowerEmail);
            textFaculty = itemView.findViewById(R.id.textFaculty);
            textYearOfStudy = itemView.findViewById(R.id.textYearOfStudy);
        }
    }
}
