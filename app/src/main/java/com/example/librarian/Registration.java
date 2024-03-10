package com.example.librarian;

public class Registration {
    String role, firstname, lastname, email, contacts, pswd;

    public Registration(String role, String firstname, String lastname, String email, String contacts, String pswd) {
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contacts = contacts;
        this.pswd = pswd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
