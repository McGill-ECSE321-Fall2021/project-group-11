package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class EventService {
    
    @Autowired 
    private EventRepository eventRepository;

    @Transactional
    public Event createEvent(Library lib, int id, String name) {
        final Event e = new Event();
        e.setLibrary(lib);
        e.setId(id);
        e.setName(name);
        eventRepository.save(e);
        return e;
    }

    @Transactional
    public Event getEvent(int id) {
        return eventRepository.findEventById(id);
    }

    @Transactional
    public List<Event> getAllEvents() {
        final ArrayList<Event> events = new ArrayList<>();
        for (final Event e : eventRepository.findAll()) {
            events.add(e);
        }
        return events;
    }
}