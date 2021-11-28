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

import static org.junit.jupiter.api.Assertions.assertTrue;
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

        final Event e = eventService.createEvent(lib, name);
        Assertions.assertEquals(lib.getId(), e.getLibrary().getId());
        Assertions.assertEquals(name, e.getName());
    }

    @Test
    public void testCreateEventNullInputs() {
        final Library lib = null;
        final String name = null;

        try {
            eventService.createEvent(lib, name);
        }   catch (IllegalArgumentException e) {
            Assertions.assertEquals(e.getMessage(), "Invalid inputs");
        }
    }

    @Test
    public void testDeleteEvent() {
        final Event e = new Event();

        lenient().when(this.mockEventRepository.findById(0))
                .thenReturn(Optional.of(e)).thenReturn(Optional.empty());

        try {
            final boolean deleted = this.eventService.deleteEvent(0);
            assertTrue(deleted);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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

    @Test
    public void addAndRemoveUsersEvent() {
        final UserRole u1 = new OnlineMember();
        u1.setId(4000);
        u1.setName("jogn ");
        final Event e = new Event();
        Assertions.assertEquals(0, e.getUsers().size());
        eventService.addUserToEvent(e, u1);
        Assertions.assertEquals(1, e.getUsers().size());
        eventService.removeUserFromEvent(e, u1);
        Assertions.assertEquals(0, e.getUsers().size());
    }
}