package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public abstract class Item {

    @Id
    private int id;

    private Status status;

    private String name;

    @ManyToOne
    private Library library;
}