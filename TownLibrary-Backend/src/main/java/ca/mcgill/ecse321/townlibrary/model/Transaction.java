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
}