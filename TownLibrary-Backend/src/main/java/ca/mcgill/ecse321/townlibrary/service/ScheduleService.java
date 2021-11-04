package ca.mcgill.ecse321.townlibrary.service;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.townlibrary.repository.HeadLibrarianRepository;
import ca.mcgill.ecse321.townlibrary.repository.LibrarianRepository;
import ca.mcgill.ecse321.townlibrary.repository.LibraryRepository;

@Service
public class ScheduleService {
    
    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    // ask about these
    @Autowired 
    LibraryRepository libraryRepository;

    @Autowired
    LibrarianRepository librarianRepository;

    @Autowired
    HeadLibrarianRepository headLibrarianRepository;

    @Transactional
    public DailySchedule createDailySchedule(DayOfWeek dayOfWeek, Time startTime, Time endTime){
        DailySchedule dailySchedule = new DailySchedule();
        dailySchedule.setDayOfWeek(dayOfWeek);
        dailySchedule.setStartTime(startTime);
        dailySchedule.setEndTime(endTime);

        dailyScheduleRepository.save(dailySchedule);

        return dailySchedule;
    }

    @Transactional
    public List<DailySchedule> getAllDailySchedules(){
        // yeah idk im tired
        return StreamSupport.stream(dailyScheduleRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Transactional
    public List<DailySchedule> getDayOfWeekDailySchedules(DayOfWeek dayOfWeek){
        return dailyScheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    @Transactional
    public DailySchedule getLibraryDailySchedule(Library library){
        return dailyScheduleRepository.findByLibrary(library);
        // oh shoot, logistic error prob here haha
        // prob need to make findByLibrary return a list instead containing mon-sun schedules
    }

    @Transactional
    public DailySchedule getLibrarianDailySchedule(Librarian librarian, DayOfWeek dayOfWeek){
        List<DailySchedule> schedule = dailyScheduleRepository.findByLibrarian(librarian);
        for (DailySchedule dailySchedule : schedule){
            if (dailySchedule.getDayOfWeek().equals(dayOfWeek)) return dailySchedule;
        }
        throw new IllegalArgumentException("No schedule found.");
    }

    @Transactional
    public List<DailySchedule> getLibrarianDailySchedules(Librarian librarian){
        return dailyScheduleRepository.findByLibrarian(librarian);
    }

    @Transactional
    public DailySchedule assignSchedule(DailySchedule dailySchedule, Librarian librarian){
        dailySchedule.setLibrarian(librarian);
        dailyScheduleRepository.save(dailySchedule);
        return dailySchedule;
    }

    @Transactional
    public DailySchedule setLibrarySchedule(DailySchedule dailySchedule, Library library){
        dailySchedule.setLibrary(library);
        dailyScheduleRepository.save(dailySchedule);
        return dailySchedule;
    }


}   
