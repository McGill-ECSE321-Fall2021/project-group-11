package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.catalina.authenticator.DigestAuthenticator.NonceInfo;
import org.codehaus.groovy.runtime.powerassert.AssertionRenderer;
import org.hibernate.property.access.spi.GetterFieldImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.townlibrary.model.DailySchedule;
import ca.mcgill.ecse321.townlibrary.model.DayOfWeek;
import ca.mcgill.ecse321.townlibrary.model.Librarian;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.repository.DailyScheduleRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    final private Library LIBRARY = new Library();
    final private Librarian LIBRARIAN = new Librarian();
    final private Time START_TIME = Time.valueOf("08:00:00"); 
    final private Time END_TIME = Time.valueOf("12:00:00");
    private ArrayList<DailySchedule> librarySchedule = new ArrayList<DailySchedule>();
    // YEP
    @Mock
    private DailyScheduleRepository mockDailyScheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;


    @BeforeEach
    public void setMockOutput(){
        lenient().when(mockDailyScheduleRepository.findByDayOfWeek(any(DayOfWeek.class))).thenAnswer((InvocationOnMock invocation)->{
            if (invocation.getArgument(0).equals(DayOfWeek.MONDAY)){
                ArrayList<DailySchedule> mondaySchedule = new ArrayList<DailySchedule>();
                DailySchedule schedule1 = new DailySchedule();
                DailySchedule schedule2 = new DailySchedule();
                schedule1.setDayOfWeek(DayOfWeek.MONDAY);
                schedule2.setDayOfWeek(DayOfWeek.MONDAY);
                mondaySchedule.add(schedule1);
                mondaySchedule.add(schedule2);
                return mondaySchedule;
            }else if (invocation.getArgument(0).equals(DayOfWeek.FRIDAY)){
                ArrayList<DailySchedule> fridaySchedule = new ArrayList<DailySchedule>();
                DailySchedule schedule = new DailySchedule();
                schedule.setDayOfWeek(DayOfWeek.FRIDAY);
                fridaySchedule.add(schedule);
                return fridaySchedule;
            }
            else return new ArrayList<DailySchedule>();
        });
        lenient().when(mockDailyScheduleRepository.findByLibrary(any(Library.class))).thenAnswer((InvocationOnMock invocation)->{
            if (invocation.getArgument(0).equals(LIBRARY)){
                ArrayList<DailySchedule> librarySchedule = new ArrayList<DailySchedule>();
                DailySchedule schedule = new DailySchedule();
                for (int i = 0; i<7; i++){
                    switch(i){
                        case 0:
                            schedule.setDayOfWeek(DayOfWeek.MONDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 1:
                            schedule.setDayOfWeek(DayOfWeek.TUESDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 2:
                            schedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 3:
                            schedule.setDayOfWeek(DayOfWeek.THURSDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 4:
                            schedule.setDayOfWeek(DayOfWeek.FRIDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 5:
                            schedule.setDayOfWeek(DayOfWeek.SATURDAY);
                            librarySchedule.add(schedule);
                            break;
                        case 6:
                            schedule.setDayOfWeek(DayOfWeek.SUNDAY);
                            librarySchedule.add(schedule);
                            break;
                    }
                    schedule = new DailySchedule();
                }
                return librarySchedule;
            }
            else return new ArrayList<DailySchedule>(); 
        });
        lenient().when(mockDailyScheduleRepository.findByLibrarian(any(Librarian.class))).thenAnswer((InvocationOnMock invocation)->{
            if (invocation.getArgument(0).equals(LIBRARIAN)){
                ArrayList<DailySchedule> librarianSchedule = new ArrayList<DailySchedule>();
                DailySchedule monday = new DailySchedule();
                monday.setDayOfWeek(DayOfWeek.MONDAY);
                monday.setStartTime(START_TIME);
                monday.setEndTime(END_TIME);
                DailySchedule tuesday = new DailySchedule();
                tuesday.setDayOfWeek(DayOfWeek.TUESDAY);
                tuesday.setStartTime(START_TIME);
                tuesday.setEndTime(END_TIME);
                librarianSchedule.add(monday);
                librarianSchedule.add(tuesday);
                return librarianSchedule;
            }
            else return new ArrayList<DailySchedule>();
        });
        
        Answer<?> returnParamAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(mockDailyScheduleRepository.save(any(DailySchedule.class))).thenAnswer(returnParamAsAnswer);
    }

    @Test
    public void testCreateSchedule(){
        // maybe change to HH:mm
        Time startTime = Time.valueOf("08:00:00"); 
        Time endTime = Time.valueOf("12:00:00");
        DailySchedule dailySchedule = null;
        try {
            dailySchedule = scheduleService.createDailySchedule(DayOfWeek.MONDAY, startTime, endTime);
        } catch (IllegalArgumentException ex) {
            fail();
        }

        assertNotNull(dailySchedule);
        assertEquals(DayOfWeek.MONDAY, dailySchedule.getDayOfWeek());
        assertEquals(startTime, dailySchedule.getStartTime());
        assertEquals(endTime, dailySchedule.getEndTime());
    }

    @Test
    public void testCreateScheduleWithNullArguments(){
        Time startTime = Time.valueOf("13:00:00"); 
        Time endTime = Time.valueOf("12:00:00");
        try {
            scheduleService.createDailySchedule(null, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-DAY-OF-WEEK", e.getMessage());
        }
        try {
            scheduleService.createDailySchedule(DayOfWeek.MONDAY, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("START-TIME-AFTER-END-TIME", e.getMessage());
        }
        try {
            scheduleService.createDailySchedule(DayOfWeek.MONDAY, null, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
        try {
            scheduleService.createDailySchedule(DayOfWeek.MONDAY, startTime, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
    }

    @Test
    public void testGetDayOfWeekDailySchedule(){
        assertEquals(2, scheduleService.getDayOfWeekDailySchedules(DayOfWeek.MONDAY).size());
        assertEquals(1, scheduleService.getDayOfWeekDailySchedules(DayOfWeek.FRIDAY).size());
        assertEquals(0, scheduleService.getDayOfWeekDailySchedules(DayOfWeek.TUESDAY).size());
    }

    @Test
    public void testGetLibraryDailySchedule(){
        assertEquals(7, scheduleService.getLibraryDailySchedule(LIBRARY).size());
        assertEquals(0, scheduleService.getLibraryDailySchedule(null).size());
    }

    @Test
    public void testGetLibrarianDailySchedules(){
        assertEquals(2, scheduleService.getLibrarianDailySchedules(LIBRARIAN).size());
        assertEquals(0, scheduleService.getLibrarianDailySchedules(null).size());
    }

    @Test
    public void testGetLibrarianDailyScheduleByDayOfWeek(){
        assertEquals(DayOfWeek.MONDAY, scheduleService.getLibrarianDailyScheduleByDayOfWeek(LIBRARIAN, DayOfWeek.MONDAY).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, scheduleService.getLibrarianDailyScheduleByDayOfWeek(LIBRARIAN, DayOfWeek.TUESDAY).getDayOfWeek());
        assertEquals(null, scheduleService.getLibrarianDailyScheduleByDayOfWeek(LIBRARIAN, DayOfWeek.FRIDAY));
        assertEquals(null, scheduleService.getLibrarianDailyScheduleByDayOfWeek(null, DayOfWeek.TUESDAY));
        

        // if we use throw instead of returning null
        // try{
        //     scheduleService.getLibrarianDailyScheduleByDayOfWeek(LIBRARIAN, DayOfWeek.FRIDAY);
        //     fail();
        // }catch (IllegalArgumentException e){
        //     assertEquals("NO-SCHEDULE", e.getMessage());
        // }
        // try{
        //     scheduleService.getLibrarianDailyScheduleByDayOfWeek(null, DayOfWeek.MONDAY);
        //     fail();
        // }catch (IllegalArgumentException e){
        //     assertEquals("NO-SCHEDULE", e.getMessage());
        // }

    }

    @Test
    public void testAssignSchedule(){
        DailySchedule nonConflictingDailySchedule = new DailySchedule();
        DailySchedule conflictDailySchedule = new DailySchedule();
        
        nonConflictingDailySchedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        nonConflictingDailySchedule.setStartTime(START_TIME);
        nonConflictingDailySchedule.setEndTime(END_TIME);
        
        conflictDailySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        conflictDailySchedule.setStartTime(START_TIME);
        conflictDailySchedule.setEndTime(END_TIME);
        
        try {
            scheduleService.assignSchedule(nonConflictingDailySchedule, LIBRARIAN);
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            scheduleService.assignSchedule(conflictDailySchedule, LIBRARIAN);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("OVERLAP-SCHEDULE-ASSIGNMENT", e.getMessage());
        }
    }

    @Test
    public void testFailSetLibrarySchedule(){
        ArrayList<DailySchedule> newSchedule = new ArrayList<DailySchedule>();
        DailySchedule schedule = new DailySchedule();
        for (int i = 0; i<5; i++){
            switch(i){
                case 0:
                    schedule.setDayOfWeek(DayOfWeek.MONDAY);
                    newSchedule.add(schedule);
                    break;
                case 1:
                    schedule.setDayOfWeek(DayOfWeek.TUESDAY);
                    newSchedule.add(schedule);
                    break;
                case 2:
                    schedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
                    newSchedule.add(schedule);

                    break;
                case 3:
                    schedule.setDayOfWeek(DayOfWeek.THURSDAY);
                    newSchedule.add(schedule);
                    break;
                case 4:
                    schedule.setDayOfWeek(DayOfWeek.FRIDAY);
                    newSchedule.add(schedule);
                    break;
                // case 5:
                //     // schedule.setDayOfWeek(DayOfWeek.SATURDAY);
                //     newSchedule.add(null);
                //     break;
                // case 6:
                //     // schedule.setDayOfWeek(DayOfWeek.SUNDAY);
                //     newSchedule.add(null);
                //     break;
            }
            schedule = new DailySchedule();
        }
        try{
            scheduleService.setLibrarySchedule(newSchedule, null);
            fail();
        }catch(IllegalArgumentException e){
            assertEquals("NULL-LIBRARY", e.getMessage());
        }
        try {
            scheduleService.setLibrarySchedule(newSchedule, LIBRARY);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NOT-WEEK-LIBRARY-SCHEDULE", e.getMessage());
        }


    }

    @Test
    public void testFailUpdateSchedule(){
        try {
            scheduleService.updateSchedule(null, DayOfWeek.MONDAY, START_TIME, END_TIME);
        } catch (IllegalArgumentException e) {
            assertEquals("NO-SCHEDULE", e.getMessage());
        }
        try {
            scheduleService.updateSchedule(new DailySchedule(), null, START_TIME, END_TIME);
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-DAY-OF-WEEK", e.getMessage());
        }
        try {
            scheduleService.updateSchedule(new DailySchedule(), DayOfWeek.MONDAY, null, END_TIME);
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
        try {
            scheduleService.updateSchedule(new DailySchedule(), DayOfWeek.MONDAY, END_TIME, START_TIME);
        } catch (IllegalArgumentException e) {
            assertEquals("START-TIME-AFTER-END-TIME", e.getMessage());
        }
    }

    
}
