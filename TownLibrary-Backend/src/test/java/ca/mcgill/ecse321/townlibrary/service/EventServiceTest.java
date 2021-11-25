package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.EventRepository;

import java.util.Optional;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository mockEventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        final Library lib = new Library();
        final String name = "my birthday";
        final int id = 10001;
        final Transaction transaction = new Transaction();

        final Event e = eventService.createEvent(lib, name);
        e.setTransaction(transaction);
        Assertions.assertEquals(lib.getId(), e.getLibrary().getId());
        Assertions.assertEquals(name, e.getName());
        Assertions.assertEquals(transaction, e.getTransaction());
    }

    @Test
    public void testCreateEventNullInputs() {
        final Library lib = null;
        final String name = null;
        final int id = 10002;


        try {
            eventService.createEvent(lib, name);
        }   catch (IllegalArgumentException e) {
            Assertions.assertEquals(e.getMessage(), "Invalid inputs");
        }
    }

    @Test
    public void testEventSetNullTransaction() {
        /*lenient().when(this.mockEventRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new Event()));

        Event e;*/
        final Event e = new Event();
        final Transaction transaction = null;

        try {
            eventService.setEventTransaction(e, transaction);
            Assertions.fail();
        }   catch (IllegalArgumentException er) {
            Assertions.assertEquals(er.getMessage(), "Invalid transaction");
        }
    }

    @Test
    public void testTransactionSetNullEvent() {
        final Transaction t = new Transaction();
        try {
            eventService.setEventTransaction(null, t);
            Assertions.fail();
        } catch (IllegalArgumentException er) {
            Assertions.assertEquals(er.getMessage(), "Invalid event");
        }
    }

    @Test
    public void testSetEventTransaction() {
        final Event e = new Event();
        final Transaction t = new Transaction();
        eventService.setEventTransaction(e, t);

        Assertions.assertEquals(t.getId(), e.getTransaction().getId());
    }

    @Test
    public void testGetEvent() {
        // Bind id 0 to an event for this test
        lenient().when(this.mockEventRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new Event()));

        Event e;

        e = this.eventService.getEventById(0);
        Assertions.assertEquals(0, e.getId());

        e = this.eventService.getEventById(1030);
        Assertions.assertNull(e);
    }

    @Test
    public void testGetEventByTransaction() {
        final Transaction KEY = new Transaction();
        KEY.setId(1); // say
        final Event VALUE = new Event();
        VALUE.setTransaction(KEY);
        lenient().when(this.mockEventRepository.findByTransaction(KEY))
                .thenAnswer(invocation -> VALUE);

        Event e;

        e = this.eventService.getEventByTransaction(KEY);
        Assertions.assertEquals(KEY.getId(), e.getTransaction().getId());

        e = this.eventService.getEventByTransaction(new Transaction());
        Assertions.assertNull(e);
    }

    /*@Test
    public void testAddUsersToEvent() {
        final UserRole u1 = new OnlineMember();
        u1.setId(4000);
        u1.setName("jogn ");
        final Event e = new Event();
        Assertions.assertEquals(0, e.getUsers().size());
        e.addUser(u1);
        Assertions.assertEquals(1, e.getUsers().size());
    }

    @Test
    public void testRemoveUsersFromEvent() {
        final UserRole u1 = new OnlineMember();
        u1.setId(4000);
        u1.setName("jogn ");
        final Event e = new Event();
        Assertions.assertEquals(0, e.getUsers().size());
        e.addUser(u1);
        Assertions.assertEquals(1, e.getUsers().size());
        e.removeUser(u1);
        Assertions.assertEquals(0, e.getUsers().size());
    }*/
}