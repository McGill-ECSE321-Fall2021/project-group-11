package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

import ca.mcgill.ecse321.townlibrary.TownLibraryApplication;

import java.util.*;

@Entity
public abstract class Item {

    @Id
    private int id;

    private Status status;

    private String name;

    @ManyToOne
    private Library library;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setStatus(Status status){
        this.status = status;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
}

