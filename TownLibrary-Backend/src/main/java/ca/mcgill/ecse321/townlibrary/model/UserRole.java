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
}