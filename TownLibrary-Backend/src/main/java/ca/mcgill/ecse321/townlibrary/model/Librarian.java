package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Librarian extends UserRole {

    @OneToOne
    private Library library;
}