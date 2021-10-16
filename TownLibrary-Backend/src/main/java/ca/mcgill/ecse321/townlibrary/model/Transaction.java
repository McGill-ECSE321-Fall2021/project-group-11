package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Transaction {

    private int id;

    private Timestamp startDate;
    private Timestamp endDate;

    private UserRole userRole;

    private Event event;

    private Item item;

    @Id
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

    @ManyToOne
    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole ur) {
        this.userRole = ur;
    }

    @OneToOne
    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event e) {
        this.event = e;
    }

    public boolean hasEvent() {
        return this.event != null;
    }

    @OneToOne
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
                + ", userRole.id=" + (this.userRole == null ? null : this.userRole.getId())
                + ", event.id=" + (this.event == null ? null : this.event.getId())
                + ", item.id=" + (this.item == null ? null : this.item.getId()) + "}";
    }
}