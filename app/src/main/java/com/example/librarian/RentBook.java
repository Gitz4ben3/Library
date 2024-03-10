package com.example.librarian;

public class RentBook {
    private String title;
    private String borrowerEmail;
    private String rentedDate;
    private String dueDate;
    private String borrowerName;

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public RentBook() {
    }

    public RentBook(String title, String borrowerEmail, String rentedDate, String dueDate) {
        this.title = title;
        this.borrowerEmail = borrowerEmail;
        this.rentedDate = rentedDate;
        this.dueDate = dueDate;
    }

    public String getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(String rentedDate) {
        this.rentedDate = rentedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }
}
