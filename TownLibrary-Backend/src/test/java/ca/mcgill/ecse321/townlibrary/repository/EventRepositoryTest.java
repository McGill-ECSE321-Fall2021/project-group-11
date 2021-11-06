package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import java.util.*;

@SpringBootTest
public class EventRepositoryTest {
    
    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    public void cleanupDB() {
        eventRepository.deleteAll();
        libraryRepository.deleteAll();
    }

    @Test
    public void testPersistEvent() {
        // Setup library for event
        final Library lib = new Library();
        lib.setId(592);
        libraryRepository.save(lib);

        // Test writes
        final Event event = new Event();
        event.setId(591);
        event.setName("cons");
        event.setLibrary(lib);
        eventRepository.save(event);

        // Testing if writes were successful
        Assertions.assertEquals(591, event.getId());
        Assertions.assertEquals("cons", event.getName());
        Assertions.assertEquals(lib, event.getLibrary());
        Assertions.assertEquals(lib.getId(), event.getLibrary().getId());

        // Test deletes
        eventRepository.delete(event);
        Assertions.assertFalse(eventRepository.findById(591).isPresent());
    }

    @Test
    public void testNameQueries() {
        // Create events to test queries
        Event event;
        event = new Event();
        event.setId(60);
        event.setName("con");
        eventRepository.save(event);

        event = new Event();
        event.setId(61);
        event.setName("pon");
        eventRepository.save(event);

        event = new Event();
        event.setId(62);
        event.setName("cat");
        eventRepository.save(event);

        // Test queries
        List<Event> l;
        Event e;
    
        e = eventRepository.findEventById(60);
        Assertions.assertEquals(60, e.getId());

        l = eventRepository.findByNameContaining("on");
        Assertions.assertEquals(2, l.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(60, 61)),
                new HashSet<>(Arrays.asList(l.get(0).getId(), l.get(1).getId())));

    }
}