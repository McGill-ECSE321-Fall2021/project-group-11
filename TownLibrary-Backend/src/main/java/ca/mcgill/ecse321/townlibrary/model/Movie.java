package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Movie extends Item {

    @Override
    public String toString() {
        return "Movie {}";
    }
}