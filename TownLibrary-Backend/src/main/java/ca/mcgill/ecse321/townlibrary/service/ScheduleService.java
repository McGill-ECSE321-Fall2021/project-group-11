package ca.mcgill.ecse321.townlibrary.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

@Service
public class ScheduleService {
    
    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    @Autowired 
    LibraryRepository libraryRepository;

    @Autowired
    LibrarianRepository librarianRepository;

    /** Creates an instance of DailySchedule for a library
     * 
     * @param libraryId
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     * @return dailySchedule - DailySchedule for a given Library
     */
    @Transactional
    public DailySchedule createLibrarySchedule(int libraryId, DayOfWeek dayOfWeek, Time startTime, Time endTime){
        if (!libraryRepository.findById(libraryId).isPresent()) throw new IllegalArgumentException("NO-LIBRARY");
        if (dayOfWeek == (null)) throw new IllegalArgumentException("NULL-DAY-OF-WEEK");
        if (startTime == (null) || endTime == (null)) throw new IllegalArgumentException("NULL-TIME");
        if (startTime.after(endTime)) throw new IllegalArgumentException("START-TIME-AFTER-END-TIME");
        
        Library library = libraryRepository.findById(libraryId).get();
        DailySchedule dailySchedule = new DailySchedule();
        dailySchedule.setLibrary(library);
        dailySchedule.setDayOfWeek(dayOfWeek);
        dailySchedule.setStartTime(startTime);
        dailySchedule.setEndTime(endTime);

        dailyScheduleRepository.save(dailySchedule);

        return dailySchedule;
        
    }

    /** Creates an instance of DailySchedule for librarians
     * 
     * @param librarianId
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     * @return dailySchedule - DailySchedule for a given Librarian
     */
    @Transactional
    public DailySchedule createLibrarianSchedule(int librarianId, DayOfWeek dayOfWeek, Time startTime, Time endTime){
        if (!librarianRepository.findById(librarianId).isPresent()){
            throw new IllegalArgumentException("NO-LIBRARIAN");
        }
        if (dayOfWeek == (null)){
            throw new IllegalArgumentException("NULL-DAY-OF-WEEK");
        }
        if (startTime == (null) || endTime == (null)){
            throw new IllegalArgumentException("NULL-TIME");
        }
        if (startTime.after(endTime)){
            throw new IllegalArgumentException("START-TIME-AFTER-END-TIME");
        }
        else{
            Librarian librarian = librarianRepository.findLibrarianById(librarianId);
            DailySchedule dailySchedule = new DailySchedule();
            dailySchedule.setLibrarian(librarian);
            dailySchedule.setDayOfWeek(dayOfWeek);
            dailySchedule.setStartTime(startTime);
            dailySchedule.setEndTime(endTime);
    
            dailyScheduleRepository.save(dailySchedule);
    
            return dailySchedule;
        }
    }

    /** Gets all schedules of given library
     * 
     * @param libraryId
     * @return schedules - list of library's schedules
     */
    @Transactional
    public List<DailySchedule> getLibrarySchedules(int libraryId){
        List<DailySchedule> schedules = dailyScheduleRepository.findByLibrary(libraryRepository.findById(libraryId).get());
        return schedules;
    }

    /** Gets library schedule on a given day
     * 
     * @param libraryId
     * @param dayOfWeek
     * @return dailySchedule - DailySchedule of a given library on a given day
     */
    @Transactional
    public DailySchedule getLibraryScheduleByDay(int libraryId, DayOfWeek dayOfWeek){
        if (!libraryRepository.findById(libraryId).isPresent()) throw new IllegalArgumentException("NO-LIBRARY");
        if (dayOfWeek == null) throw new IllegalArgumentException("NULL-DAY-OF-WEEK");
        List<DailySchedule> schedule = dailyScheduleRepository.findByLibrary(libraryRepository.findById(libraryId).get());
        for (DailySchedule dailySchedule : schedule){
            if (dailySchedule.getDayOfWeek().equals(dayOfWeek)) return dailySchedule;
        }
        // not sure whether to return null or throw exception
        throw new IllegalArgumentException("NO-SCHEDULE");
        // return null;
    }

    /** Gets all schedules of given librarian 
     * 
     * @param librarianId
     * @return schedules - list of DailySchedule representing all of given librarian's schedules
     */
    @Transactional
    public List<DailySchedule> getLibrarianSchedules(int librarianId){
        List<DailySchedule> schedules = dailyScheduleRepository.findByLibrarian(librarianRepository.findLibrarianById(librarianId));
        if (schedules.size()==0) throw new IllegalArgumentException("NO-SCHEDULE");
        else return schedules;
    }

    /** Gets librarian schedule on given day
     * 
     * @param librarianId
     * @param dayOfWeek
     * @return dailySchedule - DailySchedule representing librarian schedule on given day
     */
    @Transactional
    public DailySchedule getLibrarianScheduleByDay(int librarianId, DayOfWeek dayOfWeek){
        if (!librarianRepository.findById(librarianId).isPresent()) throw new IllegalArgumentException("NO-LIBRARIAN");
        if (dayOfWeek == null) throw new IllegalArgumentException("NULL-DAY-OF-WEEK");
        List<DailySchedule> schedule = dailyScheduleRepository.findByLibrarian(librarianRepository.findLibrarianById(librarianId));
        for (DailySchedule dailySchedule : schedule){
            if (dailySchedule.getDayOfWeek().equals(dayOfWeek)) return dailySchedule;
        }
        // not sure whether to return null or throw exception
        throw new IllegalArgumentException("NO-SCHEDULE");
        // return null;
    }
    
    /** Assigns a schedule to a librarian 
     * 
     * @param dailySchedule
     * @param librarianId
     */
    @Transactional
    public void assignSchedule(int dailyScheduleId, int librarianId){
        if (!dailyScheduleRepository.findById(dailyScheduleId).isPresent()) throw new IllegalArgumentException("NO-SCHEDULE");
        DailySchedule dailySchedule = dailyScheduleRepository.findById(dailyScheduleId).get();
        for (DailySchedule schedule:dailyScheduleRepository.findByLibrarian(librarianRepository.findById(librarianId).get())){
            // checks whether the time block (start time to end time) interfers with any of the librarian's existing time blocks
            // i.e. no overlap with currently assigned schedules (not same day or within time block)
            // NOTE: written above is what it originally was (hence commented out guard below)
            // changed to check if the days or the same (assuming librarian can only have 1 schedule per day)
            if (
                schedule.getDayOfWeek().equals(dailySchedule.getDayOfWeek()) 
                // ||
                // (schedule.getStartTime().getTime() < dailySchedule.getEndTime().getTime() &&
                //  schedule.getStartTime().getTime() > dailySchedule.getStartTime().getTime()) ||
                // (dailySchedule.getStartTime().getTime() > schedule.getStartTime().getTime() &&
                //  dailySchedule.getEndTime().getTime() < schedule.getEndTime().getTime()) ||
                // (schedule.getEndTime().getTime() > dailySchedule.getStartTime().getTime() &&
                //  schedule.getEndTime().getTime() < dailySchedule.getEndTime().getTime())
            ) throw new IllegalArgumentException("OVERLAP-SCHEDULE-ASSIGNMENT");
        }
        dailySchedule.setLibrarian(librarianRepository.findLibrarianById(librarianId));
        dailyScheduleRepository.save(dailySchedule);
    }

    /** Set opening/closing hours for a library (weekly schedule)
     * 
     * @param dailySchedulesIds
     * @param libraryId
     */
    @Transactional
    public void setLibrarySchedule(List<Integer> dailyScheduleIds, int libraryId){
        if (!libraryRepository.findById(libraryId).isPresent()) throw new IllegalArgumentException("NO-LIBRARY");
        if (dailyScheduleIds.size() != 7) throw new IllegalArgumentException("NOT-WEEK-LIBRARY-SCHEDULE");
        // changing library schedule means removing all previous daily schedules (week schedule from mon-sun)
        for(DailySchedule oldSchedule:dailyScheduleRepository.findByLibrary(libraryRepository.findById(libraryId).get())){
            if (oldSchedule != null) dailyScheduleRepository.delete(oldSchedule);
        }
        // and saving a new set of daily schedules to replace them
        for (int newScheduleId:dailyScheduleIds){
            // not sure how this is going to play out
            DailySchedule newSchedule = dailyScheduleRepository.findById(newScheduleId).get();
            if (newSchedule != null) {
                newSchedule.setLibrary(libraryRepository.findById(libraryId).get());
                dailyScheduleRepository.save(newSchedule);
            }
        }
    }

    /** Update an existing schedule with different values
     * 
     * @param dailyScheduleId
     * @param newDayOfWeek
     * @param newStartTime
     * @param newEndTime
     */
    @Transactional
    public void updateSchedule(int dailyScheduleId, Time newStartTime, Time newEndTime){
        if (!dailyScheduleRepository.findById(dailyScheduleId).isPresent()) throw new IllegalArgumentException("NO-SCHEDULE");
        if (newStartTime == null || newEndTime == null) throw new IllegalArgumentException("NULL-TIME");
        if (newStartTime.after(newEndTime)) throw new IllegalArgumentException("START-TIME-AFTER-END-TIME");
        DailySchedule dailySchedule = dailyScheduleRepository.findById(dailyScheduleId).get();
        dailySchedule.setStartTime(newStartTime);
        dailySchedule.setEndTime(newEndTime);
        dailyScheduleRepository.save(dailySchedule);
    }


}   
