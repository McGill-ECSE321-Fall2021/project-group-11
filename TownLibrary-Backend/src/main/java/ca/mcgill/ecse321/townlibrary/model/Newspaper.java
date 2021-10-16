package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class Newspaper extends Item {

	@Override
    public String toString() {
        return "Newspaper {}";
    }
}