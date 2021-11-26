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

    // clears database
    @AfterEach
    public void cleanupDB() {
        this.dailyScheduleRepository.deleteAll();
        this.librarianRepository.deleteAll();
    }

    @Test
    public void testPersistDailySchedule(){
        // Initialize a librarian
        final Librarian librarian = new Librarian();
        this.librarianRepository.save(librarian);

        // Initialize a schedule
        final DailySchedule mondaySchedule = new DailySchedule();
        final Time startTime = new Time(0);
        final Time endTime = new Time(1000);

        // Write attributes
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setLibrarian(librarian);
        mondaySchedule.setStartTime(startTime);
        mondaySchedule.setEndTime(endTime);
        // Save to database
        this.dailyScheduleRepository.save(mondaySchedule);

        // save the id somewhere since it's autogenerated!
        final int id = mondaySchedule.getId();

        // Test to see if schedule persisted
        final DailySchedule schedule = this.dailyScheduleRepository.findById(id).get();
        Assertions.assertEquals(id, schedule.getId());
        Assertions.assertEquals(librarian.getId(), schedule.getLibrarian().getId());
        Assertions.assertEquals(startTime.toString(), schedule.getStartTime().toString());
        Assertions.assertEquals(endTime.toString(), schedule.getEndTime().toString());
        Assertions.assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());

    }

    @Test
    public void testQueryDailySchedule(){
        // Initialize a librarian
        final Librarian librarian = new Librarian();
        this.librarianRepository.save(librarian);

        // Initialize two schedules
        final DailySchedule mondaySchedule = new DailySchedule();
        final DailySchedule tuesdaySchedule = new DailySchedule();

        // Write their attributes and save them on the database
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        // mondaySchedule.setId(25);
        mondaySchedule.setLibrarian(librarian);
        this.dailyScheduleRepository.save(mondaySchedule);

        tuesdaySchedule.setDayOfWeek(DayOfWeek.TUESDAY);
        // tuesdaySchedule.setId(26);
        tuesdaySchedule.setLibrarian(librarian);
        this.dailyScheduleRepository.save(tuesdaySchedule);

        // Test query results by checking if it returns the right amount of schedules
        List<DailySchedule> persistDailySchedules = this.dailyScheduleRepository.findByLibrarian(librarian);
        Assertions.assertEquals(2, persistDailySchedules.size());
        persistDailySchedules = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.TUESDAY);
        Assertions.assertEquals(1, persistDailySchedules.size());
        persistDailySchedules = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.FRIDAY);
        Assertions.assertEquals(0, persistDailySchedules.size());
        persistDailySchedules = (List<DailySchedule>) this.dailyScheduleRepository.findAll();
        Assertions.assertEquals(2, persistDailySchedules.size());

    }

    @Test
    public void testDeleteDailySchedule(){
        // Initialize two schedules
        final DailySchedule mondaySchedule = new DailySchedule();
        final DailySchedule otherMondaySchedule = new DailySchedule();

        // Write their attributes and save them on the database
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        this.dailyScheduleRepository.save(mondaySchedule);
        final int sched1 = mondaySchedule.getId();

        otherMondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        this.dailyScheduleRepository.save(otherMondaySchedule);
        final int sched2 = otherMondaySchedule.getId();

        // Test if delete works
        List<DailySchedule> persistDailySchedules = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.MONDAY);
        Assertions.assertEquals(2, persistDailySchedules.size());

        // Delete one schedule out of two
        this.dailyScheduleRepository.delete(otherMondaySchedule);;
        persistDailySchedules = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, persistDailySchedules.size());



    }
}
