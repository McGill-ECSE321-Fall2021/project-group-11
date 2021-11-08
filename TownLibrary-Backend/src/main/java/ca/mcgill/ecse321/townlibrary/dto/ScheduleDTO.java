package ca.mcgill.ecse321.townlibrary.dto;

import java.sql.Time;

import ca.mcgill.ecse321.townlibrary.model.*;

public class ScheduleDTO {
    
    // change this later
    // use dto or id
    private DayOfWeek dayOfWeek;
    private Time startTime;
    private Time endTime;
    private int librarianId;
    private int libraryId;

    /** DTO constructor for schedule without library/librarian assigned
     * 
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     */
    public ScheduleDTO(DayOfWeek dayOfWeek, Time startTime, Time endTime){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
    }

    /** DTO constructor for librarian schedule
     * 
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     * @param librarianId
     */
    public ScheduleDTO(DayOfWeek dayOfWeek, Time startTime, Time endTime, int librarianId){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime; 
        this.librarianId = librarianId;
    }

    /** DTO constructor for library schedule
     * 
     * @param libraryId
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     */
    public ScheduleDTO(int libraryId, DayOfWeek dayOfWeek, Time startTime, Time endTime){
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.libraryId = libraryId; 
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
            return new ScheduleDTO(libraryId, dayOfWeek, startTime, endTime);
        }
        else if (dailySchedule.getLibrarian() != null){
            int librarianId = dailySchedule.getLibrarian().getId();
            return new ScheduleDTO(dayOfWeek, startTime, endTime, librarianId);
        }
        else return new ScheduleDTO(dayOfWeek, startTime, endTime);
    }


}
