package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Transaction {

    private int id;

    private Timestamp startDate;
    private Timestamp endDate;
    private TransactionType type = null;
    private UserRole userRole;

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
    public void setType(TransactionType type) {
        if (this.type == null) this.type = type;
    }
    public TransactionType getType(){
        return this.type;
    }

    @Override
    public String toString() {
        return "Transaction {id=" + this.id
                + ", startDate=" + this.startDate
                + ", endDate=" + this.endDate
                + ", userRole.id=" + (this.userRole == null ? null : this.userRole.getId())
                + "}";
    }
}