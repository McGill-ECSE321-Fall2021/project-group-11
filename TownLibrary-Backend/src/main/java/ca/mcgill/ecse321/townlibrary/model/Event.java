package ca.mcgill.ecse321.townlibrary.model;

import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.*;


import java.util.List;
import java.util.Set;

@Entity
public class Event {

    private int id;

    private String name;

    private Library library;

    private Transaction transaction;

    private Set<UserRole> users = new HashSet<>();

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

    @ManyToMany
    public Set<UserRole> getUsers() {
        return users;
    }

    public void setUsers(Set<UserRole> users) {
        this.users = users;
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
    
    /*public void addUser(UserRole user) {
        users.add(user);
    }

    public void removeUser(UserRole user) {
        users.remove(user);
    }*/
}