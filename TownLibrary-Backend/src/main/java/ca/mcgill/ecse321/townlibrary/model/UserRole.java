package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public abstract class UserRole {

    @Id
    private int id;

    private String name;

    private String address;

    @ManyToOne
    private Library library;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library lib) {
        this.library = lib;
    }
}