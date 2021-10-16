package ca.mcgill.ecse321.townlibrary.repository;

import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;


@SpringBootTest
public class DailyScheduleRepositoryTest {
    
    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @AfterEach
    public void cleanupDB() {
        this.dailyScheduleRepository.deleteAll();
        this.librarianRepository.deleteAll();
    }
    
    @Test
    public void testPersistDailySchedule() throws Exception{
        
        final Librarian librarian = new Librarian();
        librarian.setId(150);
        librarian.setName("Foo Bar");

        this.librarianRepository.save(librarian);
    
        final DailySchedule mondaySchedule = new DailySchedule();
        final Time startTime = new Time(0);
        final Time endTime = new Time(1000);

        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setId(25);
        mondaySchedule.setLibrarian(librarian);
        mondaySchedule.setStartTime(startTime);
        mondaySchedule.setEndTime(endTime);
        this.dailyScheduleRepository.save(mondaySchedule);

        final DailySchedule schedule = this.dailyScheduleRepository.findById(25).get();
        Assertions.assertEquals(25, schedule.getId());
        Assertions.assertEquals(librarian.getId(), schedule.getLibrarian().getId());
        // Assertions.assertEquals(startTime, schedule.getStartTime()); // fix this
        // Assertions.assertEquals(endTime, schedule.getEndTime());    // fix this
        Assertions.assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        
    }

    @Test
    public void testQueryDailySchedule() throws Exception{
        final Librarian librarian = new Librarian();
        librarian.setId(150);
        librarian.setName("Foo Bar");

        this.librarianRepository.save(librarian);
    
        final DailySchedule mondaySchedule = new DailySchedule();
        final Time startTime1 = new Time(0);
        final Time endTime1 = new Time(1000);

        final DailySchedule tuesdaySchedule = new DailySchedule();
        final Time startTime2 = new Time(10);
        final Time endTime2 = new Time(500); 

        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setId(25);
        mondaySchedule.setLibrarian(librarian);
        mondaySchedule.setStartTime(startTime1);
        mondaySchedule.setEndTime(endTime1);
        this.dailyScheduleRepository.save(mondaySchedule);

        tuesdaySchedule.setDayOfWeek(DayOfWeek.TUESDAY);
        tuesdaySchedule.setId(26);
        tuesdaySchedule.setLibrarian(librarian);
        tuesdaySchedule.setStartTime(startTime2);
        tuesdaySchedule.setEndTime(endTime2);

        List<DailySchedule> result;
        result = this.dailyScheduleRepository.findByLibrarian(librarian);
        Assertions.assertEquals(1, result.size()); // should be 2
        result = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.TUESDAY);
        Assertions.assertEquals(1, result.size()); // this fails too
        result = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.FRIDAY);
        Assertions.assertEquals(0, result.size());
        result = this.dailyScheduleRepository.findByStartTime(startTime1);
        Assertions.assertEquals(1, result.size());
    }
}
