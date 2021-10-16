package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class HeadLibrarian extends Librarian {

    @OneToOne
    private Library library;
    
}