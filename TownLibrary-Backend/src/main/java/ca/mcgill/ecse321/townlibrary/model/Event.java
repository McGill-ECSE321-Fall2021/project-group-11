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

    public Event(int anId, String aName) {
        id = anId;
        name = aName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Library getLibrary() {
        return library;
    }

    public void setId(int anId) {
        id = anId;
    }

    public void setName(String aName) {
        name = aName;
    }

    public void setLibrary(Library aLibrary) {
        library = aLibrary;
    }

    
}