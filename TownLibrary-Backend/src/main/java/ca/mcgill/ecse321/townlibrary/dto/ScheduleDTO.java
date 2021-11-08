package ca.mcgill.ecse321.townlibrary.dto;

import java.sql.Time;

import ca.mcgill.ecse321.townlibrary.model.*;

public class ScheduleDTO {
    
    private DayOfWeek dayOfWeek;
    private Time startTime;
    private Time endTime;
    private Librarian librarian;
    private Library library;

    public ScheduleDTO(){}

    public ScheduleDTO(DayOfWeek dayOfWeek, Time startTime, Time endTime){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
    }

    public ScheduleDTO(DayOfWeek dayOfWeek, Time startTime, Time endTime, Librarian librarian){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
        this.librarian = librarian;
    }

    public ScheduleDTO(DayOfWeek dayOfWeek, Time startTime, Time endTime, Library library){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.library = library; 
    }

    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public Library getLibrary() {
        return library;
    }
    
    public static ScheduleDTO convertScheduleDTO(DailySchedule dailySchedule){
        DayOfWeek dayOfWeek = dailySchedule.getDayOfWeek();
        Time startTime = dailySchedule.getStartTime();
        Time endTime = dailySchedule.getEndTime();
        if (dailySchedule.getLibrary() != null){
            Library library = dailySchedule.getLibrary();
            return new ScheduleDTO(dayOfWeek, startTime, endTime, library);
        }
        else if (dailySchedule.getLibrarian() != null){
            Librarian librarian = dailySchedule.getLibrarian();
            return new ScheduleDTO(dayOfWeek, startTime, endTime, librarian);
        }
        else return new ScheduleDTO(dayOfWeek, startTime, endTime);
    }


}
