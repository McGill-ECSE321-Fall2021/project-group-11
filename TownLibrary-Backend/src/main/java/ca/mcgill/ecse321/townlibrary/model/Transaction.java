package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Transaction {

    @Id
    private int id;

    private Timestamp startDate;
    private Timestamp endDate;

    @ManyToOne
    private UserRole userRole;

    @OneToOne
    private Event event;

    @OneToOne
    private Item item;

    public Transaction() {
        // Must have an empty ctor for database black magic!
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Timestamp ts) {
        this.startDate = ts;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp ts) {
        this.endDate = ts;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole ur) {
        this.userRole = ur;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event e) {
        this.event = e;
    }

    public boolean hasEvent() {
        return this.event != null;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item e) {
        this.item = e;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    @Override
    public String toString() {
        return "Transaction {id=" + this.id
                + ", startDate=" + this.startDate
                + ", endDate=" + this.endDate
                + ", userRole=" + this.userRole
                + ", event=" + this.event
                + ", item=" + this.item + "}";
    }
}