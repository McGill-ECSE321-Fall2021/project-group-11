package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
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
    @Transactional
    public void testPersistEvent() throws Exception {
        final Library lib = new Library();
        final Event event = new Event();
        libraryRepository.save(lib);

        event.setId(59);
        event.setName("con");
        event.setLibrary(lib);
        eventRepository.save(event);

        Assertions.assertEquals(59, event.getId());
        Assertions.assertEquals("con", event.getName());
        Assertions.assertEquals(lib, event.getLibrary());

        eventRepository.delete(event);
        Assertions.assertTrue(eventRepository.findById(5).isEmpty());
    }

    @Test
    public void testNameQueries() throws Exception {
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
