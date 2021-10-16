package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class DailySchedule {

    private int id;

    private DayOfWeek dayOfWeek;

    private Time startTime;

    private Time endTime;
  
    private Librarian librarian;

    private Library library;

    @Id
    public int getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public Time getEndTime() {
        return endTime;
    }
    @ManyToOne
    public Librarian getLibrarian() {
        return librarian;
    }
    
    @ManyToOne
    public Library getLibrary() {
        return library;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

}