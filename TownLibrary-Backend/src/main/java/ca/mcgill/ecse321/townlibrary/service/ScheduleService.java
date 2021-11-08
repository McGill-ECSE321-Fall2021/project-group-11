package ca.mcgill.ecse321.townlibrary.service;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

@Service
public class ScheduleService {
    
    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    // @Autowired 
    // LibraryRepository libraryRepository;

    // @Autowired
    // LibrarianRepository librarianRepository;

    // @Autowired
    // HeadLibrarianRepository headLibrarianRepository;

    @Transactional
    public DailySchedule createDailySchedule(DayOfWeek dayOfWeek, Time startTime, Time endTime){
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
            DailySchedule dailySchedule = new DailySchedule();
            dailySchedule.setDayOfWeek(dayOfWeek);
            dailySchedule.setStartTime(startTime);
            dailySchedule.setEndTime(endTime);
    
            dailyScheduleRepository.save(dailySchedule);
    
            return dailySchedule;
        }
        
    }

    @Transactional
    public List<DailySchedule> getAllDailySchedules(){
        List<DailySchedule> schedules = StreamSupport.stream(dailyScheduleRepository.findAll().spliterator(), false).collect(Collectors.toList());
        // if (schedules.size()==0) throw new IllegalArgumentException("No schedule found.");
        // else 
        return schedules;
    }

    @Transactional
    public List<DailySchedule> getDayOfWeekDailySchedules(DayOfWeek dayOfWeek){
        List<DailySchedule> schedules = dailyScheduleRepository.findByDayOfWeek(dayOfWeek);
        //if (schedules.size()==0) throw new IllegalArgumentException("No schedule found.");
        //else
        return schedules;
    }

    @Transactional
    public List<DailySchedule> getLibraryDailySchedule(Library library){
        List<DailySchedule> schedules = dailyScheduleRepository.findByLibrary(library);
        //if (schedules.size()==0) throw new IllegalArgumentException("No schedule found.");
        //else
        return schedules;
    }

    @Transactional
    public DailySchedule getLibrarianDailyScheduleByDayOfWeek(Librarian librarian, DayOfWeek dayOfWeek){
        List<DailySchedule> schedule = dailyScheduleRepository.findByLibrarian(librarian);
        for (DailySchedule dailySchedule : schedule){
            if (dailySchedule.getDayOfWeek().equals(dayOfWeek)) return dailySchedule;
        }
        // not sure whether to return null or throw exception
        // throw new IllegalArgumentException("NO-SCHEDULE");
        return null;
    }

    @Transactional
    public List<DailySchedule> getLibrarianDailySchedules(Librarian librarian){
        List<DailySchedule> schedules = dailyScheduleRepository.findByLibrarian(librarian);
        //if (schedules.size()==0) throw new IllegalArgumentException("No schedule found.");
        //else
        return schedules;
    }

    @Transactional
    public void assignSchedule(DailySchedule dailySchedule, Librarian librarian){
        if (dailySchedule == null) throw new IllegalArgumentException("NULL-SCHEDULE");
        for (DailySchedule schedule:dailyScheduleRepository.findByLibrarian(librarian)){
            if (
                schedule.getDayOfWeek().equals(dailySchedule.getDayOfWeek()) ||
                (schedule.getStartTime().getTime() < dailySchedule.getEndTime().getTime() &&
                 schedule.getStartTime().getTime() > dailySchedule.getStartTime().getTime()) ||
                (dailySchedule.getStartTime().getTime() > schedule.getStartTime().getTime() &&
                 dailySchedule.getEndTime().getTime() < schedule.getEndTime().getTime()) ||
                (schedule.getEndTime().getTime() > dailySchedule.getStartTime().getTime() &&
                 schedule.getEndTime().getTime() < dailySchedule.getEndTime().getTime())
            ) throw new IllegalArgumentException("OVERLAP-SCHEDULE-ASSIGNMENT");
        }
        dailySchedule.setLibrarian(librarian);
        dailyScheduleRepository.save(dailySchedule);
        //return dailySchedule;
    }

    @Transactional
    public void setLibrarySchedule(List<DailySchedule> dailySchedules, Library library){
        if (library == null) throw new IllegalArgumentException("NULL-LIBRARY");
        if (dailySchedules.size() != 7) throw new IllegalArgumentException("NOT-WEEK-LIBRARY-SCHEDULE");
        // changing library schedule means removing all previous daily schedules (week schedule from mon-sun)
        // int i = 0;
        for(DailySchedule oldSchedule:dailyScheduleRepository.findByLibrary(library)){
            if (oldSchedule != null) dailyScheduleRepository.delete(oldSchedule);
            // dailyScheduleRepository.delete(schedule);
            // schedule = dailySchedules.get(i++);
            // if (schedule != null) schedule.setLibrary(library);
            // dailyScheduleRepository.save(schedule);
            
        }
        // and saving a new set of daily schedules to replace them
        for (DailySchedule newSchedule:dailySchedules){
            if (newSchedule != null) {
                newSchedule.setLibrary(library);
                dailyScheduleRepository.save(newSchedule);
            }
        }
    }

    @Transactional
    public void updateSchedule(DailySchedule dailySchedule, DayOfWeek newDayOfWeek, Time newStartTime, Time newEndTime){
        if (dailySchedule == null) throw new IllegalArgumentException("NO-SCHEDULE");
        if (newDayOfWeek == null) throw new IllegalArgumentException("NULL-DAY-OF-WEEK");
        if (newStartTime == null || newEndTime == null) throw new IllegalArgumentException("NULL-TIME");
        if (newStartTime.after(newEndTime)) throw new IllegalArgumentException("START-TIME-AFTER-END-TIME");
        dailyScheduleRepository.delete(dailySchedule);
        dailySchedule.setDayOfWeek(newDayOfWeek);
        dailySchedule.setStartTime(newStartTime);
        dailySchedule.setEndTime(newEndTime);
        dailyScheduleRepository.save(dailySchedule);
    }


}   
