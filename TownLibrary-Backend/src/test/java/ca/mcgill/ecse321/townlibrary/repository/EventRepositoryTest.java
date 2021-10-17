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
        final Library lib = new Library();
        lib.setId(592);
        libraryRepository.save(lib);

        final Event event = new Event();
        event.setId(591);
        event.setName("cons");
        event.setLibrary(lib);
        eventRepository.save(event);

        Assertions.assertEquals(591, event.getId());
        Assertions.assertEquals("cons", event.getName());
        Assertions.assertEquals(lib, event.getLibrary());
        Assertions.assertEquals(lib.getId(), event.getLibrary().getId());

        eventRepository.delete(event);
        Assertions.assertTrue(eventRepository.findById(591).isEmpty());
    }

    @Test
    public void testNameQueries() {
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

        List<Event> l;
        Event e;

        e = eventRepository.findEventById(60);
        Assertions.assertEquals(60, e.getId());

        l = eventRepository.findByNameContaining("con");
        Assertions.assertEquals(1, l.size());
        Assertions.assertEquals("con", l.get(0).getName());

    }
}
