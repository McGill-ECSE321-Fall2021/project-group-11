package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Event {

    @Id
    private int id;

    private String name;

    @ManyToOne
    private Library library;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Library getLibrary() {
        return library;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}