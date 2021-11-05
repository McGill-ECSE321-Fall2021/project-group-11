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

        final Event e = eventService.createEvent(lib, id, name, transaction);
        Assertions.assertEquals(lib.getId(), e.getLibrary().getId());
        Assertions.assertEquals(name, e.getName());
        Assertions.assertEquals(id, e.getId());
        Assertions.assertEquals(transaction, e.getTransaction());
    }

    @Test
    public void testCreateEventNullInputs() {
        final Library lib = null;
        final String name = null;
        final int id = 10002;
        final Transaction transaction = null;

        try {
            eventService.createEvent(lib, id, name, transaction);
        }   catch (IllegalArgumentException e) {
            Assertions.assertEquals(e.getMessage(), "Invalid inputs");
        }
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
}