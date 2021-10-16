package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class HeadLibrarian extends Librarian {

    private Library library;

    @OneToOne
    public Library getLibrary(){
        return this.library;
    }

    public void setLibrary(Library lib){
        this.library = lib;
    }

}