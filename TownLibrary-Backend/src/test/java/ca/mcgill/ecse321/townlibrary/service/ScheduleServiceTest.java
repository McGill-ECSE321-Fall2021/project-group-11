package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

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
import ca.mcgill.ecse321.townlibrary.repository.LibrarianRepository;
import ca.mcgill.ecse321.townlibrary.repository.LibraryRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    // final & private fields for testing purposes
    final private Library LIBRARY = new Library();
    final private Librarian LIBRARIAN = new Librarian();
    final private Time START_TIME = Time.valueOf("08:00:00"); 
    final private Time END_TIME = Time.valueOf("12:00:00");

    @Mock
    private DailyScheduleRepository mockDailyScheduleRepository;

    @Mock
    private LibrarianRepository mockLibrarianRepository;

    @Mock
    private LibraryRepository mockLibraryRepository;

    @InjectMocks
    private ScheduleService scheduleService;
     
    // Setup for each test
    @BeforeEach
    public void setMockOutput(){
        // handles DailyScheduleRepository.findById calls
        lenient().when(mockDailyScheduleRepository.findById(anyInt())).thenAnswer((InvocationOnMock invocation)-> {
            if (Integer.valueOf(invocation.getArgument(0).toString()) < 20){ // arbitrary number of schedules
                DailySchedule dailySchedule = new DailySchedule();
                dailySchedule.setId(Integer.valueOf(invocation.getArgument(0).toString()));
                // even id will represent wednesdays
                if (dailySchedule.getId() % 2 == 0) dailySchedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
                // odd id will represent mondays for testing purposes
                else dailySchedule.setDayOfWeek(DayOfWeek.MONDAY);
                return Optional.of(dailySchedule);
            }else return Optional.empty();
        });
        // handles DailyScheduleRepository.findByLibrary calls
        lenient().when(mockDailyScheduleRepository.findByLibrary(any(Library.class))).thenAnswer((InvocationOnMock invocation)->{
            if (invocation.getArgument(0).equals(LIBRARY)){
                return createWeekSchedule();
            }else return new ArrayList<DailySchedule>(); 
        });
        // handles DailyScheduleRepository.findByLibrarian calls
        lenient().when(mockDailyScheduleRepository.findByLibrarian(any(Librarian.class))).thenAnswer((InvocationOnMock invocation)->{
            // if test librarian, create 2 schedules assigned to test librarian
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
        // handles LibraryRepository.findById calls; returns an optional instance of test library
        lenient().when(mockLibraryRepository.findById(0)).thenAnswer((InvocationOnMock invocation)-> Optional.of(LIBRARY));
        // handles LibrarianRepository.findById calls; returns an optional instance of test librarian
        lenient().when(mockLibrarianRepository.findById(0)).thenAnswer((InvocationOnMock invocation)-> Optional.of(LIBRARIAN));
        // handles LibrarianRepository.findLibrarianById calls; returns aninstance of test librarian if the ids match, else null
        lenient().when(mockLibrarianRepository.findLibrarianById(0)).thenAnswer((InvocationOnMock invocation)->{
            if (invocation.getArgument(0).equals(LIBRARIAN.getId())){
                return LIBRARIAN;
            }else return null;
        });
        
        Answer<?> returnParamAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        // save answer
        lenient().when(mockDailyScheduleRepository.save(any(DailySchedule.class))).thenAnswer(returnParamAsAnswer);
    }
    
    // Tests for each service below
    @Test
    public void testCreateSchedule(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        DailySchedule dailySchedule = null;
        try {
            dailySchedule = scheduleService.createSchedule(DayOfWeek.MONDAY, startTime, endTime);
        } catch (IllegalArgumentException exception) {
            fail();
        }
        assertNotNull(dailySchedule);
        assertEquals(DayOfWeek.MONDAY, dailySchedule.getDayOfWeek());
        assertEquals(startTime, dailySchedule.getStartTime());
        assertEquals(endTime, dailySchedule.getEndTime());
    }

    @Test
    public void testNullInputCreateSchedule(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        try {
            scheduleService.createSchedule(null, startTime, endTime);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-DAY-OF-WEEK", exception.getMessage());
        }
        try {
            scheduleService.createSchedule(DayOfWeek.MONDAY, null, endTime);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-TIME", exception.getMessage());
        }
        try {
            scheduleService.createSchedule(DayOfWeek.MONDAY, startTime, null);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-TIME", exception.getMessage());
        }
        try {
            scheduleService.createSchedule(DayOfWeek.MONDAY, endTime, startTime);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("START-TIME-AFTER-END-TIME", exception.getMessage());
        }
    }

    @Test
    public void testCreateLibrarianSchedule(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        int librarianId = LIBRARIAN.getId();
        DailySchedule dailySchedule = null;
        try {
            dailySchedule = scheduleService.createLibrarianSchedule(librarianId, DayOfWeek.MONDAY, startTime, endTime);
        } catch (IllegalArgumentException exception) {
            fail();
        }
        assertNotNull(dailySchedule);
        assertEquals(librarianId, dailySchedule.getLibrarian().getId());
        assertEquals(DayOfWeek.MONDAY, dailySchedule.getDayOfWeek());
        assertEquals(startTime, dailySchedule.getStartTime());
        assertEquals(endTime, dailySchedule.getEndTime());
    }

    @Test
    public void testCreateLibrarianScheduleWithBadArguments(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        int librarianId = 0;
        try {
            scheduleService.createLibrarianSchedule(5, DayOfWeek.MONDAY, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NO-LIBRARIAN", e.getMessage());
        }
        try {
            scheduleService.createLibrarianSchedule(librarianId, null, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-DAY-OF-WEEK", e.getMessage());
        }
        try {
            scheduleService.createLibrarianSchedule(librarianId, DayOfWeek.MONDAY, endTime, startTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("START-TIME-AFTER-END-TIME", e.getMessage());
        }
        try {
            scheduleService.createLibrarianSchedule(librarianId, DayOfWeek.MONDAY, null, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
        try {
            scheduleService.createLibrarianSchedule(librarianId, DayOfWeek.MONDAY, startTime, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
    }

    @Test
    public void testCreateLibrarySchedule(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        int libraryId = LIBRARY.getId();
        DailySchedule dailySchedule = null;
        try {
            dailySchedule = scheduleService.createLibrarySchedule(libraryId, DayOfWeek.MONDAY, startTime, endTime);
        } catch (IllegalArgumentException exception) {
            fail();
        }
        assertNotNull(dailySchedule);
        assertEquals(libraryId, dailySchedule.getLibrary().getId());
        assertEquals(DayOfWeek.MONDAY, dailySchedule.getDayOfWeek());
        assertEquals(startTime, dailySchedule.getStartTime());
        assertEquals(endTime, dailySchedule.getEndTime());
    }

    @Test
    public void testCreateLibraryScheduleWithBadArguments(){
        Time startTime = START_TIME;
        Time endTime = END_TIME;
        int libraryId = 0;
        try {
            scheduleService.createLibrarySchedule(5, DayOfWeek.MONDAY, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NO-LIBRARY", e.getMessage());
        }
        try {
            scheduleService.createLibrarySchedule(libraryId, null, startTime, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-DAY-OF-WEEK", e.getMessage());
        }
        try {
            scheduleService.createLibrarySchedule(libraryId, DayOfWeek.MONDAY, endTime, startTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("START-TIME-AFTER-END-TIME", e.getMessage());
        }
        try {
            scheduleService.createLibrarySchedule(libraryId, DayOfWeek.MONDAY, null, endTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
        try {
            scheduleService.createLibrarySchedule(libraryId, DayOfWeek.MONDAY, startTime, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
    }

    @Test
    public void testGetLibrarySchedules(){
        assertEquals(7, scheduleService.getLibrarySchedules(LIBRARIAN.getId()).size());
        try{
            scheduleService.getLibrarySchedules(2);
            fail("Library with ID 2 should not exist");
        }catch (Exception exception){
            assertEquals(NoSuchElementException.class, exception.getClass());
        }
    }

    @Test
    public void testGetLibrarianSchedules(){
        assertEquals(2, scheduleService.getLibrarianSchedules(LIBRARIAN.getId()).size());
        try {
            scheduleService.getLibrarianSchedules(2);
        }catch (IllegalArgumentException exception){
            assertEquals("NO-SCHEDULE", exception.getMessage());
        }
    }

    @Test
    public void testGetLibrarianScheduleByDay(){
        assertEquals(DayOfWeek.MONDAY, scheduleService.getLibrarianScheduleByDay(LIBRARIAN.getId(), DayOfWeek.MONDAY).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, scheduleService.getLibrarianScheduleByDay(LIBRARIAN.getId(), DayOfWeek.TUESDAY).getDayOfWeek());
        try {
            scheduleService.getLibrarianScheduleByDay(LIBRARIAN.getId(), DayOfWeek.FRIDAY);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NO-SCHEDULE", exception.getMessage());
        }
        try {
            scheduleService.getLibrarianScheduleByDay(2, DayOfWeek.FRIDAY);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NO-LIBRARIAN", exception.getMessage());
        }
        try {
            scheduleService.getLibrarianScheduleByDay(LIBRARIAN.getId(), null);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-DAY-OF-WEEK", exception.getMessage());
        } 
    }

    @Test
    public void testGetLibraryScheduleByDay(){
        assertEquals(DayOfWeek.MONDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.MONDAY).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.TUESDAY).getDayOfWeek());
        assertEquals(DayOfWeek.WEDNESDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.WEDNESDAY).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.THURSDAY).getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.FRIDAY).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.SATURDAY).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), DayOfWeek.SUNDAY).getDayOfWeek());
        try {
            scheduleService.getLibraryScheduleByDay(2, DayOfWeek.FRIDAY);
        } catch (IllegalArgumentException exception) {
            assertEquals("NO-LIBRARY", exception.getMessage());
        }
        try {
            scheduleService.getLibraryScheduleByDay(LIBRARY.getId(), null);
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-DAY-OF-WEEK", exception.getMessage());
        } 
        // also should test for missing day (NO-SCHEDULE errMsg), but tests assume there is a schedule every day (no days off)
    }

    @Test
    public void testAssignInexistentSchedule(){
        try {
            // note that id of 10000 does not exist
            scheduleService.assignSchedule(10000, LIBRARIAN.getId());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NO-SCHEDULE", e.getMessage());
        }
    }

    @Test
    public void testAssignSchedule(){
        DailySchedule nonConflictingDailySchedule = new DailySchedule();

        nonConflictingDailySchedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        nonConflictingDailySchedule.setStartTime(START_TIME); // 8am
        nonConflictingDailySchedule.setEndTime(END_TIME);   // 12pm 
        
        try {
            assertEquals(0, nonConflictingDailySchedule.getId());
            scheduleService.assignSchedule(nonConflictingDailySchedule.getId(), LIBRARIAN.getId());
        } catch (IllegalArgumentException exception) {
            fail();
        }
        try {
            scheduleService.assignSchedule(3242, LIBRARIAN.getId());
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NO-SCHEDULE", exception.getMessage());
        }
        try {
            scheduleService.assignSchedule(nonConflictingDailySchedule.getId(), 2);
            fail();
        } catch (Exception exception) {
            assertEquals(NoSuchElementException.class, exception.getClass());
        }
    }

    @Test
    public void testConflictingAssignSchedule(){
        DailySchedule conflictingSchedule = new DailySchedule();          
        conflictingSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        conflictingSchedule.setStartTime(START_TIME); //8am
        conflictingSchedule.setEndTime(END_TIME);   //12pm
        conflictingSchedule.setId(1);
        try {
            scheduleService.assignSchedule(conflictingSchedule.getId(), LIBRARIAN.getId());
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("OVERLAP-SCHEDULE-ASSIGNMENT", exception.getMessage());
        }
    }

    @Test
    public void testSetLibrarySchedule(){
        ArrayList<DailySchedule> newSchedules = createWeekSchedule();
        ArrayList<Integer> newScheduleIds = new ArrayList<Integer>();
        for (DailySchedule schedule: newSchedules){
            newScheduleIds.add(schedule.getId());
        }
        try{
            scheduleService.setLibrarySchedule(newScheduleIds, LIBRARY.getId());
        }catch(IllegalArgumentException exception){
            fail("Not set properly");
        }
        try{
            scheduleService.setLibrarySchedule(newScheduleIds, 2);
            fail();
        }catch(IllegalArgumentException exception){
            assertEquals("NO-LIBRARY", exception.getMessage());
        }
        try {
            newScheduleIds.remove(6);
            scheduleService.setLibrarySchedule(newScheduleIds, LIBRARY.getId());
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NOT-WEEK-LIBRARY-SCHEDULE", exception.getMessage());
        }
    }

    @Test
    public void testUpdateSchedule(){
        int existingScheduleId = 0;
        int notExistingScheduleId = 500;
        try {
            scheduleService.updateSchedule(existingScheduleId, START_TIME, END_TIME);
        }catch (Exception exception){
            fail();
        }
        try {
            scheduleService.updateSchedule(notExistingScheduleId, START_TIME, END_TIME);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NO-SCHEDULE", exception.getMessage());
        }
        try {
            scheduleService.updateSchedule(existingScheduleId, null, END_TIME);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("NULL-TIME", exception.getMessage());
        }
        try {
            scheduleService.updateSchedule(existingScheduleId, START_TIME, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("NULL-TIME", e.getMessage());
        }
        try {
            scheduleService.updateSchedule(existingScheduleId, END_TIME, START_TIME);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("START-TIME-AFTER-END-TIME", e.getMessage());
        }
        
        scheduleService.updateSchedule(existingScheduleId, START_TIME, END_TIME);
    }

    // helper method to create a week worth of schedules
    private ArrayList<DailySchedule> createWeekSchedule(){
        ArrayList<DailySchedule> librarySchedule = new ArrayList<DailySchedule>();
        DailySchedule schedule = new DailySchedule();
        for (int i = 0; i<7; i++){
            schedule.setId(i);
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
}
