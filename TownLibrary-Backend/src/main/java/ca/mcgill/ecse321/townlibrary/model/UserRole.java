package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public abstract class UserRole {

    private int id;

    private String name;

    private String address;

    private Library library;

    protected UserRole() {
        // so no one can do anonymous inner class black magic
    }

    @Id
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

    @ManyToOne
    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library lib) {
        this.library = lib;
    }
}