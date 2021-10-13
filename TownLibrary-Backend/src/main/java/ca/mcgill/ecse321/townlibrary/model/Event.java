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

    // Constructor

    public Event(int anId, String aName) {
        id = anId;
        name = aName;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}