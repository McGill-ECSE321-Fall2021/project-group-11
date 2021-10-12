package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class DailySchedule {

    @Id
    private int id;

    private DayOfWeek dayOfWeek;

    private Time startTime;

    private Time endTime;

    @ManyToOne
    private Librarian librarian;

    @ManyToOne
    private Library library;
}