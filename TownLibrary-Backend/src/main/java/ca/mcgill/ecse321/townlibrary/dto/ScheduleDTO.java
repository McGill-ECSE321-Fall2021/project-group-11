package ca.mcgill.ecse321.townlibrary.dto;

import java.sql.Time;

import ca.mcgill.ecse321.townlibrary.model.*;

public class ScheduleDTO {
    
    private DayOfWeek dayOfWeek;
    private Time startTime;
    private Time endTime;
    private int librarianId;
    private int libraryId;
    private int id; 

    public ScheduleDTO(){}

    /** DTO constructor for schedule without library/librarian assigned
     * 
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     */
    public ScheduleDTO(int id, DayOfWeek dayOfWeek, Time startTime, Time endTime){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
        this.id = id;
    }

    /** DTO constructor for librarian schedule
     * 
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     * @param librarianId
     */
    public ScheduleDTO(int id, DayOfWeek dayOfWeek, Time startTime, Time endTime, int librarianId){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
        this.librarianId = librarianId;
        this.id = id;
    }

    /** DTO constructor for library schedule
     * 
     * @param libraryId
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     */
    public ScheduleDTO(int id, int libraryId, DayOfWeek dayOfWeek, Time startTime, Time endTime){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.libraryId = libraryId; 
        this.id = id;
    }

    // getters and setters
    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getLibrarianId() {
        return librarianId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public int getId(){
        return id;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    /** Converts DailySchedule to ScheduleDTO
     * 
     * @param dailySchedule
     * @return ScheduleDTO
     */
    public static ScheduleDTO convertScheduleDTO(DailySchedule dailySchedule){
        DayOfWeek dayOfWeek = dailySchedule.getDayOfWeek();
        Time startTime = dailySchedule.getStartTime();
        Time endTime = dailySchedule.getEndTime();
        if (dailySchedule.getLibrary() != null){
            int libraryId = dailySchedule.getLibrary().getId();
            return new ScheduleDTO(dailySchedule.getId(), libraryId, dayOfWeek, startTime, endTime);
        }
        else if (dailySchedule.getLibrarian() != null){
            int librarianId = dailySchedule.getLibrarian().getId();
            return new ScheduleDTO(dailySchedule.getId(), dayOfWeek, startTime, endTime, librarianId);
        }
        else return new ScheduleDTO(dailySchedule.getId(), dayOfWeek, startTime, endTime);
    }


}
