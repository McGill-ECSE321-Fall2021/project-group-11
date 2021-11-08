package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class Event {

    private int id;

    private String name;

    private Library library;

    private Transaction transaction;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @ManyToOne
    public Library getLibrary() {
        return library;
    }

    @OneToOne
    public Transaction getTransaction() {
        return transaction;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}