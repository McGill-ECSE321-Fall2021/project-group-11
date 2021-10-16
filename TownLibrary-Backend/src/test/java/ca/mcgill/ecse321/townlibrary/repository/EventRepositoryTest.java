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
    private EntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    public void cleanupDB() {
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistEvent() throws Exception {
        final Library lib = new Library();
        entityManager.persist(lib);
        entityManager.flush();

        final Event event = new Event();
        event.setId(5);
        event.setName("con");
        event.setLibrary(lib);
        eventRepository.save(event);

        Assertions.assertEquals(5, event.getId());
        Assertions.assertEquals("con", event.getName());
        Assertions.assertEquals(lib, event.getLibrary());

        eventRepository.delete(event);
        Assertions.assertTrue(eventRepository.findById(5).isEmpty());

        entityManager.remove(lib);
        entityManager.flush();
    }

    @Test
    public void testNameQueries() throws Exception {
        Event event;
        event = new Event();
        event.setId(5);
        event.setName("con");
        eventRepository.save(event);

        event = new Event();
        event.setId(6);
        event.setName("pon");
        eventRepository.save(event);

        event = new Event();
        event.setId(7);
        event.setName("cat");
        eventRepository.save(event);

        List<Event> l;
        l = eventRepository.findEventById(5);
        Assertions.assertEquals(1, l.size());
        Assertions.assertEquals(5, l.get(0).getId());

        l = eventRepository.findByName("con");
        Assertions.assertEquals(1, l.size());
        Assertions.assertEquals("con", l.get(0).getName());

    }
}
