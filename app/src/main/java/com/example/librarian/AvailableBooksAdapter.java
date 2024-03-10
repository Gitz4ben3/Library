package com.example.librarian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// AvailableBooksAdapter.java
public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.ViewHolder> {

    private List<Book> availableBooks = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = availableBooks.get(position);

        holder.textTitle.setText(book.getTitle());
        holder.textAuthor.setText(book.getAuthor());
        // Add other TextViews for additional book details if needed
    }

    @Override
    public int getItemCount() {
        return availableBooks.size();
    }

    public void setAvailableBooks(List<Book> availableBooks) {
        this.availableBooks = availableBooks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textAuthor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            // Add other TextViews for additional book details if needed
        }
    }
}
