package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class Librarian extends UserRole {

    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}