package ca.mcgill.ecse321.townlibrary.repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import ca.mcgill.ecse321.townlibrary.model.*;


@SpringBootTest
public class DailyScheduleRepositoryTest {
    
    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    @AfterEach
    public void cleanupDB() {
        this.dailyScheduleRepository.deleteAll();
    }
    
    @Test
    public void testPersistDailySchedule() throws Exception{
        
        final Librarian librarian = new Librarian();
        librarian.setId(1);
        librarian.setName("Foo Bar");
    
        final DailySchedule mondaySchedule = new DailySchedule();
        final Time startTime = new Time(0);
        final Time endTime = new Time(1000);

        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setId(1);
        mondaySchedule.setLibrarian(librarian);
        mondaySchedule.setStartTime(startTime);
        mondaySchedule.setEndTime(endTime);
        this.dailyScheduleRepository.save(mondaySchedule);

        final DailySchedule schedule = this.dailyScheduleRepository.findById(1).get();
        Assertions.assertEquals(1, schedule.getId());
        Assertions.assertEquals(librarian, schedule.getLibrarian());
        Assertions.assertEquals(startTime, schedule.getStartTime());
        Assertions.assertEquals(endTime, schedule.getEndTime());
        Assertions.assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());


        
    }

    @Test
    public void testQueryDailySchedule() throws Exception{
        final Librarian librarian = new Librarian();
        librarian.setId(1);
        librarian.setName("Foo Bar");
    
        final DailySchedule mondaySchedule = new DailySchedule();
        final Time startTime1 = new Time(0);
        final Time endTime1 = new Time(1000);

        final DailySchedule tuesdaySchedule = new DailySchedule();
        final Time startTime2 = new Time(10);
        final Time endTime2 = new Time(500); 

        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setLibrarian(librarian);
        mondaySchedule.setStartTime(startTime1);
        mondaySchedule.setEndTime(endTime1);
        this.dailyScheduleRepository.save(mondaySchedule);

        tuesdaySchedule.setDayOfWeek(DayOfWeek.TUESDAY);
        tuesdaySchedule.setLibrarian(librarian);
        tuesdaySchedule.setStartTime(startTime2);
        tuesdaySchedule.setEndTime(endTime2);

        List<DailySchedule> result;
        result = this.dailyScheduleRepository.findByLibrarian(librarian);
        Assertions.assertEquals(2, result.size());
        result = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.TUESDAY);
        Assertions.assertEquals(1, result.size());
        result = this.dailyScheduleRepository.findByDayOfWeek(DayOfWeek.FRIDAY);
        Assertions.assertEquals(0, result.size());
        result = this.dailyScheduleRepository.findByStartTime(startTime1);
        Assertions.assertEquals(1, result.size());
    }
}
